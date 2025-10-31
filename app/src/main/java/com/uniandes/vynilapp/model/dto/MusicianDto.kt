package com.uniandes.vynilapp.model.dto

import com.google.gson.annotations.SerializedName

data class MusicianDto (
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

    @SerializedName(value="albums")
    val albums: List<AlbumDto>
)