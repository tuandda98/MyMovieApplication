package com.example.mymovieapplication.feature_home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovieapplication.feature.movie.domain.model.Movie
import com.example.mymovieapplication.feature.movie.domain.usecase.GetMoviesUseCase
import com.example.mymovieapplication.feature.movie.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
): ViewModel(){
    var searchQuery by mutableStateOf("")
    var uiState by mutableStateOf(HomeScreenState())
    private var currentPage = 1

    init {
        loadMovies(forceReload = false)
    }


    fun loadMovies(forceReload: Boolean = false){
        if (uiState.loading) return
        if (forceReload) currentPage = 1
        if (currentPage == 1) uiState = uiState.copy(refreshing = true)

        viewModelScope.launch {
            uiState = uiState.copy(
                loading = true
            )

            try {
                val resultMovies = getMoviesUseCase(page = currentPage)
                val movies = if (currentPage == 1) resultMovies else uiState.movies + resultMovies

                currentPage += 1
                uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = resultMovies.isEmpty(),
                    movies = movies
                )

            }catch (error: Throwable){
                uiState = uiState.copy(
                    loading = false,
                    refreshing = false,
                    loadFinished = true,
                    errorMessage = "Could not load movies: ${error.localizedMessage}"
                )
            }
        }
    }


    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        if (query.isEmpty()) {
            loadMovies(forceReload = true)
        } else {
            searchMovies(query)
        }
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)
            try {
                val movies = searchMoviesUseCase(query, 1)
                uiState = uiState.copy(
                    loading = false,
                    movies = movies,
                    loadFinished = true
                )
            } catch (error: Throwable) {
                uiState = uiState.copy(
                    loading = false,
                    errorMessage = "Could not search movies: ${error.localizedMessage}"
                )
            }
        }
    }
}




data class HomeScreenState(
    var loading: Boolean = false,
    var refreshing: Boolean = false,
    var movies: List<Movie> = listOf(),
    var errorMessage: String? = null,
    var loadFinished: Boolean = false
)



















