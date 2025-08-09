package com.example.mymovieapplication.feature_home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mymovieapplication.core.ui.ErrorSnackbarHandler
import com.example.mymovieapplication.core.ui.Red
import com.example.mymovieapplication.feature.movie.domain.model.Movie

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeScreenState,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    loadNextMovies: (Boolean) -> Unit,
    navigateToDetail: (Movie) -> Unit
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.refreshing,
        onRefresh = { loadNextMovies(true) }
    )
    val scaffoldState = rememberScaffoldState()

    ErrorSnackbarHandler(
        scaffoldState = scaffoldState,
        errorMessage = uiState.errorMessage
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .pullRefresh(state = pullRefreshState)
    ) {
        Column {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search...") }
            )

            Text(
                text = if (searchQuery.isEmpty()) "Trending movies" else "Search results",
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 8.dp)
            )

            // Show empty state when search has no results
            if (searchQuery.isNotEmpty() && uiState.movies.isEmpty() && !uiState.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No movies found",
                            style = MaterialTheme.typography.h6,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "Try searching with different keywords",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        uiState.movies,
                        key = { index, movie -> "${movie.id}_$index" }
                    ) { index, movie ->
                        MovieListItem(movie = movie, onMovieClick = { navigateToDetail(movie) })

                        if (searchQuery.isEmpty() && index >= uiState.movies.size - 1 && !uiState.loading && !uiState.loadFinished) {
                            // Only load next page for trending, not for search
                            androidx.compose.runtime.LaunchedEffect(key1 = Unit) {
                                loadNextMovies(false)
                            }
                        }
                    }

                    if (uiState.loading && uiState.movies.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    color = Red
                                )
                            }
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = uiState.refreshing,
            state = pullRefreshState,
            modifier = modifier.align(Alignment.TopCenter)
        )
    }
}

















