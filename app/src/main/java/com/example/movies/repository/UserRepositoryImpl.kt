package com.example.movies.repository

import android.content.SharedPreferences
import com.example.movies.domain.Movie
import com.example.movies.domain.User
import com.example.movies.module.EncryptedSharedPreferencesAnn
import com.example.movies.module.get
import com.example.movies.module.put
import com.example.movies.repository.entity.UserEntity
import com.example.movies.repository.mapper.MovieRepositoryMapper
import com.example.movies.repository.mapper.UserMapper
import com.example.movies.repository.template.UserRepository
import com.example.movies.utils.Constants
import io.realm.kotlin.Realm
import io.realm.kotlin.TypedRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.query.RealmSingleQuery
import java.util.stream.Collectors
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    @EncryptedSharedPreferencesAnn private val sharedPreferences: SharedPreferences,
    private val mapper: UserMapper,
    private val movieMapper: MovieRepositoryMapper,
    private val realm: Realm
) : UserRepository {

    private fun TypedRealm.findByUsernameQuery(username: String): RealmSingleQuery<UserEntity> {
        return this.query<UserEntity>("username == $0", username).first()
    }

    override fun existsByUsername(username: String): Boolean {
        if (getUserByUsername(username) == null) {
            return false
        }
        return true
    }

    override fun update(user: User) {
        realm.writeBlocking {
            val userNew = this.findByUsernameQuery(user.username).find()
            userNew?.userId = user.userId
            userNew?.username = user.username
            userNew?.favorites = user.favorites.stream().map {
                movieMapper.toEntity(it)
            }.collect(Collectors.toList()).toRealmList()
        }
    }

    override fun isConnected(): Boolean {
        if (sharedPreferences.contains(Constants.CONNECTED)){
            return sharedPreferences.get(Constants.CONNECTED) == "true"
        }
        return false
    }

    override fun setConnected(connected: Boolean) {
        sharedPreferences.put(Constants.CONNECTED, connected.toString())
    }

    override fun getUserByUsername(username: String): User? {
        val user =  realm.findByUsernameQuery(username).find()
        if (user != null)
            return mapper.toDomain(user)
        return null
    }

    override fun setUsername(username: String) {
        sharedPreferences.put(Constants.USER_ID, username)
    }

    override fun getUsername(): String? {
        if (sharedPreferences.contains(Constants.USER_ID)){
            return sharedPreferences.get(Constants.USER_ID)
        }
        return null
    }

    override fun create(user: User) {
        realm.writeBlocking {
            val entity = mapper.toEntity(user)
            copyToRealm(entity)
        }
    }

    override fun addMovie(movie: Movie, username: String) {
        realm.writeBlocking {
            val user = this.findByUsernameQuery(username).find()
            user?.favorites?.add(movieMapper.toEntity(movie))
        }
    }

    override fun removeMovie(movieId: Int, username: String) {
        realm.writeBlocking {
            val user = this.findByUsernameQuery(username).find()
            val iterator = user?.favorites?.iterator()
            while (iterator?.hasNext() == true) {
                val movie = iterator.next()
                if (movie.id == movieId) {
                    iterator.remove()
                    break
                }
            }
        }
    }

    override fun movieExists(movieId: Int, username: String): Boolean {
        val user = realm.findByUsernameQuery(username).find()
        for (favourite in user?.favorites!!) {
            if (movieId == favourite.id)
                return true
        }
        return false
    }
    override fun save(obj: User) {
        if (getUsername()?.let { getUserByUsername(it) } == null){
            create(obj)
        }
        else
            update(obj)
    }

}