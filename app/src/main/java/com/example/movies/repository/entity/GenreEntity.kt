package com.example.movies.repository.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class GenreEntity : RealmObject {
    @PrimaryKey
    var id: Int = 0
    var name: String = ""
}