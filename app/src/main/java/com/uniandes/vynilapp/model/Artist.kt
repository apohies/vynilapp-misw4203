package com.uniandes.vynilapp.model

import com.google.gson.annotations.SerializedName

data class Artist(
    val id: Int,
    val name: String,
    val image: String?,
    val description: String,
    @SerializedName("birthDate")
    val birthDate: String,
    val albums: List<ArtistAlbum> = emptyList(),
    val performerPrizes: List<PerformerPrizes> = emptyList()
)

data class ArtistAlbum(
    val id: Int,
    val name: String,
    val cover: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    val description: String,
    val genre: String,
    @SerializedName("recordLabel")
    val recordLabel: String
)

data class PerformerPrizes(
    val id: Int,
    @SerializedName("premiationDate")
    val premiationDate: String,
)
