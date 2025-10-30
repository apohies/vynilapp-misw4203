package com.uniandes.vynilapp.viewModels.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumsUiState>(AlbumsUiState.Loading)
    val uiState: StateFlow<AlbumsUiState> = _uiState.asStateFlow()

    init {
        loadAlbums()
    }

    fun loadAlbums() {
        viewModelScope.launch {
            _uiState.value = AlbumsUiState.Loading

            try {
                val result = repository.getAllAlbums()
                result.fold(
                    onSuccess = { albums ->
                        _uiState.value = AlbumsUiState.Success(albums)
                    },
                    onFailure = { exception ->
                        _uiState.value = AlbumsUiState.Error(exception.message ?: "Error")
                    }
                )
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