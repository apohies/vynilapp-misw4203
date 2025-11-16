package com.uniandes.vynilapp.viewModels.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilapp.model.Comment
import com.uniandes.vynilapp.model.Track
import com.uniandes.vynilapp.model.repository.AlbumRepository
import com.uniandes.vynilapp.views.states.AlbumDetailUiState
import com.uniandes.vynilapp.views.states.AlbumDetailEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumDetailUiState())
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    // ID del álbum actual (se puede pasar desde la UI)
    private var currentAlbumId: Int = 100 // Valor por defecto

    fun onEvent(event: AlbumDetailEvent) {
        when (event) {
            is AlbumDetailEvent.LoadAlbum -> loadAlbum()
            is AlbumDetailEvent.LoadAlbumById -> loadAlbumById(event.albumId)
            is AlbumDetailEvent.PlayAlbum -> playAlbum()
            is AlbumDetailEvent.PauseAlbum -> pauseAlbum()
            is AlbumDetailEvent.ToggleLike -> toggleLike()
            is AlbumDetailEvent.ToggleSave -> toggleSave()
            is AlbumDetailEvent.ShareAlbum -> shareAlbum()
            is AlbumDetailEvent.AddComment -> addComment(event.comment, event.rating)
            is AlbumDetailEvent.UpdateCommentText -> updateCommentText(event.text)
            is AlbumDetailEvent.AddTrack -> addTrack(event.track)
        }
    }
    
    /**
     * Carga un álbum específico por ID
     */
    fun loadAlbumById(albumId: Int) {
        currentAlbumId = albumId
        loadAlbum()
    }
    
    /**
     * Obtiene el ID del álbum actual
     */
    fun getCurrentAlbumId(): Int = currentAlbumId
    
    /**
     * Refresca los datos del álbum actual
     */
    fun refreshAlbum() {
        loadAlbum()
    }

    private fun loadAlbum() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val result = albumRepository.getAlbumById(currentAlbumId)
                
                result.fold(
                    onSuccess = { album ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            album = album,
                            tracks = album.tracks ?: emptyList(),
                            comments = album.comments ?: emptyList(),
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

    private fun playAlbum() {
        _uiState.value = _uiState.value.copy(isPlaying = true)
    }

    private fun pauseAlbum() {
        _uiState.value = _uiState.value.copy(isPlaying = false)
    }

    private fun toggleLike() {
        _uiState.value = _uiState.value.copy(
            isLiked = !_uiState.value.isLiked
        )
    }

    private fun toggleSave() {
        _uiState.value = _uiState.value.copy(
            isSaved = !_uiState.value.isSaved
        )
    }

    private fun shareAlbum() {
        // Implementar lógica de compartir
        // Por ahora solo actualizamos el estado
    }

    private fun addComment(comment: String, rating: Int) {
        if (comment.isNotBlank()) {
            viewModelScope.launch {  // ← Necesitas esto para llamadas suspend
                _uiState.value = _uiState.value.copy(isLoading = true)

                try {
                    val result = albumRepository.AddCommentToAlbum(
                        albumId = currentAlbumId,
                        description = comment.trim(),
                        rating = rating,
                        collectorId = 100
                    )

                    result.fold(
                        onSuccess = { addCommentResponse ->
                            // Convertir AddCommentResponse a Comment
                            val newComment = Comment(
                                id = addCommentResponse.id,
                                description = addCommentResponse.description,
                                rating = addCommentResponse.rating
                            )

                            val currentComments = _uiState.value.comments
                            _uiState.value = _uiState.value.copy(
                                comments = currentComments + newComment,
                                newCommentText = "",
                                isLoading = false,
                                error = null
                            )
                        },
                        onFailure = { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = "Error al agregar comentario: ${exception.message}"
                            )
                        }
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    private fun updateCommentText(text: String) {
        _uiState.value = _uiState.value.copy(newCommentText = text)
    }

    private fun addTrack(track: Track) {
        val currentTracks = _uiState.value.tracks
        val newTrackId = (currentTracks.maxOfOrNull { it.id } ?: 0) + 1
        
        val newTrack = Track(
            id = newTrackId,
            name = track.name.trim(),
            duration = track.duration
        )
        
        _uiState.value = _uiState.value.copy(
            tracks = currentTracks + newTrack
        )
    }
}
