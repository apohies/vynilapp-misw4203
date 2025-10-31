package com.uniandes.vynilapp.model.services

import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.ArtistAlbum
import com.uniandes.vynilapp.model.PerformerPrizes
import com.uniandes.vynilapp.model.dto.ArtistAlbumDto
import com.uniandes.vynilapp.model.dto.ArtistDto
import com.uniandes.vynilapp.model.dto.PerformerPrizesDto
import com.uniandes.vynilapp.model.network.ApiService

class ArtistServiceAdapter(
    private val apiService: ApiService
) {
    suspend fun getAllArtists(): Result<List<Artist>> {
        return try {
            val response = apiService.getAllArtists()

            if (response.isSuccessful && response.body() != null) {
                val artistsDto = response.body()!!
                val artists = artistsDto.map { artistDto -> convertToArtist(artistDto) }
                Result.success(artists)
            } else if (response.isSuccessful && response.body() == null) {
                Result.failure(
                    Exception("Error obtaining artists, empty response")
                )
            } else {
                Result.failure(
                    Exception("Error obtaining artists: ${response.code()} - ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("Network error: ${e.message}")
            )
        }
    }
    suspend fun getArtistById(artistId: Int): Result<Artist> {
        return try {
            val response = apiService.getArtistById(artistId)

            if (response.isSuccessful && response.body() != null) {
                val artistDto = response.body()!!
                val artist = convertToArtist(artistDto)
                Result.success(artist)
            } else if (response.isSuccessful && response.body() == null) {
                Result.failure(
                    Exception("Error al obtener artista: respuesta vacía")
                )
            } else {
                Result.failure(
                    Exception("Error al obtener artista: ${response.code()} - ${response.message()}")
                )
            }
        }  catch (e: Exception) {
            Result.failure(
                Exception("Error de conexión: ${e.message}")
            )
        }
    }
}

private fun convertToArtist(artistDto: ArtistDto): Artist {
    return Artist(
        id = artistDto.id,
        name = artistDto.name,
        image = artistDto.image,
        description = artistDto.description,
        birthDate = artistDto.birthDate,
        albums = artistDto.albums?.map { artistAlbumDto -> convertToAlbums(artistAlbumDto) } ?: emptyList(),
        performerPrizes = artistDto.performerPrizes?.map { prizesDto -> convertToPrize(prizesDto) } ?: emptyList(),
    )
}

private fun convertToPrize(prizesDto: PerformerPrizesDto): PerformerPrizes {
    return PerformerPrizes(
        id = prizesDto.id,
        premiationDate = prizesDto.premiationDate
    )
}

private fun convertToAlbums(artistAlbumDto: ArtistAlbumDto): ArtistAlbum {
    return ArtistAlbum(
        id = artistAlbumDto.id,
        name = artistAlbumDto.name,
        cover = artistAlbumDto.cover,
        description = artistAlbumDto.description,
        releaseDate = artistAlbumDto.releaseDate,
        genre = artistAlbumDto.genre,
        recordLabel = artistAlbumDto.recordLabel
    )
}
