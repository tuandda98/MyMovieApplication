package com.example.mymovieapplication.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mymovieapplication.core.navigation.Detail
import com.example.mymovieapplication.core.navigation.Home
import com.example.mymovieapplication.core.navigation.Navigator
import com.example.mymovieapplication.core.navigation.movieDestinations
import com.example.mymovieapplication.feature_detail.DetailScreen
import com.example.mymovieapplication.feature_detail.DetailViewModel
import com.example.mymovieapplication.feature_home.HomeScreen
import com.example.mymovieapplication.feature_home.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MainAppComposable() {
    val navController = rememberNavController()
    val navigator = remember(navController) { Navigator(navController) }
    val systemUiController = rememberSystemUiController()
    val scaffoldState = rememberScaffoldState()

    val isSystemDark = isSystemInDarkTheme()
    val statusBarColor = if (isSystemDark) {
        MaterialTheme.colors.primaryVariant
    } else {
        Color.Transparent
    }

    SideEffect {
        systemUiController.setStatusBarColor(statusBarColor, darkIcons = !isSystemDark)
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = movieDestinations.find {
        backStackEntry?.destination?.route == it.route ||
                backStackEntry?.destination?.route == it.routeWithArgs
    } ?: Home

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                MovieAppBar(
                    canNavigateBack = navController.previousBackStackEntry != null,
                    currentScreen = currentScreen,
                    onNavigateBack = navigator::navigateBack,

                )
            }
        ) { innerPaddings ->
            NavHost(
                navController = navController,
                modifier = Modifier.padding(innerPaddings),
                startDestination = Home.route
            ) {
                composable(Home.route) {
                    val homeViewModel: HomeViewModel = koinViewModel()
                    val uiState by homeViewModel.uiState.collectAsState()
                    val searchQuery by homeViewModel.searchQuery.collectAsState()

                    HomeScreen(
                        uiState = uiState,
                        searchQuery = searchQuery,
                        onSearchQueryChanged = homeViewModel::onSearchQueryChanged,
                        loadNextMovies = homeViewModel::loadMovies,
                        navigateToDetail = { movie ->
                            navigator.navigateToDetail(movie.id)
                        }
                    )
                }

                composable(
                    route = Detail.routeWithArgs,
                    arguments = Detail.arguments
                ) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getInt(Detail.MOVIE_ID_ARG) ?: 0
                    val detailViewModel: DetailViewModel = koinViewModel(
                        parameters = { parametersOf(movieId) }
                    )
                    val uiState by detailViewModel.uiState.collectAsState()

                    DetailScreen(
                        uiState = uiState,
                    )
                }
            }
        }

    }
}















