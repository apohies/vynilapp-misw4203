package com.uniandes.vynilapp.presentantion.album.getall

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.views.AlbumsGrid
import com.uniandes.vynilapp.views.ErrorContent
import com.uniandes.vynilapp.views.LoadingContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ActivityInteraction {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingContentDisplayed() {
        composeTestRule.setContent {
            LoadingContent()
        }

        composeTestRule.onNode(hasContentDescription("Loading"))
            .assertExists()
    }

    @Test
    fun testErrorContentDisplayed() {
        composeTestRule.setContent {
            ErrorContent(
                message = "Error de prueba",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Error de prueba").assertExists()
        composeTestRule.onNodeWithText("Reintentar").assertExists()
    }

    @Test
    fun testErrorContentRetryClick() {
        var retryClicked = false

        composeTestRule.setContent {
            ErrorContent(
                message = "Error de prueba",
                onRetry = { retryClicked = true }
            )
        }

        composeTestRule.onNodeWithText("Reintentar").performClick()
        assert(retryClicked) { "Retry button should trigger onRetry" }
    }

    @Test
    fun testAlbumsGridWithEmptyList() {
        composeTestRule.setContent {
            AlbumsGrid(albums = emptyList())
        }

        composeTestRule.onAllNodes(hasClickAction())
            .assertCountEquals(0)
    }

    @Test
    fun testAlbumsGridWithAlbums() {
        val testAlbums = listOf(
            Album(
                id = 1,
                name = "Test Album",
                cover = "https://example.com/cover.jpg",
                releaseDate = "2024-01-01",
                description = "Test Description",
                genre = "Rock",
                recordLabel = "Test Label",
                tracks = emptyList(),
                performers = emptyList(),
                comments = emptyList()
            )
        )

        composeTestRule.setContent {
            AlbumsGrid(albums = testAlbums)
        }

        composeTestRule.onNodeWithText("Test Album").assertExists()
    }
}