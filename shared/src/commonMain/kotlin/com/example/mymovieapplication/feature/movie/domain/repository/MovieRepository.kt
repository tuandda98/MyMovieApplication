package com.example.mymovieapplication.feature.movie.domain.repository

import com.example.mymovieapplication.core.util.Result
import com.example.mymovieapplication.feature.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

internal interface MovieRepository {
    fun getMovies(page: Int): Flow<Result<List<Movie>>>
    fun getMovie(movieId: Int): Flow<Result<Movie>>
    fun searchMovies(query: String, page: Int): Flow<Result<List<Movie>>>
}