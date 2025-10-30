package com.uniandes.vynilapp.model.dto

import com.google.gson.annotations.SerializedName

data class ArtistDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("birthDate")
    val birthDate: String,

    @SerializedName("albums")
    val albums: List<ArtistAlbumDto>,

    @SerializedName("performerPrizes")
    val performerPrizes: List<PerformerPrizesDto>
)

data class ArtistAlbumDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("cover")
    val cover: String,

    @SerializedName("releaseDate")
    val releaseDate: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("genre")
    val genre: String,

    @SerializedName("recordLabel")
    val recordLabel: String
)

data class PerformerPrizesDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("premiationDate")
    val premiationDate: String,
)