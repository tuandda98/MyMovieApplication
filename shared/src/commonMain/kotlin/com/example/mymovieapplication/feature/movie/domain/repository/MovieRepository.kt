package com.example.mymovieapplication.feature.movie.domain.repository

import com.example.mymovieapplication.feature.movie.domain.model.Movie

internal interface MovieRepository {
    suspend fun getMovies(page: Int): List<Movie>

    suspend fun getMovie(movieId: Int): Movie
}