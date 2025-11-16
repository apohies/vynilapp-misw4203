package com.uniandes.vynilapp.viewModels.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumCreateViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumCreateUiState>(AlbumCreateUiState.Idle)
    val uiState: StateFlow<AlbumCreateUiState> = _uiState.asStateFlow()

    fun createAlbum(album: Album) {
        viewModelScope.launch {
            _uiState.value = AlbumCreateUiState.Loading
            try {
                val result = repository.createAlbum(album)
                result.fold(
                    onSuccess = { created ->
                        _uiState.value = AlbumCreateUiState.Success(created)
                    },
                    onFailure = { ex ->
                        _uiState.value = AlbumCreateUiState.Error(ex.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = AlbumCreateUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _uiState.value = AlbumCreateUiState.Idle
    }
}

sealed class AlbumCreateUiState {
    object Idle : AlbumCreateUiState()
    object Loading : AlbumCreateUiState()
    data class Success(val album: Album) : AlbumCreateUiState()
    data class Error(val message: String) : AlbumCreateUiState()
}