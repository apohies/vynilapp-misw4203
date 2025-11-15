package com.uniandes.vynilapp.model.repository

import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.model.CollectorAlbum
import com.uniandes.vynilapp.model.services.CollectorServiceAdapter
import javax.inject.Inject

class CollectorRepository @Inject constructor(
    private val collectorServiceAdapter: CollectorServiceAdapter
) {

    suspend fun getAllCollectors(): Result<List<Collector>> {
        return collectorServiceAdapter.getAllCollectors()
    }

    suspend fun getCollectorById(collectorId: Int): Result<Collector> {
        return collectorServiceAdapter.getCollectorById(collectorId)
    }

    suspend fun getCollectorAlbums(collectorId: Int): Result<List<CollectorAlbum>> {
        return collectorServiceAdapter.getCollectorAlbums(collectorId)
    }
}

