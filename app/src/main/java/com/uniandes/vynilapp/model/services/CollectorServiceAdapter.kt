package com.uniandes.vynilapp.model.services

import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.model.dto.CollectorDto
import com.uniandes.vynilapp.model.network.ApiService
import retrofit2.Response

class CollectorServiceAdapter(
    private val apiService: ApiService
) {
    
    suspend fun getAllCollectors(): Result<List<Collector>> {
        return try {
            val response = apiService.getAllCollectors()
            
            if (response.isSuccessful && response.body() != null) {
                val collectorDtos = response.body()!!
                val collectors = collectorDtos.map { collectorDto -> convertToCollector(collectorDto) }
                Result.success(collectors)
            } else if (response.isSuccessful && response.body() == null) {
                Result.failure(
                    Exception("Error al obtener coleccionistas: respuesta vacía")
                )
            } else {
                Result.failure(
                    Exception("Error al obtener coleccionistas: ${response.code()} - ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("Error de conexión: ${e.message}")
            )
        }
    }
}

private fun convertToCollector(collectorDto: CollectorDto): Collector {
    val imageUrl = collectorDto.favoritePerformers?.firstOrNull()?.image 
        ?: "https://i.pravatar.cc/150?img=${collectorDto.id}"
    
    val albumCount = collectorDto.collectorAlbums?.size ?: 0
    
    return Collector(
        id = collectorDto.id,
        name = collectorDto.name,
        imageUrl = imageUrl,
        albumCount = albumCount
    )
}

