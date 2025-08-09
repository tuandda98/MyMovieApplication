package com.example.mymovieapplication.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://api.themoviedb.org/3"
private const val BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI2NGIwMTkyNmZhYTg2ODRlMmJiY2ZkZTI4ZTgwNTZkOSIsIm5iZiI6MTc0OTcxNjc2OC4xNDksInN1YiI6IjY4NGE4ZjIwNzBlODY4Yjg4MGIwZDNiNCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.7DDv7c1TnQJn7KI9gAbBK-8R37JOBt7eQmB9-nqlePM"

internal abstract class KtorApi {
    val client = HttpClient {
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    fun HttpRequestBuilder.pathUrl(path: String){
        url("$BASE_URL/$path")
        header(HttpHeaders.Authorization, "Bearer $BEARER_TOKEN")
        header(HttpHeaders.Accept, "application/json")
    }
}