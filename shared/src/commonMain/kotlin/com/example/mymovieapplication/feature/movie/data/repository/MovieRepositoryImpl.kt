package com.example.mymovieapplication.feature.movie.data.repository

import com.example.mymovieapplication.core.util.Result
import com.example.mymovieapplication.core.util.safeApiCall
import com.example.mymovieapplication.feature.movie.data.remote.RemoteDataSource
import com.example.mymovieapplication.feature.movie.data.remote.dto.toMovie
import com.example.mymovieapplication.feature.movie.domain.model.Movie
import com.example.mymovieapplication.feature.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class MovieRepositoryImpl(
    private val remoteDateSource: RemoteDataSource
) : MovieRepository {

    override fun getMovies(page: Int): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        val result = if (page == 1) {
            safeApiCall { remoteDateSource.getMoviesFromDB() }
        } else {
            safeApiCall {
                remoteDateSource.getMovies(page = page).results.map { it.toMovie() }
            }
        }
        emit(result)
    }

    override fun getMovie(movieId: Int): Flow<Result<Movie>> = flow {
        emit(Result.Loading)
        val result = safeApiCall {
            remoteDateSource.getMovie(movieId = movieId).toMovie()
        }

        when (result) {
            is Result.Success -> emit(result)
            is Result.Error -> {
                val cachedMovie = remoteDateSource.getMovieFromDB(movieId)
                if (cachedMovie != null) {
                    emit(Result.Success(cachedMovie))
                } else {
                    emit(Result.Error(Exception("Movie not found in cache and network unavailable")))
                }
            }
            is Result.Loading -> emit(result)
        }
    }

    override fun searchMovies(query: String, page: Int): Flow<Result<List<Movie>>> = flow {
        emit(Result.Loading)
        val result = safeApiCall {
            remoteDateSource.searchMovies(query, page).results.map { it.toMovie() }
        }
        emit(result)
    }
}