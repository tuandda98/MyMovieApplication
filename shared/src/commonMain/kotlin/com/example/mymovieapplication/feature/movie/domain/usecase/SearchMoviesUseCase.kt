package com.example.mymovieapplication.feature.movie.domain.usecase

import com.example.mymovieapplication.feature.movie.domain.model.Movie
import com.example.mymovieapplication.feature.movie.domain.repository.MovieRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchMoviesUseCase: KoinComponent {
    private val repository: MovieRepository by inject()

    @Throws(Exception::class)
    suspend operator fun invoke(query: String, page: Int): List<Movie> {
        return repository.searchMovies(query, page)
    }
}
