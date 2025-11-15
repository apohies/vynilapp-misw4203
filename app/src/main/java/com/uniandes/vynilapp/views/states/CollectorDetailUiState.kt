package com.uniandes.vynilapp.views.states

import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.model.CollectorAlbum

data class CollectorDetailUiState(
    val isLoading: Boolean = false,
    val collector: Collector? = null,
    val albums: List<CollectorAlbum> = emptyList(),
    val error: String? = null,
)

sealed class CollectorDetailEvent {
    object LoadCollector : CollectorDetailEvent()
    data class LoadCollectorById(val collectorId: Int) : CollectorDetailEvent()
}