package com.example.mymovieapplication

import android.app.Application
import com.example.mymovieapplication.core.di.appModule
import com.example.mymovieapplication.core.di.getSharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApplication)  // Add this line
            modules(appModule + getSharedModules())
        }
    }
}