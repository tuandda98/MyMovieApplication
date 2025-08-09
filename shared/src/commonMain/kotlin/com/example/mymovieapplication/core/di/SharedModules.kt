package com.example.mymovieapplication.core.di

import com.example.mymovieapplication.core.database.DatabaseDriverFactory
import com.example.mymovieapplication.core.util.provideDispatcher
import com.example.mymovieapplication.feature.movie.data.local.MovieDatabase
import com.example.mymovieapplication.feature.movie.data.local.TrendingMoviesLocalDataSource
import com.example.mymovieapplication.feature.movie.data.remote.MovieApi
import com.example.mymovieapplication.feature.movie.data.remote.RemoteDataSource
import com.example.mymovieapplication.feature.movie.data.repository.MovieRepositoryImpl
import com.example.mymovieapplication.feature.movie.domain.repository.MovieRepository
import com.example.mymovieapplication.feature.movie.domain.usecase.GetMovieUseCase
import com.example.mymovieapplication.feature.movie.domain.usecase.GetMoviesUseCase
import com.example.mymovieapplication.feature.movie.domain.usecase.SearchMoviesUseCase
import org.koin.dsl.module

private val dataModule = module {
    single<MovieDatabase> { createDatabase(get()) }
    factory { TrendingMoviesLocalDataSource(get()) }
    factory { RemoteDataSource(get(), get(), get()) }
    factory { MovieApi() }
}

private val utilityModule = module {
    factory { provideDispatcher() }
}

private val domainModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get()) }
    factory { GetMoviesUseCase() }
    factory { GetMovieUseCase() }
    factory { SearchMoviesUseCase() }
}

private val sharedModules = listOf(domainModule, dataModule, utilityModule)

fun getSharedModules() = sharedModules

expect fun createDatabase(driverFactory: DatabaseDriverFactory): MovieDatabase