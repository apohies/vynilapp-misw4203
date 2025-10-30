package com.uniandes.vynilapp.views.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
) {
    ALBUMS("Albums", Icons.Default.Album, "albums"),
    ARTISTS("Artists", Icons.Default.Person, "artists"),
    COLLECTIONS("Collections", Icons.Default.LibraryMusic, "collections")
}

@Composable
fun BottomNavigationBar(
    selectedTab: NavigationItem,
    onTabSelected: (NavigationItem) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFF1A1A2E),
        contentColor = Color.White
    ) {
        NavigationItem.entries.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = selectedTab == item,
                onClick = { onTabSelected(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF6C63FF),
                    selectedTextColor = Color(0xFF6C63FF),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFF2D2D44)
                )
            )
        }
    }
}