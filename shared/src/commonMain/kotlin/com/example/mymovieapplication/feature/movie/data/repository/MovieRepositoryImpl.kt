package com.example.mymovieapplication.feature.movie.data.repository

import com.example.mymovieapplication.feature.movie.data.remote.RemoteDataSource
import com.example.mymovieapplication.feature.movie.data.remote.dto.toMovie
import com.example.mymovieapplication.feature.movie.domain.model.Movie
import com.example.mymovieapplication.feature.movie.domain.repository.MovieRepository

internal class MovieRepositoryImpl(
    private val remoteDateSource: RemoteDataSource
): MovieRepository {

    override suspend fun getMovies(page: Int): List<Movie> {
        return if (page == 1) {
            // Use cached trending movies for page 1
            remoteDateSource.getMoviesFromDB()
        } else {
            // Fetch directly from API for other pages
            remoteDateSource.getMovies(page = page).results.map { it.toMovie() }
        }
    }

    override suspend fun getMovie(movieId: Int): Movie {
        return try {
            remoteDateSource.getMovie(movieId = movieId).toMovie()
        } catch (e: Exception) {
            remoteDateSource.getMovieFromDB(movieId)
                ?: throw Exception("Movie not found in cache and network unavailable")
        }
    }

    override suspend fun searchMovies(query: String, page: Int): List<Movie> {
        return remoteDateSource.searchMovies(query, page).results.map { it.toMovie() }
    }
}