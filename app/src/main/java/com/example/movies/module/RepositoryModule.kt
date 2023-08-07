package com.example.movies.module

import com.example.movies.repository.MovieRepositoryImpl
import com.example.movies.repository.UserRepositoryImpl
import com.example.movies.repository.template.MovieRepository
import com.example.movies.repository.template.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideUserRepository(repository: UserRepositoryImpl) : UserRepository

    @Binds
    @Singleton
    fun provideHandTestSessionRepository(repository: MovieRepositoryImpl) : MovieRepository
}