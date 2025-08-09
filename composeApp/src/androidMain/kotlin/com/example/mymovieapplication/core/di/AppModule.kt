package com.example.mymovieapplication.core.di

import com.example.mymovieapplication.core.database.AndroidDatabaseDriverFactory
import com.example.mymovieapplication.core.database.DatabaseDriverFactory
import com.example.mymovieapplication.feature_detail.DetailViewModel
import com.example.mymovieapplication.feature_home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get(),get()) }
    viewModel { params -> DetailViewModel(getMovieUseCase = get(), movieId = params.get()) }
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(androidContext()) }
}