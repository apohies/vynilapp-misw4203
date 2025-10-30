package com.uniandes.vynilapp.utils

object ApiConfig {
    private val currentEnvironment = ApiEnvironments.getCurrentEnvironment()
    
    val PROTOCOL = currentEnvironment.PROTOCOL
    val IP = currentEnvironment.IP
    val PORT = currentEnvironment.PORT
    val BASE_URL = currentEnvironment.BASE_URL
    
    const val ALBUMS_ENDPOINT = "albums"
    const val ALBUM_DETAIL_ENDPOINT = "albums/{id}"
    const val ARTIST_DETAIL_ENDPOINT = "musicians/{id}"
    const val CONNECT_TIMEOUT_SECONDS = 60L
    const val READ_TIMEOUT_SECONDS = 60L
    const val WRITE_TIMEOUT_SECONDS = 60L
    
    fun switchToEnvironment(environment: EnvironmentConfig) {
    }
}
