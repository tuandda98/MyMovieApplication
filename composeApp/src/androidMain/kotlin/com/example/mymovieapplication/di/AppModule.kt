package com.example.mymovieapplication.di

import com.example.mymovieapplication.detail.DetailViewModel
import com.example.mymovieapplication.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { params -> DetailViewModel(getMovieUseCase = get(), movieId = params.get()) }
}