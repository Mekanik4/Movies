package com.example.movies.module

import com.example.movies.repository.entity.CreditsEntity
import com.example.movies.repository.entity.CrewEntity
import com.example.movies.repository.entity.GenreEntity
import com.example.movies.repository.entity.MovieEntity
import com.example.movies.repository.entity.PersonEntity
import com.example.movies.repository.entity.UserEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.dynamic.DynamicMutableRealmObject
import io.realm.kotlin.dynamic.DynamicRealmObject
import io.realm.kotlin.dynamic.getValue
import io.realm.kotlin.migration.AutomaticSchemaMigration
import javax.inject.Singleton

const val REALM_SCHEMA_VERSION = 2L

@InstallIn(SingletonComponent::class)
@Module
class RealmModule {
    @Singleton
    @Provides
    fun getRealm(): Realm {
        val configuration = RealmConfiguration.Builder(
            schema = setOf(
                UserEntity::class,
                MovieEntity::class,
                CreditsEntity::class,
                PersonEntity::class,
                CrewEntity::class,
                GenreEntity::class
            )
        )
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(REALM_SCHEMA_VERSION)
            .migration(AutomaticSchemaMigration {
                it.enumerate(className = "UserEntity") { oldObject: DynamicRealmObject, newObject: DynamicMutableRealmObject? ->
                    newObject?.run {
                        // Change property type
                        set(
                            "userId",
                            oldObject.getValue<String>(fieldName = "userId")
                        )
//                        // Merge properties
//                        set(
//                            "fullName",
//                            "${oldObject.getValue<String>(fieldName = "firstName")} ${oldObject.getValue<String>(fieldName = "lastName")}"
//                        )
//                        // Rename property
//                        set(
//                            "yearsSinceBirth",
//                            oldObject.getValue<String>(fieldName = "age")
//                        )
                    }
                }
            })
            .build()
        return Realm.open(configuration)
    }
}