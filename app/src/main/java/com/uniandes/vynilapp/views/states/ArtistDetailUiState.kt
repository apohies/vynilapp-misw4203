package com.uniandes.vynilapp.views.states

import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.ArtistAlbum
import com.uniandes.vynilapp.model.PerformerPrizes

data class ArtistDetailUiState(
    val isLoading: Boolean = false,
    val artist: Artist? = null,
    val albums: List<ArtistAlbum> = emptyList(),
    val performerPrizes: List<PerformerPrizes> = emptyList(),
    val error: String? = null,
)

sealed class ArtistDetailEvent {
    object LoadArtist : ArtistDetailEvent()
    data class LoadArtistById(val artistId: Int) : ArtistDetailEvent()
}