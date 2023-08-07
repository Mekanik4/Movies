package com.example.movies.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import com.example.movies.domain.Movie
import com.example.movies.domain.User
import com.example.movies.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    val user: User?
        get() {
            return getCurrentUser()?.let { userRepository.getUserByUsername(it) }
        }

    var fromHome: Boolean = true

    fun save(user: User) {
        userRepository.save(user)
    }

    fun isConnected(): Boolean {
        return userRepository.isConnected()
    }

    fun setConnected(connected: Boolean) {
        userRepository.setConnected(connected)
    }

    fun setUsername(username: String) {
        userRepository.setUsername(username)
    }

    fun userExist(username: String): Boolean {
        return userRepository.existsByUsername(username)
    }

    fun addMovie(movie: Movie) {
        getCurrentUser()?.let { userRepository.addMovie(movie, it) }
    }

    fun removeMovie(movieId: Int) {
        getCurrentUser()?.let { userRepository.removeMovie(movieId, it) }
    }

    fun movieExists(movieId: Int): Boolean? {
        return getCurrentUser()?.let { userRepository.movieExists(movieId, it) }
    }
    private fun getCurrentUser(): String? {
        return userRepository.getUsername()
    }
}