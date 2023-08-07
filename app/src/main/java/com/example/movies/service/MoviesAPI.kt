package com.example.movies.service

import android.util.Log
import com.example.movies.domain.Credits
import com.example.movies.domain.GenreList
import com.example.movies.domain.Movie
import com.example.movies.repository.mapper.MovieRepositoryMapper
import com.example.movies.service.model.CreditsModel
import com.example.movies.service.model.GenreListModel
import com.example.movies.service.model.MovieModel
import com.example.movies.service.model.ResponseModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import kotlinx.serialization.json.Json
import javax.inject.Inject

interface MoviesAPIService {

    suspend fun allMovies(page: Int): MutableList<Movie>
    suspend fun searchMovie(string: String): MutableList<Movie>
    suspend fun getMovie(id: Int): MutableList<Movie>
    suspend fun getCredits(id: Int): MutableList<Credits>
    suspend fun maxPages(page: Int): Int
    suspend fun getGenres(): GenreList
    companion object {
        const val BASE_URL = ApiConstants.BASE_URL
    }

    sealed class Endpoints(val url: String) {
        object GetAllMovies: Endpoints("$BASE_URL/discover/movie")
        object SearchMovie: Endpoints("$BASE_URL/search/movie")
        object GetMovie: Endpoints("$BASE_URL/movie")
        object GetGenres: Endpoints("$BASE_URL/genre/movie/list")
    }
}

class MoviesApiServiceImpl @Inject constructor(
    private val client: HttpClient,
    private val mapper: MovieRepositoryMapper
) : MoviesAPIService {
    override suspend fun allMovies(page: Int): MutableList<Movie> {
        val response: HttpResponse = client.request(MoviesAPIService.Endpoints.GetAllMovies.url + "?page=$page&year=2023") {
            method = HttpMethod.Get
            headers {
                append("accept", "application/json")
                append("Authorization", ApiConstants.BEARER)
            }
        }
        try {
            val result: ResponseModel = json.decodeFromString(response.body<String>().toString())
            Log.d("serverResult", "All Movies OK")

            return mapper.toDomain(result)
        } catch (cause: Throwable) {
            cause.message?.let {
                Log.d(
                    "serverStatus", "$it : Sync failed: ${response.body<String>()}"
                )
            }
        }
        return ArrayList()
    }
    override suspend fun searchMovie(string: String): MutableList<Movie> {
        val response: HttpResponse = client.request(MoviesAPIService.Endpoints.SearchMovie.url + "?query=$string&year=2023") {
            method = HttpMethod.Get
            headers {
                append("accept", "application/json")
                append("Authorization", ApiConstants.BEARER)
            }
        }
        try {
            val result: ResponseModel = json.decodeFromString(response.body<String>().toString())
            Log.d("serverResult", "Search Movie OK")
            return mapper.toDomain(result)
        } catch (cause: Throwable) {
            cause.message?.let {
                Log.d(
                    "serverStatus", "$it : Sync failed: ${response.body<String>()}"
                )
            }
        }
        return ArrayList()
    }

    override suspend fun getMovie(id: Int): MutableList<Movie> {
        val response: HttpResponse = client.request(MoviesAPIService.Endpoints.GetMovie.url + "/$id") {
            method = HttpMethod.Get
            headers {
                append("accept", "application/json")
                append("Authorization", ApiConstants.BEARER)
            }
        }
        try {
            val result: MovieModel = json.decodeFromString(response.body<String>().toString())
            Log.d("serverResult", "Get Movie OK")

            return mapper.toDomain(result)
        } catch (cause: Throwable) {
            cause.message?.let {
                Log.d(
                    "serverStatus", "$it : Sync failed: ${response.body<String>()}"
                )
            }
        }
        return ArrayList()
    }

    override suspend fun getCredits(id: Int): MutableList<Credits> {
        val response: HttpResponse = client.request(MoviesAPIService.Endpoints.GetMovie.url + "/$id/credits") {
            method = HttpMethod.Get
            headers {
                append("accept", "application/json")
                append("Authorization", ApiConstants.BEARER)
            }
        }
        try {
            val result: CreditsModel = json.decodeFromString(response.body<String>().toString())
            Log.d("serverResult", "Get Credits OK")

            return mapper.toDomain(result)
        } catch (cause: Throwable) {
            cause.message?.let {
                Log.d(
                    "serverStatus", "$it : Sync failed: ${response.body<String>()}"
                )
            }
        }
        return ArrayList()
    }

    override suspend fun maxPages(page: Int): Int {
        val response: HttpResponse = client.request(MoviesAPIService.Endpoints.GetAllMovies.url + "?page=$page&year=2023") {
            method = HttpMethod.Get
            headers {
                append("accept", "application/json")
                append("Authorization", ApiConstants.BEARER)
            }
        }
        try {
            val result: ResponseModel = json.decodeFromString(response.body<String>().toString())

            return result.totalPages
        } catch (cause: Throwable) {
            cause.message?.let {
                Log.d(
                    "serverStatus", "$it : Sync failed: ${response.body<String>()}"
                )
            }
        }
        return 0
    }

    override suspend fun getGenres(): GenreList {
        val response: HttpResponse = client.request(MoviesAPIService.Endpoints.GetGenres.url + "?language=en") {
            method = HttpMethod.Get
            headers {
                append("accept", "application/json")
                append("Authorization", ApiConstants.BEARER)
            }
        }
        try {
            val result: GenreListModel = json.decodeFromString(response.body<String>().toString())

            return mapper.toDomain(result)
        } catch (cause: Throwable) {
            cause.message?.let {
                Log.d(
                    "serverStatus", "$it : Sync failed: ${response.body<String>()}"
                )
            }
        }
        return GenreList()
    }

    private val json: Json
        get() = Json {
            ignoreUnknownKeys = true
            isLenient = true
            allowSpecialFloatingPointValues = true
        }
}