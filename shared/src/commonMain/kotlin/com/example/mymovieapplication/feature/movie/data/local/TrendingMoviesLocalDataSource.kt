package com.example.mymovieapplication.feature.movie.data.local

import com.example.mymovieapplication.feature.movie.domain.model.Movie
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

internal class TrendingMoviesLocalDataSource(
    private val database: MovieDatabase
) {
    private val queries = database.movieDatabaseQueries

    @OptIn(ExperimentalTime::class)
    suspend fun getTrendingMovies(
        validityDurationMillis: Long,
        fetchFromNetwork: suspend () -> List<Movie>
    ): List<Movie> {
        return if (isCacheValid(validityDurationMillis)) {
            // Return cached data
            queries.selectAll().executeAsList().map {
                Movie(
                    id = it.id.toInt(),
                    title = it.title,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    releaseDate = it.releaseDate
                )
            }
        } else {
            // Fetch from network and update cache
            val movies = fetchFromNetwork()
            saveTrendingMovies(movies)
            movies
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun saveTrendingMovies(movies: List<Movie>) {
        val currentTimestamp = Clock.System.now().toEpochMilliseconds()
        queries.transaction {
            queries.clearAll()
            movies.forEach { movie ->
                queries.insert(
                    id = movie.id.toLong(),
                    title = movie.title,
                    description = movie.description,
                    imageUrl = movie.imageUrl,
                    releaseDate = movie.releaseDate,
                    cacheTimestamp = currentTimestamp
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun isCacheValid(validityDurationMillis: Long): Boolean {
        val currentTimestamp = Clock.System.now().toEpochMilliseconds()
        val lastCacheTimestamp = queries.getLatestTimestamp()
            .executeAsOneOrNull()?.latestTimestamp ?: 0L
        return (currentTimestamp - lastCacheTimestamp) <= validityDurationMillis
    }
}