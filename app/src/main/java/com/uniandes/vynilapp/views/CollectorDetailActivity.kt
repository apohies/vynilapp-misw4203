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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.request.ImageRequest
import com.uniandes.vynilapp.model.Collector
import com.uniandes.vynilapp.model.CollectorAlbum
import com.uniandes.vynilapp.viewModels.collections.CollectionsDetailViewModel
import com.uniandes.vynilapp.views.common.ErrorScreen
import com.uniandes.vynilapp.views.common.LoadingScreen
import com.uniandes.vynilapp.views.common.TopBar
import com.uniandes.vynilapp.views.states.CollectorDetailEvent
import com.uniandes.vynilapp.views.states.CollectorDetailUiState
import com.uniandes.vynilapp.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.utils.DateUtils
import com.uniandes.vynilapp.utils.NetworkUtils
import com.uniandes.vynilapp.views.common.OfflineErrorScreen

@Composable
fun CollectorDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    collectorId: Int = 100,
    viewModel: CollectionsDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(collectorId) {
        viewModel.onEvent(CollectorDetailEvent.LoadCollectorById(collectorId))
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
                    onRetry = { viewModel.onEvent(CollectorDetailEvent.LoadCollectorById(collectorId)) },
                    modifier = modifier
                )
            }
        }
        else -> {
            CollectorDetailContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                modifier = modifier,
                onBack = onBack
            )
        }
    }
}

@Composable
fun CollectorDetailContent(
    uiState: CollectorDetailUiState,
    onEvent: (CollectorDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    val collectorDetailTitle = stringResource(id = R.string.collector_detail_title)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111120))
    ) {
        // Top Bar - Fixed (not scrolling)
        TopBar(collectorDetailTitle, onBack)

        // Scrollable Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Collector Info Card
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CollectorAvatar(uiState.collector)

                Spacer(modifier = Modifier.height(24.dp))

                uiState.collector?.name?.let {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Show album count
                uiState.collector?.albumCount?.let { count ->
                    val description = String.format(stringResource(id = R.string.collector_albums_in_collection), count)
                    Text(
                        text = description,
                        color = Color(0xFFB0B0B0),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.albums.isNotEmpty()) {
                CollectorAlbums(
                    albums = uiState.albums,
                    onAlbumClick = { albumId ->
                        Log.d("CollectorDetail", "Album clicked: $albumId")
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
                var label = stringResource(id = R.string.collector_info_total_albums)
                CollectorInfoRow(label = label, value = "${uiState.albums.size}")

                label = stringResource(id = R.string.collector_info_collection_value)
                val totalValue = uiState.albums.sumOf { it.price }
                CollectorInfoRow(
                    label = label,
                    value = String.format("$%.2f", totalValue)
                )
            }

            // Add bottom padding for better scrolling experience
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CollectorAlbumCard(
    collectorAlbum: CollectorAlbum,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E)
        )
    ) {
        Column {
            // Album Cover Image
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(collectorAlbum.album.cover)
                    .crossfade(true)
                    .build(),
                contentDescription = collectorAlbum.album.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
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
                    text = collectorAlbum.album.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = DateUtils.formatAlbumYear(collectorAlbum.album.releaseDate),
                    color = Color.Gray,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Show price and status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = String.format("$%.0f", collectorAlbum.price),
                        color = Color(0xFF6C63FF),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = collectorAlbum.status,
                        color = when(collectorAlbum.status.lowercase()) {
                            "active" -> Color(0xFF4CAF50)
                            "inactive" -> Color(0xFFFF9800)
                            else -> Color.Gray
                        },
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CollectorAlbums(
    albums: List<CollectorAlbum>,
    onAlbumClick: (Int) -> Unit = {}
) {
    val collectorDetailTitle = stringResource(id = R.string.collector_detail_album_title)
    Column(modifier = Modifier.fillMaxWidth()) {
        // Section Title
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Text(
                text = collectorDetailTitle,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C63FF)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Search",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 4.dp)
                )
                Text(
                    text = "Añadir album",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        // Horizontal Scrolling Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            albums.forEach { collectorAlbum ->
                CollectorAlbumCard(
                    collectorAlbum = collectorAlbum,
                    onClick = { onAlbumClick(collectorAlbum.album.id) }
                )
            }
        }
    }
}

@Composable
fun CollectorAvatar(
    collector: Collector?
) {
    if (collector?.imageUrl != null && collector.imageUrl.isNotEmpty()) {
        val imageUrl = collector.imageUrl
        Log.d("CollectorDetail", "Loading collector image: $imageUrl")

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = collector.name,
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
                Log.e("CollectorDetail", "Failed to load image: $imageUrl - Error: $errorMessage", error.result.throwable)

                CollectorAvatarPlaceHolder(collector.name)
            },
            success = { state ->
                Log.i("CollectorDetail", "Successfully loaded image: $imageUrl")
                // Use default rendering when successful
                SubcomposeAsyncImageContent()
            }
        )
    } else {
        Log.w("CollectorDetail", "Collector image URL is null or empty")
        // Collector Image Placeholder
        CollectorAvatarPlaceHolder(collector?.name)
    }
}

@Composable
fun CollectorAvatarPlaceHolder(name: String?) {
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
            text = name?.firstOrNull()?.uppercase() ?: "C",
            color = Color.White,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CollectorInfoRow(label: String, value: String) {
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
    name = "Collector Detail Content - With Albums",
    showBackground = true,
    backgroundColor = 0xFF111120,
    heightDp = 900
)
@Composable
fun PreviewCollectorDetailWithAlbums() {
    val mockAlbums = listOf(
        CollectorAlbum(
            id = 1,
            price = 35.0,
            status = "Active",
            album = Album(
                id = 100,
                name = "Buscando América",
                cover = "https://i.pravatar.cc/300",
                releaseDate = "1984-08-01T00:00:00.000Z",
                description = "Classic salsa album",
                genre = "Salsa",
                recordLabel = "Elektra"
            )
        ),
        CollectorAlbum(
            id = 2,
            price = 50.0,
            status = "Active",
            album = Album(
                id = 101,
                name = "Siembra",
                cover = "https://i.pravatar.cc/300",
                releaseDate = "1978-09-01T00:00:00.000Z",
                description = "Iconic album",
                genre = "Salsa",
                recordLabel = "Fania"
            )
        ),
        CollectorAlbum(
            id = 3,
            price = 25.0,
            status = "Inactive",
            album = Album(
                id = 102,
                name = "Amor y Control",
                cover = "https://i.pravatar.cc/300",
                releaseDate = "1992-01-01T00:00:00.000Z",
                description = "Great album",
                genre = "Latin",
                recordLabel = "Sony"
            )
        )
    )

    val mockUiState = CollectorDetailUiState(
        collector = Collector(
            id = 1,
            name = "Jaime Andrés",
            imageUrl = "https://i.pravatar.cc/150",
            albumCount = 3
        ),
        albums = mockAlbums,
        isLoading = false,
        error = null
    )

    CollectorDetailContent(
        uiState = mockUiState,
        onEvent = {},
        onBack = {}
    )
}

@Preview(
    name = "Collector Detail - Loading State",
    showBackground = true,
    backgroundColor = 0xFF111120
)
@Composable
fun PreviewCollectorDetailLoading() {
    LoadingScreen()
}

@Preview(
    name = "Collector Detail - Error State",
    showBackground = true,
    backgroundColor = 0xFF111120
)
@Composable
fun PreviewCollectorDetailError() {
    ErrorScreen(
        error = "Error al cargar el coleccionista",
        onRetry = {}
    )
}