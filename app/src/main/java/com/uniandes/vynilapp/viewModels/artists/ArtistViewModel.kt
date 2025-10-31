package com.uniandes.vynilapp.viewModels.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.repository.ArtistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArtistsViewModel @Inject constructor(
    private val repository: ArtistRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ArtistUiState>(ArtistUiState.Loading)
    val uiState: StateFlow<ArtistUiState> = _uiState.asStateFlow()

    init {
        loadArtists()
    }

    fun loadArtists() {
        viewModelScope.launch {
            _uiState.value = ArtistUiState.Loading

            try {
                val result = repository.getAllArtists()
                result.fold(
                    onSuccess = { artists ->
                        _uiState.value = ArtistUiState.Success(artists)
                    },
                    onFailure = { exception ->
                        _uiState.value = ArtistUiState.Error(exception.message ?: "Error")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = ArtistUiState.Error(e.message ?: "Error")
            }
        }
    }

}
sealed class ArtistUiState {
    object Loading : ArtistUiState()
    data class Success(val artists: List<Artist>) : ArtistUiState()
    data class Error(val message: String) : ArtistUiState()
}