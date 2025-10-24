package com.uniandes.vynilapp.data.network

import com.uniandes.vynilapp.data.model.Album
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========== ALBUMS ==========
    @GET("albums")
    suspend fun getAllAlbums(): Response<List<Album>>

    @GET("albums/{albumId}")
    suspend fun getAlbumById(@Path("albumId") albumId: Int): Response<Album>

    @POST("albums")
    suspend fun createAlbum(@Body album: Album): Response<Album>

    @PUT("albums/{albumId}")
    suspend fun updateAlbum(
        @Path("albumId") albumId: Int,
        @Body album: Album
    ): Response<Album>

    @DELETE("albums/{albumId}")
    suspend fun deleteAlbum(@Path("albumId") albumId: Int): Response<Unit>
}