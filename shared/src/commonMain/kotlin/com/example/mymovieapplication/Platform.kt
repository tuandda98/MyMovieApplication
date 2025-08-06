package com.example.mymovieapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform