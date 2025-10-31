package com.uniandes.vynilapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.viewModels.artists.ArtistDetailViewModel
import com.uniandes.vynilapp.views.common.ErrorScreen
import com.uniandes.vynilapp.views.common.LoadingScreen
import com.uniandes.vynilapp.views.common.TopBar
import com.uniandes.vynilapp.views.states.ArtistDetailEvent
import com.uniandes.vynilapp.views.states.ArtistDetailUiState
import com.uniandes.vynilapp.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import android.util.Log
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

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
    val artistDetailTitle = stringResource(id = R.string.artist_detail_title)
    // Create a scroll state
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111120))
    ) {
        // Top Bar - Fixed (not scrolling)
        TopBar(artistDetailTitle,onBack)

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Artist Info Card
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ArtistAvatar(uiState.artist)

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

                uiState.artist?.description?.let {
                    Text(
                        text = it,
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
fun ArtistAvatar(
    artist: Artist?
){
    if(artist?.image != null) {
        val imageUrl = artist.image
        Log.d("ArtistDetail", "Loading artist image: $imageUrl")

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = artist.name,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(75.dp)),
            contentScale = ContentScale.Crop,
            loading = {
                // Show loading indicator
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF2A2A3E)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF6C63FF),
                        modifier = Modifier.size(40.dp)
                    )
                }
            },
            error = { error ->
                // Show when image fails to load
                val errorMessage = error.result.throwable.message ?: "Unknown error"
                Log.e("ArtistDetail", "Failed to load image: $imageUrl - Error: $errorMessage", error.result.throwable)

                ArtistAvatarPlaceHolder(artist.name)
            },
            success = { state ->
                Log.i("ArtistDetail", "Successfully loaded image: $imageUrl")
                // Use default rendering when successful
                SubcomposeAsyncImageContent()
            }
        )
    } else {
        Log.w("ArtistDetail", "Artist image URL is null")
        // Artist Image Placeholder
        ArtistAvatarPlaceHolder(null)
    }
}

@Composable
fun ArtistAvatarPlaceHolder(name: String?) {
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
            text = name?.firstOrNull()?.uppercase() ?: "A",
            color = Color.White,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold
        )
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
            image = null,
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