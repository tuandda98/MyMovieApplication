package com.example.mymovieapplication.core.di

import com.example.mymovieapplication.feature_detail.DetailViewModel
import com.example.mymovieapplication.feature_home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get(),get()) }
    viewModel { params -> DetailViewModel(getMovieUseCase = get(), movieId = params.get()) }
}