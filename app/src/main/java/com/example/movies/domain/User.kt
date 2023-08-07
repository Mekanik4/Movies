package com.example.movies.domain

import kotlinx.serialization.Serializable
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

@Serializable
data class User(
    var userId: ObjectId = BsonObjectId(),
    var username: String = "",
    var favorites: List<Movie> = ArrayList()
) {
    companion object {
        fun init(username: String): User {
            return User(
                username = username,
                favorites = listOf()
            )
        }
    }
}