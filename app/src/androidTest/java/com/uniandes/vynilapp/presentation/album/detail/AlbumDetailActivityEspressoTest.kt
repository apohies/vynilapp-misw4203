package com.uniandes.vynilapp.presentation.album.detail

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uniandes.vynilapp.views.AlbumDetailActivity
import com.uniandes.vynilapp.views.AlbumDetailScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class AlbumDetailActivityEspressoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun displayAlbumDetailsWhenActivityLoadsSuccessfully() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.onNodeWithText("Detalle del Álbum")
            .assertIsDisplayed()
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Buscando América")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Salsa")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Elektra")
            .assertIsDisplayed()
    }

    @Test
    fun displayLoadingStateInitially() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.onNodeWithText("Cargando...")
            .assertIsDisplayed()
    }

    @Test
    fun displayErrorStateWhenAlbumLoadingFails() {
        val albumId = -1
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithText("Error")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Error")
            .assertIsDisplayed()
    }

    @Test
    fun toggleLikeButtonWhenClicked() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithContentDescription("Me gusta")
            .performClick()
        
        composeTestRule.onNodeWithContentDescription("Me gusta")
            .assertIsDisplayed()
    }

    @Test
    fun toggleSaveButtonWhenClicked() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithContentDescription("Guardar")
            .performClick()
        
        composeTestRule.onNodeWithContentDescription("Guardar")
            .assertIsDisplayed()
    }

    @Test
    fun togglePlayPauseButtonWhenClicked() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithContentDescription("Reproducir")
            .performClick()
        
        composeTestRule.onNodeWithContentDescription("Detener")
            .assertIsDisplayed()
    }

    @Test
    fun displayAlbumTracksWhenAvailable() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Canciones")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Decisiones")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Desapariciones")
            .assertIsDisplayed()
    }

    @Test
    fun displayAlbumPerformersWhenAvailable() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Artistas")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Rubén Blades")
            .assertIsDisplayed()
    }

    @Test
    fun displayAlbumCommentsWhenAvailable() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Comentarios")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Excelente álbum")
            .assertIsDisplayed()
    }

    @Test
    fun allowAddingNewComment() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Escribe tu comentario...")
            .performTextInput("¡Me encanta este álbum!")
        
        composeTestRule.onNodeWithContentDescription("Enviar comentario")
            .performClick()
        
        composeTestRule.onNodeWithText("¡Me encanta este álbum!")
            .assertIsDisplayed()
    }

    @Test
    fun navigateBackWhenBackButtonIsClicked() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.onNodeWithContentDescription("Volver")
            .performClick()
        
        composeTestRule.onNodeWithContentDescription("Volver")
            .assertIsDisplayed()
    }

    @Test
    fun displayFormattedDateCorrectly() {
        val albumId = 100
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Buscando América")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("1984")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("1 de ago 1984")
            .assertIsDisplayed()
    }

    @Test
    fun handleEmptyAlbumDataGracefully() {
        val albumId = 101
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = albumId)
        }
        
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Otro Álbum")
                .fetchSemanticsNodes().isNotEmpty()
        }
        
        composeTestRule.onNodeWithText("Otro Álbum")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithText("Rock")
            .assertIsDisplayed()
    }
}