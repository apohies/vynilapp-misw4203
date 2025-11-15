package com.uniandes.vynilapp.model.network

import com.uniandes.vynilapp.model.dto.AddCommentResponse
import com.uniandes.vynilapp.model.dto.AddCommnetDto
import com.uniandes.vynilapp.model.dto.AlbumDto
import com.uniandes.vynilapp.model.dto.ArtistDto
import com.uniandes.vynilapp.model.dto.CollectorDto
import com.uniandes.vynilapp.utils.ApiConfig
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    // ========== ALBUMS ==========
    @GET(ApiConfig.ALBUMS_ENDPOINT)
    suspend fun getAllAlbums(): Response<List<AlbumDto>>

    @GET(ApiConfig.ALBUM_DETAIL_ENDPOINT)
    suspend fun getAlbumById(@Path("id") albumId: Int): Response<AlbumDto>

    @POST(ApiConfig.ALBUMS_ENDPOINT)
    suspend fun createAlbum(@Body album: AlbumDto): Response<AlbumDto>

    @PUT(ApiConfig.ALBUM_DETAIL_ENDPOINT)
    suspend fun updateAlbum(
        @Path("id") albumId: Int,
        @Body album: AlbumDto
    ): Response<AlbumDto>

    @DELETE(ApiConfig.ALBUM_DETAIL_ENDPOINT)
    suspend fun deleteAlbum(@Path("id") albumId: Int): Response<Unit>

    // ============== ARTISTS ===============
    @GET(ApiConfig.ARTIST_ENDPOINT)
    suspend fun getAllArtists(): Response<List<ArtistDto>>


    @GET(ApiConfig.ARTIST_DETAIL_ENDPOINT)
    suspend fun getArtistById(@Path("id") artistId: Int): Response<ArtistDto>

    // ============== COLLECTORS ===============
    @GET(ApiConfig.COLLECTORS_ENDPOINT)
    suspend fun getAllCollectors(): Response<List<CollectorDto>>


    @POST(ApiConfig.ADD_COMMENT_ENDPOINT)
    suspend fun addCommentToAlbum(
        @Path("albumId") albumId: Int,
        @Body comment: AddCommnetDto
    ): Response<AddCommentResponse>
}