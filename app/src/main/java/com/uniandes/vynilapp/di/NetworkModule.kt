package com.uniandes.vynilapp.di

import com.uniandes.vynilapp.model.network.RetrofitClient
import com.uniandes.vynilapp.model.network.ApiService
import com.uniandes.vynilapp.model.services.AlbumServiceAdapter
import com.uniandes.vynilapp.model.repository.AlbumRepository
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
        return RetrofitClient.apiService
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
