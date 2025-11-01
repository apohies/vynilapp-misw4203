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
class ActivityState {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingState() {
        composeTestRule.setContent {
            LoadingContent()
        }


        composeTestRule.onNode(
            hasContentDescription("Loading") or hasTestTag("loading_indicator")
        ).assertExists()
    }

    @Test
    fun testSuccessStateWithAlbums() {
        val testAlbums = listOf(
            Album(
                id = 1,
                name = "Album 1",
                cover = "https://example.com/cover1.jpg",
                releaseDate = "2024-01-01",
                description = "Description 1",
                genre = "Rock",
                recordLabel = "Label 1",
                tracks = emptyList(),
                performers = emptyList(),
                comments = emptyList()
            ),
            Album(
                id = 2,
                name = "Album 2",
                cover = "https://example.com/cover2.jpg",
                releaseDate = "2024-02-01",
                description = "Description 2",
                genre = "Pop",
                recordLabel = "Label 2",
                tracks = emptyList(),
                performers = emptyList(),
                comments = emptyList()
            )
        )

        composeTestRule.setContent {
            AlbumsGrid(albums = testAlbums)
        }


        composeTestRule.onNodeWithText("Album 1").assertExists()
        composeTestRule.onNodeWithText("Album 2").assertExists()
    }

    @Test
    fun testSuccessStateWithEmptyList() {
        composeTestRule.setContent {
            AlbumsGrid(albums = emptyList())
        }


        composeTestRule.onAllNodes(hasClickAction())
            .assertCountEquals(0)
    }

    @Test
    fun testErrorState() {
        val errorMessage = "Error al cargar álbumes"

        composeTestRule.setContent {
            ErrorContent(
                message = errorMessage,
                onRetry = {}
            )
        }


        composeTestRule.onNodeWithText(errorMessage).assertExists()


        composeTestRule.onNodeWithText("Reintentar").assertExists()
    }

    @Test
    fun testErrorStateRetryAction() {
        var retryCount = 0

        composeTestRule.setContent {
            ErrorContent(
                message = "Error de conexión",
                onRetry = { retryCount++ }
            )
        }


        composeTestRule.onNodeWithText("Reintentar").performClick()


        assert(retryCount == 1) { "Retry should be called once" }
    }

    @Test
    fun testStateTransitionFromLoadingToSuccess() {
        var showLoading = true

        composeTestRule.setContent {
            if (showLoading) {
                LoadingContent()
            } else {
                AlbumsGrid(albums = emptyList())
            }
        }

        // Verificar estado de carga
        composeTestRule.onNode(
            hasContentDescription("Loading") or hasTestTag("loading_indicator")
        ).assertExists()


        showLoading = false
        composeTestRule.waitForIdle()
    }

    @Test
    fun testMultipleAlbumsDisplayedInGrid() {
        val albums = (1..6).map { index ->
            Album(
                id = index,
                name = "Album $index",
                cover = "https://example.com/cover$index.jpg",
                releaseDate = "2024-0$index-01",
                description = "Description $index",
                genre = "Genre $index",
                recordLabel = "Label $index",
                tracks = emptyList(),
                performers = emptyList(),
                comments = emptyList()
            )
        }

        composeTestRule.setContent {
            AlbumsGrid(albums = albums)
        }

        // Verificar que se muestran múltiples álbumes
        composeTestRule.onAllNodes(hasClickAction())
            .assertCountEquals(6)
    }
}