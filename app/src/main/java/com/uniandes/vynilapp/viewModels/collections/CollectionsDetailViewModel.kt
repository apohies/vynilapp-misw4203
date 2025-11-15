package com.uniandes.vynilapp.viewModels.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilapp.model.repository.CollectorRepository
import com.uniandes.vynilapp.views.states.CollectorDetailEvent
import com.uniandes.vynilapp.views.states.CollectorDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionsDetailViewModel @Inject constructor(
    private val collectorRepository: CollectorRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(CollectorDetailUiState())
    val uiState: StateFlow<CollectorDetailUiState> = _uiState.asStateFlow()

    // ID's collector (could be as a parameter from the UI)
    private var currentCollectorId: Int = 100 // Default value

    fun onEvent(event: CollectorDetailEvent) {
        when (event) {
            is CollectorDetailEvent.LoadCollector -> loadCollector()
            is CollectorDetailEvent.LoadCollectorById -> loadCollectorById(event.collectorId)
        }
    }


    /**
     * Load an specific artist using the given ID
     */
    fun loadCollectorById(collectorId: Int) {
        currentCollectorId = collectorId
        loadCollector()
    }

    /**
     * Return the current ArtistId
     */
    fun getCurrentCollectorId(): Int = currentCollectorId

    /**
     * Refresh data of the current artist
     */
    fun refreshCollector() {
        loadCollector()
    }

    private fun loadCollector() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val collectorResult = collectorRepository.getCollectorById(currentCollectorId)

                collectorResult.fold(
                    onSuccess = { collector ->
                        val albumsResult = collectorRepository.getCollectorAlbums(currentCollectorId)
                        albumsResult.fold(
                            onSuccess = { collectorAlbums ->
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    collector = collector,
                                    albums = collectorAlbums,
                                    error = null
                                )
                            },
                            onFailure = { exception ->
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    error = "Error al cargar los albums: ${exception.message}"
                                )
                            }
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Error al cargar el coleccionista: ${exception.message}"
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