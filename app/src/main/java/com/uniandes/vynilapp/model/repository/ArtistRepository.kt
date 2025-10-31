package com.uniandes.vynilapp.model.repository

import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.services.ArtistServiceAdapter
import javax.inject.Inject

class ArtistRepository @Inject constructor(
    private val artistServiceAdapter: ArtistServiceAdapter
) {

    suspend fun getAllArtists(): Result<List<Artist>> {
        return artistServiceAdapter.getAllArtists();
    }

    suspend fun getArtistById(artistId: Int): Result<Artist> {
        return artistServiceAdapter.getArtistById(artistId)
    }
}
