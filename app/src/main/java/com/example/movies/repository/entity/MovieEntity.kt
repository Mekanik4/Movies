package com.example.movies.repository.entity

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class MovieEntity : RealmObject {
    @PrimaryKey
    var movieId: ObjectId = BsonObjectId()
    var backdropPath: String? = ""
    var budget: Float = 0f
    var genres: RealmList<GenreEntity> = realmListOf()
    var id: Int = 0
    var title: String = ""
    var runtime: Int? = null
    var originalTitle: String = ""
    var originalLanguage: String = ""
    var overview: String = ""
    var posterPath: String? = ""
    var voteAverage: Float = 0f
//    var status: TmdbMovieStatus = TmdbMovieStatus.RELEASED
    var tagline: String = ""
    var credits: CreditsEntity? = CreditsEntity()
}
