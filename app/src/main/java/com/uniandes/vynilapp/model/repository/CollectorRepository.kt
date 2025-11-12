package com.uniandes.vynilapp.model.repository

import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.model.services.CollectorServiceAdapter
import javax.inject.Inject

class CollectorRepository @Inject constructor(
    private val collectorServiceAdapter: CollectorServiceAdapter
) {

    suspend fun getAllCollectors(): Result<List<Collector>> {
        return collectorServiceAdapter.getAllCollectors()
    }
}

