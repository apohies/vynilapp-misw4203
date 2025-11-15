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

data class CollectorDetailAlbumDto(
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

data class CollectorAlbumsDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("price")
    val price: Double,

    @SerializedName("status")
    val status: String,

    @SerializedName("album")
    val album: CollectorDetailAlbumDto
)


