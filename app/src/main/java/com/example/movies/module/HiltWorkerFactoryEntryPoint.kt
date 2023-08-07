package com.example.movies.module

import com.example.movies.repository.mapper.MovieRepositoryMapper
import com.example.movies.repository.template.MovieRepository
import com.example.movies.repository.template.UserRepository
import com.example.movies.ui.screen.viewmodel.MovieViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import io.ktor.client.HttpClient

@EntryPoint
@InstallIn(ServiceComponent::class)
interface HiltWorkerFactoryEntryPoint {

    fun clientFactory(): HttpClient

    fun movieRepositoryFactory(): MovieRepository

    fun userRepositoryFactory(): UserRepository

    fun movieRepositoryMapperFactory(): MovieRepositoryMapper
}