package com.example.mymovieapplication

import android.app.Application
import com.example.mymovieapplication.di.appModule
import com.example.mymovieapplication.di.getSharedModules
import org.koin.core.context.startKoin

class Movie: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModule + getSharedModules())
        }
    }
}