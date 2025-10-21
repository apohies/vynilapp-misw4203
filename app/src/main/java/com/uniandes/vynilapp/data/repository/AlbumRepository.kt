package com.uniandes.vynilapp.data.repository
import com.uniandes.vynilapp.data.model.Album
import com.uniandes.vynilapp.data.remote.ApiClient


class AlbumRepository {

    private val apiService = ApiClient.apiService

    suspend fun getAllAlbums(): Result<List<Album>> {
        return try {
            val response = apiService.getAllAlbums()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumById(albumId: Int): Result<Album> {
        return try {
            val response = apiService.getAlbumById(albumId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}