package com.uniandes.vynilapp.data.remote

import com.uniandes.vynilapp.data.model.Album
import com.uniandes.vynilapp.data.model.Comment
import com.uniandes.vynilapp.data.model.Performer
import com.uniandes.vynilapp.data.model.Track
import com.uniandes.vynilapp.data.remote.dto.AlbumDto
import com.uniandes.vynilapp.data.remote.dto.CommentDto
import com.uniandes.vynilapp.data.remote.dto.PerformerDto
import com.uniandes.vynilapp.data.remote.dto.TrackDto
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AlbumServiceAdapter(
    private val apiService: ApiService
) {
    
    suspend fun getAlbumById(albumId: Int): Result<Album> {
        return try {
            val response = apiService.getAlbumById(albumId)
            
            if (response.isSuccessful && response.body() != null) {
                val albumDto = response.body()!!
                val album = albumDto.toDomainModel()
                Result.success(album)
            } else if (response.isSuccessful && response.body() == null) {
                Result.failure(
                    Exception("Error al obtener álbum: respuesta vacía")
                )
            } else {
                Result.failure(
                    Exception("Error al obtener álbum: ${response.code()} - ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("Error de conexión: ${e.message}")
            )
        }
    }
    
    suspend fun getAllAlbums(): Result<List<Album>> {
        return try {
            val response = apiService.getAllAlbums()
            
            if (response.isSuccessful && response.body() != null) {
                val albumDtos = response.body()!!
                val albums = albumDtos.map { it.toDomainModel() }
                Result.success(albums)
            } else if (response.isSuccessful && response.body() == null) {
                Result.failure(
                    Exception("Error al obtener álbumes: respuesta vacía")
                )
            } else {
                Result.failure(
                    Exception("Error al obtener álbumes: ${response.code()} - ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("Error de conexión: ${e.message}")
            )
        }
    }
}

private fun AlbumDto.toDomainModel(): Album {
    return Album(
        id = this.id,
        name = this.name,
        cover = this.cover,
        releaseDate = this.releaseDate,
        description = this.description,
        genre = this.genre,
        recordLabel = this.recordLabel,
        tracks = this.tracks?.map { it.toDomainModel() },
        performers = this.performers?.map { it.toDomainModel() },
        comments = this.comments?.map { it.toDomainModel() }
    )
}

private fun TrackDto.toDomainModel(): Track {
    return Track(
        id = this.id,
        name = this.name,
        duration = this.duration
    )
}

private fun PerformerDto.toDomainModel(): Performer {
    return Performer(
        id = this.id,
        name = this.name,
        image = this.image,
        description = this.description,
        birthDate = this.birthDate
    )
}

private fun CommentDto.toDomainModel(): Comment {
    return Comment(
        id = this.id,
        description = this.description,
        rating = this.rating
    )
}
