package com.uniandes.vynilapp.model.repository

import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.network.RetrofitClient


class AlbumRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getAllAlbums(): List<Album> {
        val response = apiService.getAllAlbums()

        return if (response.isSuccessful && response.body() != null) {
            response.body()!!
        } else {
            throw Exception("Error al obtener álbumes: ${response.code()}")
        }
    }
}