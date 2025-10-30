package com.uniandes.vynilapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    
    /**
     * Formatea una fecha ISO 8601 a formato legible en español
     * Ejemplo: "1984-08-01T00:00:00.000Z" -> "1 de Ago 1984"
     */
    fun formatAlbumDate(dateString: String?): String {
        if (dateString.isNullOrBlank()) return "Fecha no disponible"
        
        return try {
            // Parsear la fecha ISO 8601
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)
            
            if (date != null) {
                // Formatear a formato español
                val outputFormat = SimpleDateFormat("d 'de' MMM yyyy", Locale("es", "ES"))
                outputFormat.format(date)
            } else {
                "Fecha inválida"
            }
        } catch (e: Exception) {
            // Si falla el parsing, intentar con formato más simple
            try {
                val simpleFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = simpleFormat.parse(dateString)
                if (date != null) {
                    val outputFormat = SimpleDateFormat("d 'de' MMM yyyy", Locale("es", "ES"))
                    outputFormat.format(date)
                } else {
                    dateString // Devolver el string original si no se puede parsear
                }
            } catch (e2: Exception) {
                dateString // Devolver el string original si no se puede parsear
            }
        }
    }
    
    /**
     * Formatea solo el año de una fecha ISO 8601
     * Ejemplo: "1984-08-01T00:00:00.000Z" -> "1984"
     */
    fun formatAlbumYear(dateString: String?): String {
        if (dateString.isNullOrBlank()) return "Año no disponible"
        
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(dateString)
            
            if (date != null) {
                val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())
                outputFormat.format(date)
            } else {
                // Intentar extraer solo el año del string
                dateString.substring(0, 4)
            }
        } catch (e: Exception) {
            try {
                dateString.substring(0, 4)
            } catch (e2: Exception) {
                "Año no disponible"
            }
        }
    }
}
