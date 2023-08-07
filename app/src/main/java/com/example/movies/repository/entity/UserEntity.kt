package com.example.movies.repository.entity

import com.example.movies.domain.Movie
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class UserEntity : RealmObject {
    @PrimaryKey
    var userId: ObjectId = BsonObjectId()
    var username: String = ""
    var favorites: RealmList<MovieEntity> = realmListOf()
}