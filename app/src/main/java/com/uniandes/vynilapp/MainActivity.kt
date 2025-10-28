package com.uniandes.vynilapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uniandes.vynilapp.ui.theme.VynilappTheme
import com.uniandes.vynilapp.components.SearchBar
import com.uniandes.vynilapp.components.AlbumCard
import com.uniandes.vynilapp.presentation.album.detail.AlbumDetailActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VynilappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    MainContent(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111120))
            .padding(16.dp)
    ) {
        CompSearchBar()

        Spacer(modifier = Modifier.height(16.dp))

        CompAlbumGrid()
        
        Spacer(modifier = Modifier.height(24.dp))
        
        CompNavigationButton()
    }
}

@Composable
fun CompSearchBar(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    SearchBar(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = "Find in albums",
        modifier = modifier
    )
}

@Composable
fun CompAlbumGrid(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AlbumCard(
            albumTitle = "The Sound of Silence",
            artistName = "Simon & Garfunkel",
            imageUrl = "https://picsum.photos/200"
        )

        AlbumCard(
            albumTitle = "Abbey Road",
            artistName = "The Beatles",
            imageUrl = "https://picsum.photos/201"
        )
    }
}

@Composable
fun CompNavigationButton(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    
    Button(
        onClick = {
            val intent = AlbumDetailActivity.createIntent(context, 100)
            context.startActivity(intent)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50)
        )
    ) {
        Text(
            text = "Ver Detalle del Álbum",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}