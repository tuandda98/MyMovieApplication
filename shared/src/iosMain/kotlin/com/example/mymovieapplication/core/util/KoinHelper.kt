package com.example.mymovieapplication.core.util

import com.example.mymovieapplication.core.di.getSharedModules
import org.koin.core.context.startKoin

fun initKoin(){
    startKoin {
        modules(getSharedModules())
    }
}