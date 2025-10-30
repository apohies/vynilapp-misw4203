package com.uniandes.vynilapp.viewModels.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilapp.model.repository.ArtistRepository
import com.uniandes.vynilapp.views.states.ArtistDetailEvent
import com.uniandes.vynilapp.views.states.ArtistDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val artistRepository: ArtistRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(ArtistDetailUiState())
    val uiState: StateFlow<ArtistDetailUiState> = _uiState.asStateFlow()

    // ID's artist (could be as a parameter from the UI)
    private var currentArtistId: Int = 100 // Default value

    fun onEvent(event: ArtistDetailEvent) {
        when (event) {
            is ArtistDetailEvent.LoadArtist -> loadArtist()
            is ArtistDetailEvent.LoadArtistById -> loadArtistById(event.artistId)
        }
    }


    /**
     * Load an specific artist using the given ID
     */
    fun loadArtistById(artistId: Int) {
        currentArtistId = artistId
        loadArtist()
    }

    /**
     * Return the current ArtistId
     */
    fun getCurrentArtistId(): Int = currentArtistId

    /**
     * Refresh data of the current artist
     */
    fun refreshArtist() {
        loadArtist()
    }

    private fun loadArtist() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val result = artistRepository.getArtistById(currentArtistId)

                result.fold(
                    onSuccess = { artist ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            artist = artist,
                            performerPrizes = artist.performerPrizes ?: emptyList(),
                            albums = artist.albums ?: emptyList(),
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Error al cargar el álbum: ${exception.message}"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error de conexión: ${e.message}"
                )
            }
        }
    }
}