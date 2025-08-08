package com.example.mymovieapplication.di

import com.example.mymovieapplication.data.remote.MovieService
import com.example.mymovieapplication.data.remote.RemoteDataSource
import com.example.mymovieapplication.data.repository.MovieRepositoryImpl
import com.example.mymovieapplication.domain.repository.MovieRepository
import com.example.mymovieapplication.domain.usecase.GetMovieUseCase
import com.example.mymovieapplication.domain.usecase.GetMoviesUseCase
import com.example.mymovieapplication.util.provideDispatcher
import org.koin.dsl.module

private val dataModule = module {
    factory { RemoteDataSource(get(), get()) }
    factory { MovieService() }
}

private val utilityModule = module {
    factory { provideDispatcher() }
}

private val domainModule = module {
    single<MovieRepository> { MovieRepositoryImpl(get()) }
    factory { GetMoviesUseCase() }
    factory { GetMovieUseCase() }
}

private val sharedModules = listOf(domainModule, dataModule, utilityModule)

fun getSharedModules() = sharedModules












