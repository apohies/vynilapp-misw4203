package com.uniandes.vynilapp.model.network

import com.uniandes.vynilapp.model.Album
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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