package com.uniandes.vynilapp.model.services

import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Musician
import com.uniandes.vynilapp.model.dto.AlbumDto
import com.uniandes.vynilapp.model.dto.MusicianDto
import com.uniandes.vynilapp.model.network.ApiService

class ArtistServiceAdapter(
    private val apiService: ApiService
) {
    suspend fun getAllArtists(): Result<List<Musician>> {
        return try {
            val response = apiService.getAllMusicians()

            if (response.isSuccessful && response.body() != null) {
                val musiciansDtos = response.body()!!
                val musicians = musiciansDtos.map { musicianDto -> convertToMusician(musicianDto) }
                Result.success(musicians)
            } else if (response.isSuccessful && response.body() == null) {
                Result.failure(
                    Exception("Error obtaining musicians, empty response")
                )
            } else {
                Result.failure(
                    Exception("Error obtaining musicians: ${response.code()} - ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("Network error: ${e.message}")
            )
        }
    }
}

private fun convertToMusician(musicianDto: MusicianDto): Musician {
    return Musician(
        id = musicianDto.id,
        name = musicianDto.name,
        image = musicianDto.image,
        description = musicianDto.description,
        birthDate = musicianDto.birthDate,
        albums = convertAlbums(musicianDto.albums)
    )
}

private fun convertAlbums(albums: List<AlbumDto>): List<Album> {
    val list = mutableListOf<Album>()
    for (album in albums) {
        list.add(convertToAlbum(album))
    }
    return list
}
private fun convertToAlbum(albumDTO: AlbumDto): Album {
    return Album(
        id = albumDTO.id,
        name = albumDTO.name,
        cover = albumDTO.cover,
        releaseDate = albumDTO.releaseDate,
        description = albumDTO.description,
        genre = albumDTO.genre,
        recordLabel = albumDTO.recordLabel
    )
}