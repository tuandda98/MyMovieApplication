package com.example.mymovieapplication.feature.movie.data.repository

import com.example.mymovieapplication.feature.movie.data.remote.RemoteDataSource
import com.example.mymovieapplication.feature.movie.data.remote.dto.toMovie
import com.example.mymovieapplication.feature.movie.domain.model.Movie
import com.example.mymovieapplication.feature.movie.domain.repository.MovieRepository

internal class MovieRepositoryImpl(
    private val remoteDateSource: RemoteDataSource
): MovieRepository {

    override suspend fun getMovies(page: Int): List<Movie> {
        return remoteDateSource.getMovies(page = page).results.map {
            it.toMovie()
        }
    }

    override suspend fun getMovie(movieId: Int): Movie {
        return remoteDateSource.getMovie(movieId = movieId).toMovie()
    }

    override suspend fun searchMovies(query: String, page: Int): List<Movie> {
        return remoteDateSource.searchMovies(query, page).results.map { it.toMovie() }
    }
}