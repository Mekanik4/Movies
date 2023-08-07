package com.example.movies.repository.entity

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class CreditsEntity : RealmObject {
    @PrimaryKey
    var creditsId: ObjectId = BsonObjectId()
    var movieId: Int = 0
    var cast: RealmList<PersonEntity> = realmListOf()
    var crew: RealmList<CrewEntity> = realmListOf()
}

class PersonEntity : RealmObject {
    @PrimaryKey
    var name: String = ""
    var popularity: Float = 0f
}

class CrewEntity : RealmObject {
    @PrimaryKey
    var name: String = ""
    var job: String = ""
    var department: String = ""
}