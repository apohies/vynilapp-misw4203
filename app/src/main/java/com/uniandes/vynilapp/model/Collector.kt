package com.uniandes.vynilapp.model

data class Collector(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val albumCount: Int,
)

data class CollectorAlbum(
    val id: Int,
    val price: Double,
    val status: String,
    val album: Album
)