package com.uniandes.vynilapp.utils

import android.content.Context
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Comment
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheManager @Inject constructor(@ApplicationContext context: Context) {

    private var comments: HashMap<Int, List<Comment>> = hashMapOf()
    private var albums: List<Album>? = null

    fun addComments(albumId: Int, comment: List<Comment>) {
        if (!comments.containsKey(albumId)) {
            comments[albumId] = comment
        }
    }

    fun getComments(albumId: Int): List<Comment> {
        return if (comments.containsKey(albumId)) comments[albumId]!! else listOf<Comment>()
    }

    fun addAlbums(albumsList: List<Album>) {
        albums = albumsList
    }

    fun getAlbums(): List<Album>? {
        return albums
    }

    fun clearAlbums() {
        albums = null
    }
}

