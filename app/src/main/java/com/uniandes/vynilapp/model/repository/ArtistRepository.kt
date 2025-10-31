package com.uniandes.vynilapp.model.repository

import com.uniandes.vynilapp.model.Musician
import com.uniandes.vynilapp.model.services.ArtistServiceAdapter
import javax.inject.Inject

class ArtistRepository @Inject constructor(
    private val artistServiceAdapter: ArtistServiceAdapter
) {

    suspend fun getAllArtists(): Result<List<Musician>> {
        return artistServiceAdapter.getAllArtists();
    }
}