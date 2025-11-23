package com.uniandes.vynilapp.model.dto

import com.google.gson.annotations.SerializedName


data class CollectorE(
    @SerializedName("id")
    val id: Int,

)

data class AddCommnetDto(

    @SerializedName("description")
    val description: String,

    @SerializedName("rating")
    val rating: Int,

    @SerializedName("collector")
    val collector: CollectorE
)



data class AddCommentResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("description")
    val description: String,
    @SerializedName("rating")
    val rating: Int,

    @SerializedName("collector")
    val collector: CollectorDto,

    @SerializedName("album")
    val album: AlbumDto,



)
