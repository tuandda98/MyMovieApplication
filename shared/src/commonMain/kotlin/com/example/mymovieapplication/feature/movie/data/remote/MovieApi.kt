package com.example.mymovieapplication.feature.movie.data.remote

import com.example.mymovieapplication.core.network.KtorApi
import com.example.mymovieapplication.feature.movie.data.remote.dto.MovieDto
import com.example.mymovieapplication.feature.movie.data.remote.dto.ListMoviesDto
import io.ktor.client.call.*
import io.ktor.client.request.*

internal class MovieApi: KtorApi() {

    suspend fun getMovies(page: Int = 1): ListMoviesDto = try {
        val response = client.get {
            pathUrl("trending/movie/day")
            parameter("page", page)
            parameter("language", "en-US")
        }
        response.body()
    } catch (e: Exception) {
        throw e
    }

    suspend fun getMovie(movieId: Int): MovieDto = try {
        val response = client.get {
            pathUrl("movie/${movieId}")
        }
        response.body()
    } catch (e: Exception) {
        throw e
    }
}