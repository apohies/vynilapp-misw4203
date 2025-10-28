package com.uniandes.vynilapp.data.remote

import com.uniandes.vynilapp.data.remote.dto.AlbumDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("albums")
    suspend fun getAllAlbums(): Response<List<AlbumDto>>

    @GET("albums/{albumId}")
    suspend fun getAlbumById(@Path("albumId") albumId: Int): Response<AlbumDto>
}