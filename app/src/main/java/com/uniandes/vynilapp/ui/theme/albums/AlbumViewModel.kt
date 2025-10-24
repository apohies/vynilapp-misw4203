package com.uniandes.vynilapp.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilapp.data.model.Album
import com.uniandes.vynilapp.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumsViewModel : ViewModel() {

    private val repository = AlbumRepository()

    private val _uiState = MutableStateFlow<AlbumsUiState>(AlbumsUiState.Loading)
    val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _uiState.value = AlbumsUiState.Loading

            try {
                val albums = repository.getAllAlbums()
                _uiState.value = AlbumsUiState.Success(albums)
            } catch (e: Exception) {
                _uiState.value = AlbumsUiState.Error(e.message ?: "Error")
            }
        }
    }
}

sealed class AlbumsUiState {
    object Loading : AlbumsUiState()
    data class Success(val albums: List<Album>) : AlbumsUiState()
    data class Error(val message: String) : AlbumsUiState()
}