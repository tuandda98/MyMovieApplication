package com.example.mymovieapplication.data.repository

import com.example.mymovieapplication.data.remote.RemoteDataSource
import com.example.mymovieapplication.data.util.toMovie
import com.example.mymovieapplication.domain.model.Movie
import com.example.mymovieapplication.domain.repository.MovieRepository

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
}