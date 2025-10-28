package com.uniandes.vynilapp.presentation.album.detail

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uniandes.vynilapp.test.BaseEspressoTest
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class AlbumDetailActivityStateTest : BaseEspressoTest() {

    @Test
    fun displayLoadingStateInitially() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.assertLoadingState()
    }

    @Test
    fun transitionFromLoadingToContentState() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.waitForNodeWithText("Buscando América")

        composeTestRule.assertContentLoaded()
        composeTestRule.assertNodeContainsText("Buscando América")
        composeTestRule.assertNodeContainsText("Salsa")
        composeTestRule.assertNodeContainsText("Elektra")
    }

    @Test
    fun displayErrorStateWhenAlbumNotFound() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = -1)
        }

        composeTestRule.waitForNodeWithText("Error", timeoutMillis = 10000)

        composeTestRule.assertErrorState()
        composeTestRule.assertNodeWithTextDoesNotExist("Cargando...")
    }

    @Test
    fun displayErrorStateWhenServerErrorOccurs() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 999)
        }

        composeTestRule.waitForNodeWithText("Error", timeoutMillis = 10000)

        composeTestRule.assertErrorState()
    }

    @Test
    fun handleRetryButtonInErrorState() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = -1)
        }

        composeTestRule.waitForNodeWithText("Error", timeoutMillis = 10000)

        composeTestRule.clickNodeWithText("Reintentar")

        composeTestRule.assertLoadingState()
    }

    @Test
    fun displayAlbumWithMinimalDataCorrectly() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 101)
        }

        composeTestRule.waitForNodeWithText("Otro Álbum")

        composeTestRule.assertNodeContainsText("Otro Álbum")
        composeTestRule.assertNodeContainsText("Rock")
        composeTestRule.assertNodeContainsText("Sony")
        
        composeTestRule.assertNodeWithTextDoesNotExist("Canciones")
        composeTestRule.assertNodeWithTextDoesNotExist("Artistas")
        composeTestRule.assertNodeWithTextDoesNotExist("Comentarios")
    }

    @Test
    fun displayAlbumWithCompleteDataCorrectly() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.waitForNodeWithText("Buscando América")

        composeTestRule.assertNodeContainsText("Buscando América")
        composeTestRule.assertNodeContainsText("Salsa")
        composeTestRule.assertNodeContainsText("Elektra")
        composeTestRule.assertNodeContainsText("Canciones")
        composeTestRule.assertNodeContainsText("Decisiones")
        composeTestRule.assertNodeContainsText("Desapariciones")
        composeTestRule.assertNodeContainsText("Artistas")
        composeTestRule.assertNodeContainsText("Rubén Blades")
        composeTestRule.assertNodeContainsText("Comentarios")
        composeTestRule.assertNodeContainsText("Excelente álbum")
    }

    @Test
    fun displayFormattedDateCorrectly() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.waitForNodeWithText("Buscando América")

        composeTestRule.assertNodeContainsText("1984")
        composeTestRule.assertNodeContainsText("1 de ago 1984")
    }

    @Test
    fun handleRapidStateChangesGracefully() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.waitForNodeWithText("Buscando América")
        
        repeat(3) {
            composeTestRule.clickNodeWithContentDescription("Me gusta")
            waitForDelay(100)
            composeTestRule.clickNodeWithContentDescription("Guardar")
            waitForDelay(100)
            composeTestRule.clickNodeWithContentDescription("Reproducir")
            waitForDelay(100)
        }

        composeTestRule.assertNodeContainsText("Buscando América")
        composeTestRule.assertNodeWithContentDescriptionIsVisible("Me gusta")
        composeTestRule.assertNodeWithContentDescriptionIsVisible("Guardar")
        composeTestRule.assertNodeWithContentDescriptionIsVisible("Reproducir")
    }
}