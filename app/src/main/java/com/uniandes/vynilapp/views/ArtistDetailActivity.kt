package com.uniandes.vynilapp.views

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.viewModels.artists.ArtistDetailViewModel
import com.uniandes.vynilapp.views.common.ErrorScreen
import com.uniandes.vynilapp.views.common.LoadingScreen
import com.uniandes.vynilapp.views.states.ArtistDetailEvent
import com.uniandes.vynilapp.views.states.ArtistDetailUiState

@Composable
fun ArtistDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    artistId: Int = 100,
    viewModel: ArtistDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(artistId) {
        viewModel.onEvent(ArtistDetailEvent.LoadArtistById(artistId))
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(modifier = modifier)
        }
        uiState.error != null -> {
            val errorMessage = uiState.error ?: "Error desconocido"
            ErrorScreen(
                error = errorMessage,
                onRetry = { viewModel.onEvent(ArtistDetailEvent.LoadArtistById(artistId)) },
                modifier = modifier
            )
        }
        else -> {
            ArtistDetailContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                modifier = modifier,
                onBack = onBack
            )
        }
    }

}

@Composable
fun ArtistDetailContent(
    uiState: ArtistDetailUiState,
    onEvent: (ArtistDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    // Create a scroll state
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111120))
    ) {
        // Top Bar - Fixed (not scrolling)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Artist Details",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Artist Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1A1A2E)
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Artist Image Placeholder
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(
                                Color(0xFF6C63FF),
                                RoundedCornerShape(75.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = uiState.artist?.name?.firstOrNull()?.uppercase() ?: "A",
                            color = Color.White,
                            fontSize = 64.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    uiState.artist?.name?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Biography and additional information will be displayed here. This is a longer text to demonstrate scrolling functionality.",
                        color = Color(0xFFB0B0B0),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Additional Info Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ArtistInfoRow(label = "Genre", value = "Various")
                ArtistInfoRow(label = "Albums", value = "Coming soon")
                ArtistInfoRow(label = "Popularity", value = "★★★★☆")
                ArtistInfoRow(label = "Country", value = "Panama")
                ArtistInfoRow(label = "Active Since", value = "1970")
                ArtistInfoRow(label = "Label", value = "Sony Music")
            }

            // Add bottom padding for better scrolling experience
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ArtistInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFF1A1A2E),
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 16.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(
    name = "Artist Detail Content - With Albums",
    showBackground = true,
    backgroundColor = 0xFF111120,
    heightDp = 900
)
@Composable
fun PreviewArtistDetailWithAlbums() {
    val mockUiState = ArtistDetailUiState(
        artist = Artist(
            id = 104,
            name = "Pink Floyd",
            image = "https://example.com/pinkfloyd.jpg",
            description = "English rock band formed in London in 1965, known for progressive and psychedelic music.",
            birthDate = "1965-01-01T00:00:00.000Z",
            albums = emptyList(),
            performerPrizes = emptyList()
        ),
        albums = emptyList(),
        performerPrizes = emptyList(),
        isLoading = false,
        error = null
    )

    ArtistDetailContent(
        uiState = mockUiState,
        onEvent = {},
        onBack = {}
    )
}