package com.uniandes.vynilapp.di

import com.uniandes.vynilapp.data.remote.ApiClient
import com.uniandes.vynilapp.data.remote.ApiService
import com.uniandes.vynilapp.data.remote.AlbumServiceAdapter
import com.uniandes.vynilapp.data.repository.AlbumRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return ApiClient.apiService
    }

    @Provides
    @Singleton
    fun provideAlbumServiceAdapter(apiService: ApiService): AlbumServiceAdapter {
        return AlbumServiceAdapter(apiService)
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(albumServiceAdapter: AlbumServiceAdapter): AlbumRepository {
        return AlbumRepository(albumServiceAdapter)
    }
}
