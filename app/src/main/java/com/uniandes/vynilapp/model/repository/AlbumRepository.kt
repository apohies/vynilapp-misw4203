package com.uniandes.vynilapp.model.repository

import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Track
import com.uniandes.vynilapp.model.dto.AddCommentResponse
import com.uniandes.vynilapp.model.dto.AddTrackResponseDto
import com.uniandes.vynilapp.model.services.AlbumServiceAdapter
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val albumServiceAdapter: AlbumServiceAdapter
) {

    suspend fun getAllAlbums(): Result<List<Album>> {
        return albumServiceAdapter.getAllAlbums()
    }

    suspend fun getAlbumById(albumId: Int): Result<Album> {
        return albumServiceAdapter.getAlbumById(albumId)
    }

    suspend fun createAlbum(album: Album): Result<Album> {
        return albumServiceAdapter.createAlbum(album)
    }

    suspend fun AddCommentToAlbum(albumId: Int , description: String, rating: Int , collectorId: Int): Result<AddCommentResponse> {
        return albumServiceAdapter.addCommentToAlbum(albumId, description, rating, collectorId)

    }

    suspend fun addTrackToAlbum(albumId: Int, track: Track): Result<Track> {
        return albumServiceAdapter.addTrackToAlbum(albumId, track)
    }

}