package com.example.mymovieapplication.core.di

import com.example.mymovieapplication.core.database.DatabaseDriverFactory
import com.example.mymovieapplication.feature.movie.data.local.MovieDatabase

actual fun createDatabase(driverFactory: DatabaseDriverFactory): MovieDatabase {
    return MovieDatabase(driverFactory.createDriver())
}