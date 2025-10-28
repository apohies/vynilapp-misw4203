package com.uniandes.vynilapp.utils

object ApiConfig {
    // Configuración actual del entorno
    private val currentEnvironment = ApiEnvironments.getCurrentEnvironment()
    
    // Valores de la configuración actual
    val PROTOCOL = currentEnvironment.PROTOCOL
    val IP = currentEnvironment.IP
    val PORT = currentEnvironment.PORT
    val BASE_URL = currentEnvironment.BASE_URL
    
    // Endpoints
    const val ALBUMS_ENDPOINT = "albums"
    const val ALBUM_DETAIL_ENDPOINT = "albums/{id}"
    
    // Timeouts
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L
    
    // Función para cambiar de entorno dinámicamente
    fun switchToEnvironment(environment: EnvironmentConfig) {
        // Esta función se puede usar para cambiar de entorno en runtime
        // Por ahora solo actualiza la configuración estática
    }
}
