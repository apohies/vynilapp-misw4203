package com.uniandes.vynilapp.model.repository

import com.uniandes.vynilapp.model.Album
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
}