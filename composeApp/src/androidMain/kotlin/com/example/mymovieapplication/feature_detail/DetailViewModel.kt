package com.example.mymovieapplication.feature_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapplication.core.util.Result
import com.example.mymovieapplication.feature.movie.domain.model.Movie
import com.example.mymovieapplication.feature.movie.domain.usecase.GetMovieUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getMovieUseCase: GetMovieUseCase,
    movieId: Int
): ViewModel(){

    private val _uiState = MutableStateFlow(DetailScreenState())
    val uiState: StateFlow<DetailScreenState> = _uiState.asStateFlow()

    init {
        loadMovie(movieId)
    }

    private fun loadMovie(movieId: Int){
        viewModelScope.launch {
            getMovieUseCase(movieId = movieId).collect { result ->
                when(result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            movie = result.data,
                            errorMessage = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            movie = null,
                            errorMessage = result.exception.message ?: "Could not load the movie"
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(loading = true)
                    }
                }
            }
        }
    }
}

data class DetailScreenState(
    val loading: Boolean = false,
    val movie: Movie? = null,
    val errorMessage: String? = null
)










