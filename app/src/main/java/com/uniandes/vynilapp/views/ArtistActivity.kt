package com.uniandes.vynilapp.views

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.viewModels.artists.ArtistUiState
import com.uniandes.vynilapp.viewModels.artists.ArtistsViewModel

data class ArtistSelection(val id: Int, val name: String)

@Composable
fun ArtistsScreen(
    modifier: Modifier = Modifier,
    artistViewModel: ArtistsViewModel = hiltViewModel()
) {
    var selectedArtist by remember { mutableStateOf<ArtistSelection?>(null) }

    if(selectedArtist != null){
        ArtistDetailScreen(
            onBack = { selectedArtist = null },
            artistId = selectedArtist!!.id,
            modifier = modifier
        )
    }else{
        val uiState by artistViewModel.uiState.collectAsStateWithLifecycle()
        val query by artistViewModel.searchQuery.collectAsStateWithLifecycle()
        val filtered by artistViewModel.filteredArtists.collectAsStateWithLifecycle()
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF111120))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Contenido según el estado
            when (uiState) {
                is ArtistUiState.Loading -> {
                    LoadingArtists()
                }

                is ArtistUiState.Success -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = { artistViewModel.setSearchQuery(it.trimStart().trimEnd()) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            placeholder = { Text("Search artists") },
                            singleLine = true
                        )

                        ArtistsList(
                            artists = filtered,
                            onArtistClick = { artist: Artist ->
                                selectedArtist = ArtistSelection(artist.id, artist.name)
                            }
                        )
                    }
                }

                is ArtistUiState.Error -> {
                    val message = (uiState as ArtistUiState.Error).message
                    ErrorContent(
                        message = message,
                        onRetry = { artistViewModel.loadArtists() }
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistsList(
    artists: List<Artist>,
    onArtistClick: (Artist) -> Unit
) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(
            artists, key = {artist -> artist.id}
        ) { artist ->
            ArtistItem(
                artist = artist,
                onClick = { onArtistClick(artist) }
            )
        }
    }
}

@Composable
fun ArtistItem(
    artist: Artist,
    onClick: () -> Unit
) {
    val albumCount = try { artist.albums?.size ?: 0 } catch (_: Exception) { 0 }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Circular image
        val imageModifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Gray.copy(alpha = 0.3f), CircleShape)

        if (artist.image?.isNotBlank() == true) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(artist.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "${artist.name} image",
                modifier = imageModifier,
                fallback = painterResource(id = R.drawable.ic_menu_report_image),
                error = painterResource(id = R.drawable.ic_menu_report_image)
            )
        } else {
            // placeholder drawable if no URL
            Image(
                painter = painterResource(id = R.drawable.sym_def_app_icon),
                contentDescription = "placeholder",
                modifier = imageModifier
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = artist.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$albumCount album${if (albumCount == 1) "" else "s"}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
            )
        }
    }

    HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.15f))
}

@Composable
fun LoadingArtists() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}
