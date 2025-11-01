package com.uniandes.vynilapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.uniandes.vynilapp.ui.theme.VynilappTheme
import com.uniandes.vynilapp.utils.NetworkUtils
import com.uniandes.vynilapp.views.AlbumsScreen
import com.uniandes.vynilapp.views.ArtistsScreen
import com.uniandes.vynilapp.views.CollectionsScreen
import com.uniandes.vynilapp.views.common.BottomNavigationBar
import com.uniandes.vynilapp.views.common.NavigationItem
import com.uniandes.vynilapp.views.common.OfflineErrorScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VynilappTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(NavigationItem.ALBUMS) }
    var isOnline by remember { mutableStateOf(NetworkUtils.isNetworkAvailable(context)) }

    if (!isOnline) {
        OfflineErrorScreen(
            onRetry = {
                isOnline = NetworkUtils.isNetworkAvailable(context)
            }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        ) { paddingValues ->
            when (selectedTab) {
                NavigationItem.ALBUMS -> AlbumsScreen(
                    modifier = Modifier.padding(paddingValues)
                )
                NavigationItem.ARTISTS -> ArtistsScreen(
                    modifier = Modifier.padding(paddingValues)
                )
                NavigationItem.COLLECTIONS -> CollectionsScreen(
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}