package com.example.mymovieapplication.feature.movie.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class ListMoviesDto(
    val results: List<MovieDto>
)