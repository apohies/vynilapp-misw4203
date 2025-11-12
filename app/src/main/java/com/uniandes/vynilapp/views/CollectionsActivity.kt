package com.uniandes.vynilapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.viewModels.collections.CollectionsViewModel
import com.uniandes.vynilapp.viewModels.collections.CollectionsUiState
import com.uniandes.vynilapp.views.common.CollectorCard
import com.uniandes.vynilapp.views.common.SearchBar

@Composable
fun CollectionsScreen(
    modifier: Modifier = Modifier,
    viewModel: CollectionsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111120))
            .padding(16.dp)
    ) {
        Text(
            text = "Coleccionista",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        SearchBar(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = "Buscar Coleccionitas"
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is CollectionsUiState.Loading -> {
                CollectionsLoadingContent()
            }

            is CollectionsUiState.Success -> {
                val collectors = (uiState as CollectionsUiState.Success).collectors

                val filteredCollectors = if (searchText.isBlank()) {
                    collectors
                } else {
                    collectors.filter {
                        it.name.contains(searchText, ignoreCase = true)
                    }
                }

                CollectorsList(collectors = filteredCollectors)
            }

            is CollectionsUiState.Error -> {
                val message = (uiState as CollectionsUiState.Error).message
                CollectionsErrorContent(
                    message = message,
                    onRetry = { viewModel.loadCollectors() }
                )
            }
        }
    }
}

@Composable
fun CollectionsLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun CollectorsList(collectors: List<Collector>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        items(collectors) { collector ->
            CollectorCard(
                collectorName = collector.name,
                albumCount = collector.albumCount,
                imageUrl = collector.imageUrl
            )
        }
    }
}

@Composable
fun CollectionsErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = message,
                color = Color.Red
            )
        }
    }
}