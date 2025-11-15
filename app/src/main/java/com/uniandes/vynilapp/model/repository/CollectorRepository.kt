package com.uniandes.vynilapp.model.repository

import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.model.CollectorAlbum
import com.uniandes.vynilapp.model.services.CollectorServiceAdapter
import javax.inject.Inject

open class CollectorRepository @Inject constructor(
    private val collectorServiceAdapter: CollectorServiceAdapter
) {

    open suspend fun getAllCollectors(): Result<List<Collector>> {
        return collectorServiceAdapter.getAllCollectors()
    }

    open suspend fun getCollectorById(collectorId: Int): Result<Collector> {
        return collectorServiceAdapter.getCollectorById(collectorId)
    }

    open suspend fun getCollectorAlbums(collectorId: Int): Result<List<CollectorAlbum>> {
        return collectorServiceAdapter.getCollectorAlbums(collectorId)
    }
}

