package com.example.mymovieapplication.data.remote

import io.ktor.client.call.*
import io.ktor.client.request.*

internal class MovieService: KtorApi() {

    suspend fun getMovies(page: Int = 1): MoviesResponse = try {
        val response = client.get {
            pathUrl("trending/movie/day")
            parameter("page", page)
            parameter("language", "en-US")
        }
        response.body()
    } catch (e: Exception) {
        throw e
    }

    suspend fun getMovie(movieId: Int): MovieRemote = try {
        val response = client.get {
            pathUrl("movie/${movieId}")
        }
        response.body()
    } catch (e: Exception) {
        throw e
    }
}