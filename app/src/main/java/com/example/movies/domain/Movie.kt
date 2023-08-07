package com.example.movies.domain

import com.example.movies.service.model.TmdbCrewJobType
import kotlinx.serialization.Serializable
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

@Serializable
data class Movie(
    var movieId: ObjectId = BsonObjectId(),
    var backdropPath: String? = "",
    var budget: Long = 0,
    var genres: List<Genre> = ArrayList(),
    var id: Int = 0,
    var title: String = "",
    var runtime: Int? = null,
    var originalTitle: String = "",
    var originalLanguage: String = "",
    var overview: String = "",
    var posterPath: String? = "",
    var voteAverage: Float = 0f,
//    var status: TmdbMovieStatus = TmdbMovieStatus.RELEASED,
    var tagline: String = "",
    var credits: Credits? = Credits(0, ArrayList(), ArrayList())
) {

    fun getDirectors(): List<String> {
        val sortedCrew = credits?.getSortedCrew()
        val directors = ArrayList<String>()
        if (sortedCrew != null) {
            for (i in sortedCrew) {
                if (i.job == TmdbCrewJobType.DIRECTOR) {
                    directors.add(i.name)
                }
            }
        }
        return directors
    }

    fun getCast(): List<String> {
        var cast = credits?.cast
        if (cast?.size!! > 5)
            cast = cast.dropLast(cast.size - 5)
        val castNames = ArrayList<String>()
        for (person in cast)
            castNames.add(person.name)
        return castNames
    }

    fun getGenreNames(): List<String> {
        val genresNames = ArrayList<String>()
        if(genres.isNotEmpty()) {
            for (genre in genres) {
                genresNames.add(genre.name)
            }
        }
        return genresNames
    }
}