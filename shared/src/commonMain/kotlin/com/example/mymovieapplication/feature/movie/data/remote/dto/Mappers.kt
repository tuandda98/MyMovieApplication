package com.example.mymovieapplication.feature.movie.data.remote.dto

import com.example.mymovieapplication.feature.movie.domain.model.Movie

internal fun MovieDto.toMovie(): Movie{
    return Movie(
        id = id,
        title = title,
        description = overview,
        imageUrl = getImageUrl(posterImage),
        releaseDate = releaseDate
    )
}

private fun getImageUrl(posterImage: String) = "https://image.tmdb.org/t/p/w500/$posterImage"