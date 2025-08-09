package com.example.mymovieapplication.core.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Destination {
    val title: String
    val route: String
    val routeWithArgs: String
    val arguments: List<NamedNavArgument>
        get() = emptyList()
}

data object Home : Destination {
    override val title: String = "Movies"
    override val route: String = "home"
    override val routeWithArgs: String = route
}

data object Detail : Destination {
    override val title: String = "Movie details"
    override val route: String = "detail"
    override val routeWithArgs: String = "$route/{$MOVIE_ID_ARG}"

    const val MOVIE_ID_ARG = "movieId"

    override val arguments = listOf(
        navArgument(MOVIE_ID_ARG) { type = NavType.IntType }
    )

    fun createRoute(movieId: Int): String = "$route/$movieId"
}

val movieDestinations = listOf(Home, Detail)















