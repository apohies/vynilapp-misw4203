package com.uniandes.vynilapp.data.repository

import com.uniandes.vynilapp.data.model.Album
import com.uniandes.vynilapp.data.network.ApiService
import com.uniandes.vynilapp.data.network.RetrofitClient


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