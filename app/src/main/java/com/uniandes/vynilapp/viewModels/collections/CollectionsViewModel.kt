package com.uniandes.vynilapp.viewModels.collections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.model.repository.CollectorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val repository: CollectorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CollectionsUiState>(CollectionsUiState.Loading)
    val uiState: StateFlow<CollectionsUiState> = _uiState.asStateFlow()

    init {
        loadCollectors()
    }

    fun loadCollectors() {
        viewModelScope.launch {
            _uiState.value = CollectionsUiState.Loading

            try {
                val result = repository.getAllCollectors()
                result.fold(
                    onSuccess = { collectors ->
                        _uiState.value = CollectionsUiState.Success(collectors)
                    },
                    onFailure = { exception ->
                        _uiState.value = CollectionsUiState.Error(exception.message ?: "Error")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = CollectionsUiState.Error(e.message ?: "Error")
            }
        }
    }
}

sealed class CollectionsUiState {
    object Loading : CollectionsUiState()
    data class Success(val collectors: List<Collector>) : CollectionsUiState()
    data class Error(val message: String) : CollectionsUiState()
}

