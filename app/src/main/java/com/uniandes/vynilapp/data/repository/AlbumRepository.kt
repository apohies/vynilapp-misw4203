package com.uniandes.vynilapp.data.repository

import com.uniandes.vynilapp.data.model.Album
import com.uniandes.vynilapp.data.remote.AlbumServiceAdapter

class AlbumRepository(
    private val albumServiceAdapter: AlbumServiceAdapter
) {

    suspend fun getAllAlbums(): Result<List<Album>> {
        return albumServiceAdapter.getAllAlbums()
    }

    suspend fun getAlbumById(albumId: Int): Result<Album> {
        return albumServiceAdapter.getAlbumById(albumId)
    }
}