package com.example.mymovieapplication.feature.movie.domain.usecase

import com.example.mymovieapplication.core.util.Result
import com.example.mymovieapplication.feature.movie.domain.model.Movie
import com.example.mymovieapplication.feature.movie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetMovieUseCase: KoinComponent {
    private val repository: MovieRepository by inject()

    operator fun invoke(movieId: Int): Flow<Result<Movie>> {
        return repository.getMovie(movieId)
    }
}