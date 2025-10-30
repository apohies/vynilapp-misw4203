package com.uniandes.vynilapp.utils

object ApiEnvironments {
    
    // Entorno de desarrollo local
    object Development : EnvironmentConfig {
        override val PROTOCOL = "https"
        override val IP = "vynils-backend-miso-23a2a992472e.herokuapp.com"
        override val PORT = ""
        override val BASE_URL = if (PORT.isNotEmpty()) {
            "$PROTOCOL://$IP:$PORT/"
        } else {
            "$PROTOCOL://$IP/"
        }
    }
    
    // Entorno de producción
    object Production : EnvironmentConfig {
        override val PROTOCOL = "https"
        override val IP = "vynils-backend-miso-23a2a992472e.herokuapp.com"
        override val PORT = ""
        override val BASE_URL = if (PORT.isNotEmpty()) {
            "$PROTOCOL://$IP:$PORT/"
        } else {
            "$PROTOCOL://$IP/"
        }
    }
    
    // Entorno de testing
    object Testing : EnvironmentConfig {
        override val PROTOCOL = "https"
        override val IP = "vynils-backend-miso-23a2a992472e.herokuapp.com"
        override val PORT = ""
        override val BASE_URL = if (PORT.isNotEmpty()) {
            "$PROTOCOL://$IP:$PORT/"
        } else {
            "$PROTOCOL://$IP/"
        }
    }
    
    // Función para obtener la configuración actual
    fun getCurrentEnvironment(): EnvironmentConfig {
        // Por defecto usa desarrollo, pero puedes cambiar esto
        return Development
    }
}

interface EnvironmentConfig {
    val PROTOCOL: String
    val IP: String
    val PORT: String
    val BASE_URL: String
}
