package com.uniandes.vynilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uniandes.vynilapp.ui.theme.VynilappTheme
import com.uniandes.vynilapp.components.SearchBar
import com.uniandes.vynilapp.components.AlbumCard

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