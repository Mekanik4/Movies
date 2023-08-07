package com.example.movies.service.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreModel(
    val id: Int,
    val name: String
)

@Serializable
data class GenreListModel(
    val genres: List<GenreModel>
)