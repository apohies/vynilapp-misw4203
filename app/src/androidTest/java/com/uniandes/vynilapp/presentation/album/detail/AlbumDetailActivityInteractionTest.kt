package com.uniandes.vynilapp.presentation.album.detail

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uniandes.vynilapp.test.BaseEspressoTest
import com.uniandes.vynilapp.views.AlbumDetailActivity
import com.uniandes.vynilapp.views.AlbumDetailScreen
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@HiltAndroidTest
class AlbumDetailActivityInteractionTest : BaseEspressoTest() {

    @Test
    fun handleLikeButtonInteractionCorrectly() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.waitForNodeWithText("Buscando América")

        composeTestRule.clickNodeWithContentDescription("Me gusta")

        composeTestRule.assertNodeWithContentDescriptionIsVisible("Me gusta")
    }

    @Test
    fun handleSaveButtonInteractionCorrectly() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.waitForNodeWithText("Buscando América")

        composeTestRule.clickNodeWithContentDescription("Guardar")

        composeTestRule.assertNodeWithContentDescriptionIsVisible("Guardar")
    }

    @Test
    fun handlePlayPauseButtonInteractionCorrectly() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.waitForNodeWithText("Buscando América")

        composeTestRule.clickNodeWithContentDescription("Reproducir")

        composeTestRule.assertNodeWithContentDescriptionIsVisible("Detener")

        composeTestRule.clickNodeWithContentDescription("Detener")

        composeTestRule.assertNodeWithContentDescriptionIsVisible("Reproducir")
    }

    @Test
    fun handleCommentInputCorrectly() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.waitForNodeWithText("Buscando América")

        val commentText = "¡Excelente álbum!"
        composeTestRule.typeTextInNodeWithText("Escribe tu comentario...", commentText)

        composeTestRule.clickNodeWithContentDescription("Enviar comentario")

        composeTestRule.assertNodeContainsText(commentText)
    }

    @Test
    fun handleBackNavigationCorrectly() {
        val composeTestRule = createAndroidComposeRule<AlbumDetailActivity>()
        composeTestRule.setContent {
            AlbumDetailScreen(albumId = 100)
        }

        composeTestRule.clickNodeWithContentDescription("Volver")

        composeTestRule.assertNodeWithContentDescriptionIsVisible("Volver")
    }
}