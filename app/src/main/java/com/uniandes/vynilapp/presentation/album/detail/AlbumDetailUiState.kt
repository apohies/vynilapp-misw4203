package com.uniandes.vynilapp.presentation.album.detail

import com.uniandes.vynilapp.data.model.Album
import com.uniandes.vynilapp.data.model.Comment
import com.uniandes.vynilapp.data.model.Track

data class AlbumDetailUiState(
    val isLoading: Boolean = false,
    val album: Album? = null,
    val tracks: List<Track> = emptyList(),
    val comments: List<Comment> = emptyList(),
    val error: String? = null,
    val isPlaying: Boolean = false,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false,
    val newCommentText: String = ""
)

sealed class AlbumDetailEvent {
    object LoadAlbum : AlbumDetailEvent()
    data class LoadAlbumById(val albumId: Int) : AlbumDetailEvent()
    object PlayAlbum : AlbumDetailEvent()
    object PauseAlbum : AlbumDetailEvent()
    object ToggleLike : AlbumDetailEvent()
    object ToggleSave : AlbumDetailEvent()
    object ShareAlbum : AlbumDetailEvent()
    data class AddComment(val comment: String) : AlbumDetailEvent()
    data class UpdateCommentText(val text: String) : AlbumDetailEvent()
    data class AddTrack(val track: Track) : AlbumDetailEvent()
}
