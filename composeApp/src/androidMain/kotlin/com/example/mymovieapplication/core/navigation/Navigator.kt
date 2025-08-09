package com.example.mymovieapplication.core.navigation

import androidx.navigation.NavHostController

class Navigator(private val navController: NavHostController) {
    fun navigateToDetail(movieId: Int) {
        navController.navigate(Detail.createRoute(movieId))
    }

    fun navigateBack() {
        navController.navigateUp()
    }

}