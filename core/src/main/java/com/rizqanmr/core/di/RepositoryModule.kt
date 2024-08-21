package com.rizqanmr.core.di

import com.rizqanmr.core.data.network.ApiService
import com.rizqanmr.core.data.repository.NewsRepository
import com.rizqanmr.core.data.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(apiService: ApiService) : NewsRepository = NewsRepositoryImpl(apiService)
}