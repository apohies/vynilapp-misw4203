package com.uniandes.vynilapp.model.services

import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.model.CollectorAlbum
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.dto.CollectorAlbumsDto
import com.uniandes.vynilapp.model.dto.CollectorDetailAlbumDto
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

    suspend fun getCollectorById(collectorId: Int): Result<Collector> {
        return try {
            val response = apiService.getCollectorById(collectorId)

            if (response.isSuccessful && response.body() != null) {
                val collectorDto = response.body()!!
                val collector = convertToCollector(collectorDto)
                Result.success(collector)
            } else if (response.isSuccessful && response.body() == null) {
                Result.failure(
                    Exception("Error al obtener coleccionista: respuesta vacía")
                )
            } else {
                Result.failure(
                    Exception("Error al obtener coleccionista: ${response.code()} - ${response.message()}")
                )
            }
        }  catch (e: Exception) {
            Result.failure(
                Exception("Error de conexión: ${e.message}")
            )
        }
    }

    suspend fun getCollectorAlbums(collectorId: Int): Result<List<CollectorAlbum>> {
        return try {
            val response = apiService.getAllAlbumsByCollector(collectorId)

            if (response.isSuccessful && response.body() != null) {
                val collectorAlbumsDto = response.body()!!
                val collectorAlbums = collectorAlbumsDto.map { collectorAlbumDto -> convertToCollectorAlbum(collectorAlbumDto) }
                Result.success(collectorAlbums)
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

private fun convertToAlbum(collectorDetailAlbumDto: CollectorDetailAlbumDto): Album {
    return Album(
        id = collectorDetailAlbumDto.id,
        name = collectorDetailAlbumDto.name,
        cover = collectorDetailAlbumDto.cover,
        releaseDate = collectorDetailAlbumDto.releaseDate,
        genre = collectorDetailAlbumDto.genre,
        description = collectorDetailAlbumDto.description,
        recordLabel = collectorDetailAlbumDto.recordLabel
    )
}

private fun convertToCollectorAlbum(collectorAlbumDto: CollectorAlbumsDto): CollectorAlbum {
    return CollectorAlbum(
        id = collectorAlbumDto.id,
        price = collectorAlbumDto.price,
        status = collectorAlbumDto.status,
        album = convertToAlbum(collectorAlbumDto.album)
    )
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
