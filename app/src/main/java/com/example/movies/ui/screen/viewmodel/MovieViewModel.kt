package com.example.movies.ui.screen.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies.domain.GenreList
import com.example.movies.domain.Movie
import com.example.movies.repository.MovieRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepositoryImpl
) : ViewModel() {

    private val _movies = MutableLiveData<MutableList<Movie>>()
    val movies: LiveData<MutableList<Movie>>
        get() = _movies

    private val _searchData = MutableLiveData<MutableList<Movie>>()
    val searchData: LiveData<MutableList<Movie>>
        get() = _searchData

    private val _movie = MutableLiveData<MutableList<Movie>>()
    val movie: LiveData<MutableList<Movie>>
        get() = _movie

    var genres: GenreList = GenreList()

    var currentPage = 1
    var maxPage = 2

    private val _currentMovie = MutableStateFlow(Movie())
    private val currentMovie: StateFlow<Movie> = _currentMovie.asStateFlow()

    val isLoading: MutableState<Boolean> = mutableStateOf(true)
    val searching: MutableState<Boolean> = mutableStateOf(true)
    val creditsLoading: MutableState<Boolean> = mutableStateOf(true)
    val genresLoading: MutableState<Boolean> = mutableStateOf(true)

    init{
        getGenres()
        getMaxPages()
        getAllMovies(currentPage)
    }

    fun setCurrentMovie(movie: Movie) {
        _currentMovie.value = movie
        Log.d("serverResult", "movie saved: " + _currentMovie.value.title)
    }
    fun getCurrentMovie(): Movie {
        Log.d("serverResult", "movie returned: " + currentMovie.value.title)
        return currentMovie.value
    }

//    fun getReady(): Boolean {
//        return movieRepository.getReady()
//    }
//
//    fun setReady(ready: Boolean) {
//        movieRepository.setReady(ready)
//    }
    fun getAllMovies(page: Int) {
        searching.value = true
        viewModelScope.launch {
            try {
                _movies.value = movieRepository.getAllMovies(page)
                for (m in _movies.value!!) {
                    for (g in m.genres)
                        g.name = getGenreName(g.id)
                }
                searching.value = false
            }catch (throwable: Throwable){
                withContext(Dispatchers.Main) {
                    searching.value = false
                }
            }
        }
    }

    fun search(string: String) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                _searchData.value = movieRepository.searchMovie(string)
                for (m in _searchData.value!!) {
                    for (g in m.genres)
                        g.name = getGenreName(g.id)
                }
                isLoading.value = false
            }catch (throwable: Throwable){
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                }
            }
        }
    }

    fun getMovie(id: Int) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                _movie.value = movieRepository.getMovie(id)
                setCurrentMovie(_movie.value!![0])
                for (g in _currentMovie.value.genres) {
                    g.name = getGenreName(g.id)
                }
                isLoading.value = false
            }catch (throwable: Throwable){
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                }
            }
        }
    }

    fun getCredits(id: Int) {
        creditsLoading.value = true
        viewModelScope.launch {
            try {
                val credits = movieRepository.getCredits(id)
                if (getCurrentMovie().id == credits?.get(0)?.id) {
                    getCurrentMovie().credits?.crew = credits[0].crew
                    getCurrentMovie().credits?.cast = credits[0].cast
                    Log.d("serverResult", getCurrentMovie().credits?.cast?.size.toString())

                }
                creditsLoading.value = false
            }catch (throwable: Throwable){
                withContext(Dispatchers.Main) {
                    creditsLoading.value = false
                }
            }
        }
    }

    fun getMaxPages() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                maxPage = movieRepository.getMaxPages()
                isLoading.value = false
            }catch (throwable: Throwable){
                withContext(Dispatchers.Main) {
                    isLoading.value = false
                }
            }
        }
    }

    fun getGenres() {
        genresLoading.value = true
        viewModelScope.launch {
            try {
                genres = movieRepository.getGenres()
                genresLoading.value = false
            }catch (throwable: Throwable){
                withContext(Dispatchers.Main) {
                    genresLoading.value = false
                }
            }
        }
    }

    private fun getGenreName(id: Int): String {
        for (genre in genres.genres) {
            if (genre.id == id)
                return genre.name
        }
        return ""
    }
}