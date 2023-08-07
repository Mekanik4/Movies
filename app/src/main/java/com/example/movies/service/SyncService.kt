package com.example.movies.service

import android.util.Log
import android.widget.Toast
import com.example.movies.module.HiltWorkerFactoryEntryPoint
import com.example.movies.service.model.MovieModel
import com.example.movies.service.model.ResponseModel
import dagger.hilt.EntryPoints
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.apache.http.conn.ConnectTimeoutException
import java.net.ConnectException
import java.net.NoRouteToHostException

@AndroidEntryPoint
class SyncService : AbstractService() {

    override val serviceName: String
        get() = "SyncService"

    override fun serviceFunction() {
        try {
            runBlocking {
                allMovies()
            }
        } catch (e: ConnectTimeoutException) {
//                Looper.prepare()
            Toast.makeText(applicationContext, "Sync failed for reason: $e", Toast.LENGTH_LONG)
                .show()
            Log.d("serverStatus", "Sync failed for reason: $e")
        } catch (e: ConnectException) {
            Toast.makeText(applicationContext, "Sync failed for reason: $e", Toast.LENGTH_LONG)
                .show()
            Log.d("serverStatus", "Sync failed for reason: $e")
        } catch (e: NoRouteToHostException) {
            Toast.makeText(applicationContext, "Sync failed for reason: $e", Toast.LENGTH_LONG)
                .show()
            Log.d("serverStatus", "Sync failed for reason: $e")
        }
//        catch (e: HttpRequestTimeoutException) {
//            Toast.makeText(applicationContext, "Sync failed for reason: $e", Toast.LENGTH_LONG)
//                .show()
//            Log.d("serverStatus", "Sync failed for reason: $e")
//        }
    }

    companion object {
        const val BASE_URL = ""
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        allowSpecialFloatingPointValues = true
    }

    private suspend fun allMovies() {
        val client = EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).clientFactory()
        val repo = EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).movieRepositoryFactory()
        val mapper = EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).movieRepositoryMapperFactory()
        val response: HttpResponse = client.get("https://api.themoviedb.org/3/discover/movie?year=2023") {
            headers {
                append("accept", "application/json")
                append("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhYTBlZjU5MTBhODIwMzgxZjk4NDc0MTU1ZTg2MjExZCIsInN1YiI6IjY0Yzc4ODhiZWVjNWI1NThlZTY0MTYyMCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.k5BL_VGAyz14nlr8ckTATKv4Pwiyg4RC877FJJNWG58")
            }
        }
        try {
            val result: ResponseModel = json.decodeFromString(response.body<String>().toString())
//            Log.d("serverResult", mapper.toDomain(result).toString())

            repo.setAllMovies(mapper.toDomain(result))
            //repo.setReady(true)
            Log.d("serverResult", result.totalResults.toString())
            Log.d("serverResult", result.totalPages.toString())

        } catch (cause: Throwable) {
            cause.message?.let {
                Log.d(
                    "serverStatus", "$it : Sync failed: ${response.body<String>()}"
                )
            }
        }
    }
}