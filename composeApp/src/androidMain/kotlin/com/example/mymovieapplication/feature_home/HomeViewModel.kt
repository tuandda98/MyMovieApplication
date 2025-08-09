package com.example.mymovieapplication.feature_home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapplication.core.util.Result
import com.example.mymovieapplication.feature.movie.domain.model.Movie
import com.example.mymovieapplication.feature.movie.domain.usecase.GetMoviesUseCase
import com.example.mymovieapplication.feature.movie.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
): ViewModel(){

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState: StateFlow<HomeScreenState> = _uiState.asStateFlow()

    private var currentPage = 1

    init {
        loadMovies(forceReload = false)
    }

    fun loadMovies(forceReload: Boolean = false){
        if (_uiState.value.loading) return
        if (forceReload) currentPage = 1
        if (currentPage == 1) _uiState.value = _uiState.value.copy(refreshing = true)

        viewModelScope.launch {
            getMoviesUseCase(page = currentPage).collect { result ->
                when(result) {
                    is Result.Success -> {
                        val resultMovies = result.data
                        val movies = if (currentPage == 1) resultMovies else _uiState.value.movies + resultMovies

                        currentPage += 1
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            refreshing = false,
                            loadFinished = resultMovies.isEmpty(),
                            movies = movies,
                            errorMessage = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            refreshing = false,
                            loadFinished = true,
                            errorMessage = result.exception.message ?: "Could not load movies"
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(loading = true)
                    }
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        if (query.isEmpty()) {
            loadMovies(forceReload = true)
        } else {
            searchMovies(query)
        }
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            searchMoviesUseCase(query, 1).collect { result ->
                when(result) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            movies = result.data,
                            loadFinished = true,
                            errorMessage = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            errorMessage = result.exception.message ?: "Could not search movies"
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

data class HomeScreenState(
    val loading: Boolean = false,
    val refreshing: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val loadFinished: Boolean = false
)



















