package com.example.movies.repository

import android.content.SharedPreferences
import com.example.movies.domain.Credits
import com.example.movies.domain.GenreList
import com.example.movies.domain.Movie
import com.example.movies.module.EncryptedSharedPreferencesAnn
import com.example.movies.repository.mapper.MovieRepositoryMapper
import com.example.movies.repository.template.MovieRepository
import com.example.movies.service.MoviesApiServiceImpl
import io.realm.kotlin.Realm
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val moviesAPIService: MoviesApiServiceImpl,
    @EncryptedSharedPreferencesAnn private val sharedPreferences: SharedPreferences,
    private val mapper: MovieRepositoryMapper,
    private val realm: Realm
) : MovieRepository {

    var _allMovies: MutableList<Movie> = mutableListOf()
    var _searchData: MutableList<Movie> = mutableListOf()
    var _movie: MutableList<Movie> = mutableListOf()
    var _credits: MutableList<Credits> = mutableListOf()
    var _genres: GenreList = GenreList()


    override fun setAllMovies(allMovies: MutableList<Movie>) {
        _allMovies = allMovies
//        Log.d("serverResult", _allMovies.size.toString())
    }

    override suspend fun getAllMovies(page: Int): MutableList<Movie> {
        _allMovies  = moviesAPIService.allMovies(page)
        return _allMovies
    }

    override suspend fun searchMovie(string: String): MutableList<Movie> {
        _searchData  = moviesAPIService.searchMovie(string)
//        Log.d("serverResult", _searchData.size.toString())
        return _searchData
    }

    override suspend fun getMovie(id: Int): MutableList<Movie> {
        _movie  = moviesAPIService.getMovie(id)
        return _movie
    }

    override suspend fun getCredits(id: Int): List<Credits>? {
        _credits = moviesAPIService.getCredits(id)
        return _credits
    }

    override suspend fun getMaxPages(): Int {
        return moviesAPIService.maxPages(1)
    }

    override suspend fun getGenres(): GenreList {
        _genres = moviesAPIService.getGenres()
        return _genres
    }


    companion object {
        const val READY = "READY"
    }
}