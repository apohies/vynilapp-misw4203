package com.uniandes.vynilapp.utils

object ApiConfig {
    private val currentEnvironment = ApiEnvironments.getCurrentEnvironment()
    
    val PROTOCOL = currentEnvironment.PROTOCOL
    val IP = currentEnvironment.IP
    val PORT = currentEnvironment.PORT
    val BASE_URL = currentEnvironment.BASE_URL
    
    const val ALBUMS_ENDPOINT = "albums"
    const val ALBUM_DETAIL_ENDPOINT = "albums/{id}"

    const val ARTIST_ENDPOINT = "musicians"
    const val ARTIST_DETAIL_ENDPOINT = "musicians/{id}"
    
    const val COLLECTORS_ENDPOINT = "collectors"

    const val ADD_COMMENT_ENDPOINT = "albums/{albumId}/comments"

    const val COLLECTORS_DETAIL_ENDPOINT = "collectors/{id}"
    const val COLLECTORS_DETAIL_ALBUMS_ENDPOINT = "collectors/{id}/albums"

    const val ADD_TRACK_ENDPOINT = "albums/{albumId}/tracks"

    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L
    
    fun switchToEnvironment(environment: EnvironmentConfig) {
    }
}
