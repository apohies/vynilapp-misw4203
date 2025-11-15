package com.uniandes.vynilapp.model.services

import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Comment
import com.uniandes.vynilapp.model.Performer
import com.uniandes.vynilapp.model.Track
import com.uniandes.vynilapp.model.dto.AddCommentResponse
import com.uniandes.vynilapp.model.dto.AddCommnetDto
import com.uniandes.vynilapp.model.dto.AlbumDto
import com.uniandes.vynilapp.model.dto.CommentDto
import com.uniandes.vynilapp.model.dto.PerformerDto
import com.uniandes.vynilapp.model.dto.TrackDto
import com.uniandes.vynilapp.model.network.ApiService
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
                val album = convertToAlbum(albumDto)
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
                val albums = albumDtos.map { albumDto -> convertToAlbum(albumDto) }
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


    suspend fun addCommentToAlbum(albumId: Int , description: String, rating: Int , collectorId: Int): Result<AddCommentResponse> {
        return try {

            val comment = AddCommnetDto(
                description = description,
                rating = rating,
                collector = com.uniandes.vynilapp.model.dto.CollectorE(
                    id = collectorId
                ))



            val response = apiService.addCommentToAlbum(albumId, comment)

            if (response.isSuccessful && response.body() != null) {
                val addCommentResponse = response.body()!!
                Result.success(addCommentResponse)
            } else if (response.isSuccessful && response.body() == null) {
                Result.failure(
                    Exception("Error al agregar comentario")
                )
            } else {
                Result.failure(
                    Exception("Error al agregar comentario: ${response.code()} - ${response.message()}")
                )
            }


        } catch (e: Exception) {
            Result.failure(
                Exception("Error de conexión: ${e.message}")
            )
        }


    }

}




private fun convertToAlbum(albumDto: AlbumDto): Album {
    return Album(
        id = albumDto.id,
        name = albumDto.name,
        cover = albumDto.cover,
        releaseDate = albumDto.releaseDate,
        description = albumDto.description,
        genre = albumDto.genre,
        recordLabel = albumDto.recordLabel,
        tracks = albumDto.tracks?.map { trackDto -> convertToTrack(trackDto) } ?: emptyList(),
        performers = albumDto.performers?.map { performerDto -> convertToPerformer(performerDto) } ?: emptyList(),
        comments = albumDto.comments?.map { commentDto -> convertToComment(commentDto) } ?: emptyList()
    )
}

private fun convertToTrack(trackDto: TrackDto): Track {
    return Track(
        id = trackDto.id,
        name = trackDto.name,
        duration = trackDto.duration
    )
}

private fun convertToPerformer(performerDto: PerformerDto): Performer {
    return Performer(
        id = performerDto.id,
        name = performerDto.name,
        image = performerDto.image,
        description = performerDto.description,
        birthDate = performerDto.birthDate,
        creationDate = performerDto.creationDate
    )
}

private fun convertToComment(commentDto: CommentDto): Comment {
    return Comment(
        id = commentDto.id,
        description = commentDto.description,
        rating = commentDto.rating
    )
}
