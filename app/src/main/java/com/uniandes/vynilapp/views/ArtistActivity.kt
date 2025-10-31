package com.uniandes.vynilapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ArtistSelection(val id: Int, val name: String)

@Composable
fun ArtistsScreen(modifier: Modifier = Modifier) {
    var selectedArtist by remember { mutableStateOf<ArtistSelection?>(null) }

    if (selectedArtist != null) {
        // Show artist detail
        ArtistDetailScreen(
            onBack = { selectedArtist = null },
            modifier = modifier
        )
    } else {
        // Show artist list
        ArtistsListScreen(
            onArtistClick = { artist -> selectedArtist = artist },
            modifier = modifier
        )
    }
}

@Composable
fun ArtistsListScreen(
    onArtistClick: (ArtistSelection) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF111120))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Artists",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Discover your favorite artists",
                color = Color.Gray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Artist buttons
            ArtistButton(
                name = "Rubén Blades",
                onClick = {
                    onArtistClick(ArtistSelection(1, "Rubén Blades"))
                },
                backgroundColor = Color(0xFF6C63FF)
            )

            ArtistButton(
                name = "Queen",
                onClick = {
                    onArtistClick(ArtistSelection(2, "Queen"))
                },
                backgroundColor = Color(0xFF9C27B0)
            )

            ArtistButton(
                name = "The Beatles",
                onClick = {
                    onArtistClick(ArtistSelection(3, "The Beatles"))
                },
                backgroundColor = Color(0xFFFF6B6B)
            )
        }
    }
}

@Composable
fun ArtistButton(
    name: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(250.dp)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = name,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}