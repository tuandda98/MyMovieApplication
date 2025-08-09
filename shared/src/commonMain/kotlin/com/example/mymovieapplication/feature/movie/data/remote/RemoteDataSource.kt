package com.example.mymovieapplication.feature.movie.data.remote

import com.example.mymovieapplication.core.util.Dispatcher
import com.example.mymovieapplication.feature.movie.data.local.MoviesLocalDataSource
import com.example.mymovieapplication.feature.movie.data.remote.dto.toMovie
import com.example.mymovieapplication.feature.movie.domain.model.Movie
import kotlinx.coroutines.withContext

internal class RemoteDataSource(
    private val apiService: MovieApi,
    private val dispatcher: Dispatcher,
    private val localDataSource: MoviesLocalDataSource
) {

    // Cache validity: 24 hours in milliseconds
    private val cacheValidityDuration = 24 * 60 * 60 * 1000L

    suspend fun getMoviesFromDB(): List<Movie> = withContext(dispatcher.io) {
        localDataSource.getTrendingMovies(cacheValidityDuration) {
            // Fetch from remote API when cache is invalid
            apiService.getMovies(page = 1).results.map { it.toMovie() }
        }
    }
    suspend fun getMovieFromDB(movieId: Int): Movie? = withContext(dispatcher.io) {
        localDataSource.getMovieById(movieId)
    }
    suspend fun getMovies(page: Int) = withContext(dispatcher.io) {
        apiService.getMovies(page = page)
    }

    suspend fun getMovie(movieId: Int) = withContext(dispatcher.io) {
        apiService.getMovie(movieId = movieId)
    }

    suspend fun searchMovies(query: String, page: Int) = withContext(dispatcher.io) {
        apiService.searchMovies(query, page)
    }
}