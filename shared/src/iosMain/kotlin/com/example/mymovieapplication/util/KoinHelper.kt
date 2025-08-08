package com.example.mymovieapplication.util

import com.example.mymovieapplication.di.getSharedModules
import org.koin.core.context.startKoin

fun initKoin(){
    startKoin {
        modules(getSharedModules())
    }
}