package com.example.movies.domain

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    var id: Int = 0,
    var name: String = ""
)

@Serializable
data class GenreList(
    var genres: List<Genre> = ArrayList()
)