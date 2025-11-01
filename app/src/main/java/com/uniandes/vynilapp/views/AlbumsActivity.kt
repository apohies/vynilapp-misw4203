package com.uniandes.vynilapp.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.views.common.AlbumCard
import com.uniandes.vynilapp.views.common.SearchBar
import com.uniandes.vynilapp.viewModels.albums.AlbumsViewModel
import com.uniandes.vynilapp.viewModels.albums.AlbumsUiState
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal para mostrar la lista de álbumes
 */
@AndroidEntryPoint
class AlbumsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AlbumsScreen()
        }
    }
}

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    viewModel: AlbumsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111120))
            .padding(16.dp)
    ) {

        SearchBar(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = "Find in albums"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contenido según el estado
        when (uiState) {
            is AlbumsUiState.Loading -> {
                LoadingContent()
            }

            is AlbumsUiState.Success -> {
                val albums = (uiState as AlbumsUiState.Success).albums

                // Filtrar álbumes según búsqueda
                val filteredAlbums = if (searchText.isBlank()) {
                    albums
                } else {
                    albums.filter {
                        it.name.contains(searchText, ignoreCase = true) ||
                                it.genre.contains(searchText, ignoreCase = true)
                    }
                }

                AlbumsGrid(albums = filteredAlbums)
            }

            is AlbumsUiState.Error -> {
                val message = (uiState as AlbumsUiState.Error).message
                ErrorContent(
                    message = message,
                    onRetry = { viewModel.loadAlbums() }
                )
            }
        }
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun AlbumsGrid(albums: List<Album>) {
    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(albums) { album ->
            AlbumCard(
                albumTitle = album.name,
                artistName = album.performers.firstOrNull()?.name ?: album.recordLabel,
                imageUrl = album.cover,
                onClick = {
                    // Navegar al detalle del álbum
                    val intent = AlbumDetailActivity.createIntent(context, album.id)
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun ErrorContent(
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
            Button(onClick = onRetry) {
                Text("Reintentar")
            }
        }
    }
}
