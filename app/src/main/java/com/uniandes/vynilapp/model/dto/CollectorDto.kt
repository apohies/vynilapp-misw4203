package com.uniandes.vynilapp.model.dto

import com.google.gson.annotations.SerializedName

data class CollectorDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("telephone")
    val telephone: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("comments")
    val comments: List<CommentDto>?,
    
    @SerializedName("favoritePerformers")
    val favoritePerformers: List<PerformerDto>?,
    
    @SerializedName("collectorAlbums")
    val collectorAlbums: List<CollectorAlbumDto>?
)

data class CollectorAlbumDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("price")
    val price: Double,
    
    @SerializedName("status")
    val status: String
)

