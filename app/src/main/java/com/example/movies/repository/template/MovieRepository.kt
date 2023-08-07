package com.example.movies.repository.template

import com.example.movies.domain.Credits
import com.example.movies.domain.GenreList
import com.example.movies.domain.Movie

interface MovieRepository : Repository<Movie> {


    fun setAllMovies(allMovies: MutableList<Movie>)

    suspend fun getAllMovies(page: Int): List<Movie>?

    suspend fun searchMovie(string: String): List<Movie>?

    suspend fun getMovie(id: Int): List<Movie>?

    suspend fun getCredits(id: Int): List<Credits>?

    suspend fun getMaxPages(): Int

    suspend fun getGenres(): GenreList
}