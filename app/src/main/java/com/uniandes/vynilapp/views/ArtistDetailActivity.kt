package com.uniandes.vynilapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.text.style.TextOverflow
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
import com.uniandes.vynilapp.utils.DateUtils
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontStyle
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.uniandes.vynilapp.model.ArtistAlbum
import com.uniandes.vynilapp.utils.NetworkUtils
import com.uniandes.vynilapp.views.common.OfflineErrorScreen

@Composable
fun ArtistDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    artistId: Int = 100,
    viewModel: ArtistDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(artistId) {
        viewModel.onEvent(ArtistDetailEvent.LoadArtistById(artistId))
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(modifier = modifier)
        }
        uiState.error != null -> {
            val errorMessage = uiState.error ?: "Error desconocido"
            var isOnline by remember { mutableStateOf(NetworkUtils.isNetworkAvailable(context)) }

            if (!isOnline) {
                OfflineErrorScreen(
                    onRetry = {
                        isOnline = NetworkUtils.isNetworkAvailable(context)
                    }
                )
            } else {
                ErrorScreen(
                    error = errorMessage,
                    onRetry = { viewModel.onEvent(ArtistDetailEvent.LoadArtistById(artistId)) },
                    modifier = modifier
                )
            }
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
    val artistDescriptionTitle = stringResource(id = R.string.artist_description_title)
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

                Text(
                    text = artistDescriptionTitle,
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))

                uiState.artist?.description?.let {
                    Text(
                        text = it,
                        color = Color(0xFFB0B0B0),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.albums.isNotEmpty()) {
                ArtistAlbums(
                    albums = uiState.albums,
                    onAlbumClick = { albumId ->
                        Log.d("ArtistDetail", "Album clicked: $albumId")
                        // TODO: Navigate to album detail
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Additional Info Section
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val popularity = stringResource(id = R.string.artist_additional_info_popularity)
                val birthDate = stringResource(id = R.string.artist_additional_info_birth_date)
                ArtistInfoRow(label = birthDate, value = DateUtils.formatAlbumDate(uiState.artist?.birthDate))
                ArtistInfoRow(label = popularity, value = "★★★★☆")
            }

            // Add bottom padding for better scrolling experience
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AlbumCard(
    album: ArtistAlbum,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF111120)
        )
    ) {
        Column {
            // Album Cover Image
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(album.cover)
                    .crossfade(true)
                    .build(),
                contentDescription = album.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomEnd = 8.dp, bottomStart = 8.dp)),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2A2A3E)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF6C63FF),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2A2A3E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♪",
                            fontSize = 40.sp,
                            color = Color.Gray
                        )
                    }
                }
            )

            // Album Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = album.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = DateUtils.formatAlbumYear(album.releaseDate),
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }
    }
}


@Composable
fun ArtistAlbums(
    albums: List<ArtistAlbum>,
    onAlbumClick: (Int) -> Unit = {}
) {
    if (albums.isEmpty()) {
        // Show empty state
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No albums available",
                color = Color.Gray,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
        }
    } else {
        val artistAlbumsTitle = stringResource(id = R.string.artist_album_title)
        Column(modifier = Modifier.fillMaxWidth()) {
            // Section Title
            Text(
                text = artistAlbumsTitle,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Horizontal Scrolling Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                albums.forEach { album ->
                    AlbumCard(
                        album = album,
                        onClick = { onAlbumClick(album.id) }
                    )
                }
            }
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
    val mockAlbums = listOf(
        ArtistAlbum(
            id = 1,
            name = "Buscando América",
            cover = "https://i.pravatar.cc/300",
            releaseDate = "1984-08-01T00:00:00.000Z",
            description = "Classic salsa album",
            genre = "Salsa",
            recordLabel = "Elektra"
        ),
        ArtistAlbum(
            id = 2,
            name = "Siembra",
            cover = "https://i.pravatar.cc/300",
            releaseDate = "1978-09-01T00:00:00.000Z",
            description = "Iconic album",
            genre = "Salsa",
            recordLabel = "Fania"
        ),
        ArtistAlbum(
            id = 3,
            name = "Amor y Control",
            cover = "https://i.pravatar.cc/300",
            releaseDate = "1992-01-01T00:00:00.000Z",
            description = "Great album",
            genre = "Latin",
            recordLabel = "Sony"
        )
    )

    val mockUiState = ArtistDetailUiState(
        artist = Artist(
            id = 104,
            name = "Pink Floyd",
            image = null,
            description = "English rock band formed in London in 1965, known for progressive and psychedelic music.",
            birthDate = "1965-01-01T00:00:00.000Z",
            albums = mockAlbums,
            performerPrizes = emptyList()
        ),
        albums = mockAlbums,
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