package com.example.movies.repository.mapper

import com.example.movies.domain.User
import com.example.movies.repository.MovieRepositoryImpl
import com.example.movies.repository.entity.UserEntity
import io.realm.kotlin.ext.toRealmList
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor(
    private val movieRepository: MovieRepositoryImpl,
    private val movieMapper: MovieRepositoryMapper
) {
    fun toEntity(user: User): UserEntity {
        return UserEntity().apply {
            userId = user.userId
            username = user.username
            favorites = user.favorites.stream().map {
                movieMapper.toEntity(it)
            }.collect(Collectors.toList()).toRealmList()
        }
    }

    fun toDomainNoMovies(userEntity: UserEntity): User {
        return User().apply {
            userId = userEntity.userId
            username = userEntity.username
            favorites = listOf()
        }
    }

    fun toDomain(userEntity: UserEntity): User {
        return User().apply {
            userId = userEntity.userId
            username = userEntity.username
            favorites = userEntity.favorites.stream().map {
                movieMapper.toDomain(it)
            }.collect(Collectors.toList())
        }
    }
}