package com.uniandes.vynilapp.model
import com.google.gson.annotations.SerializedName
data class Album(
    val id: Int,
    val name: String,
    val cover: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    val description: String,
    val genre: String,
    @SerializedName("recordLabel")
    val recordLabel: String,
    val tracks: List<Track> = emptyList(),
    val performers: List<Performer> = emptyList(),
    val comments: List<Comment> = emptyList()
)

data class Track(
    val id: Int,
    val name: String,
    val duration: String
)

data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    @SerializedName("birthDate")
    val birthDate: String? = null,  // Para músicos
    @SerializedName("creationDate")
    val creationDate: String? = null  // Para bandas
)

data class Comment(
    val id: Int,
    val description: String,
    val rating: Int
)