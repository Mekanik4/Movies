package com.example.movies.repository.template

import com.example.movies.domain.Movie
import com.example.movies.domain.User

interface UserRepository : Repository<User> {

    fun save(obj: User)
    fun existsByUsername(username: String): Boolean

    fun update(user: User)

    fun isConnected(): Boolean

    fun setConnected(connected: Boolean)

    fun getUserByUsername(username: String): User?

    fun setUsername(username: String)

    fun getUsername(): String?

    fun create(user: User)

    fun addMovie(movie: Movie, username: String)

    fun removeMovie(movieId: Int, username: String)

    fun movieExists(movieId: Int, username: String): Boolean
}