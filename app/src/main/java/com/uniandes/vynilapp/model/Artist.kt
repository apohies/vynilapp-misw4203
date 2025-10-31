package com.uniandes.vynilapp.model

data class Musician (
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String,
    val albums: List<Album>?,
)