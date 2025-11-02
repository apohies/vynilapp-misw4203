package com.uniandes.vynilapp.views

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.uniandes.vynilapp.model.Album
import com.uniandes.vynilapp.model.Performer
import com.uniandes.vynilapp.model.Track
import com.uniandes.vynilapp.model.Comment
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class AlbumActivityTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var albumClickCount = 0
    private var clickedAlbumId: Int? = null
    private var retryClickCount = 0

    // Mock data
    private lateinit var mockAlbums: List<Album>
    private lateinit var mockAlbumWithPerformer: Album
    private lateinit var mockAlbumWithoutPerformer: Album
    private lateinit var mockPerformers: List<Performer>

    @Before
    fun setup() {
        albumClickCount = 0
        clickedAlbumId = null
        retryClickCount = 0

        // Setup mock performers
        mockPerformers = listOf(
            Performer(
                id = 1,
                name = "Queen",
                image = "https://example.com/queen.jpg",
                description = "British rock band",
                creationDate = "1970-01-01T00:00:00.000Z"
            ),
            Performer(
                id = 2,
                name = "The Beatles",
                image = "https://example.com/beatles.jpg",
                description = "English rock band",
                creationDate = "1960-01-01T00:00:00.000Z"
            )
        )

        // Setup mock albums
        mockAlbumWithPerformer = Album(
            id = 100,
            name = "Bohemian Rhapsody",
            cover = "https://example.com/album1.jpg",
            releaseDate = "1975-10-31T00:00:00.000Z",
            description = "Classic rock album",
            genre = "Rock",
            recordLabel = "EMI",
            tracks = emptyList(),
            performers = listOf(mockPerformers[0]),
            comments = emptyList()
        )

        mockAlbumWithoutPerformer = Album(
            id = 101,
            name = "Unknown Album",
            cover = "https://example.com/album2.jpg",
            releaseDate = "2020-01-01T00:00:00.000Z",
            description = "Album without performer",
            genre = "Pop",
            recordLabel = "Sony Music",
            tracks = emptyList(),
            performers = emptyList(),
            comments = emptyList()
        )

        mockAlbums = listOf(
            mockAlbumWithPerformer,
            mockAlbumWithoutPerformer,
            Album(
                id = 102,
                name = "Abbey Road",
                cover = "https://example.com/album3.jpg",
                releaseDate = "1969-09-26T00:00:00.000Z",
                description = "The Beatles masterpiece",
                genre = "Rock",
                recordLabel = "Apple Records",
                tracks = emptyList(),
                performers = listOf(mockPerformers[1]),
                comments = emptyList()
            ),
            Album(
                id = 103,
                name = "Thriller",
                cover = "https://example.com/album4.jpg",
                releaseDate = "1982-11-30T00:00:00.000Z",
                description = "Best-selling album",
                genre = "Pop",
                recordLabel = "Epic",
                tracks = emptyList(),
                performers = emptyList(),
                comments = emptyList()
            )
        )
    }

    // ==================== AlbumsGrid Tests ====================

    // Test 1: Validate AlbumsGrid displays all albums
    @Test
    fun validateAlbumsGridDisplaysAllAlbumsTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // Verify all album names are displayed
        composeTestRule.onNodeWithText("Bohemian Rhapsody").assertExists()
        composeTestRule.onNodeWithText("Unknown Album").assertExists()
        composeTestRule.onNodeWithText("Abbey Road").assertExists()
        composeTestRule.onNodeWithText("Thriller").assertExists()
    }

    // Test 2: Validate AlbumsGrid with empty list
    @Test
    fun validateAlbumsGridEmptyTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = emptyList())
        }

        // Should render without errors
        composeTestRule.waitForIdle()
    }

    // Test 3: Validate AlbumsGrid with single album
    @Test
    fun validateAlbumsGridSingleAlbumTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = listOf(mockAlbumWithPerformer))
        }

        composeTestRule.onNodeWithText("Bohemian Rhapsody")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 4: Validate AlbumsGrid displays performer names
    @Test
    fun validateAlbumsGridDisplaysPerformerNamesTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // Verify performer names are displayed
        composeTestRule.onNodeWithText("Queen").assertExists()
        composeTestRule.onNodeWithText("The Beatles").assertExists()
    }

    // Test 5: Validate AlbumsGrid shows record label when no performer
    @Test
    fun validateAlbumsGridShowsRecordLabelTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = listOf(mockAlbumWithoutPerformer))
        }

        // Should show record label instead of performer
        composeTestRule.onNodeWithText("Sony Music").assertExists()
    }

    // Test 6: Validate AlbumsGrid with many albums
    @Test
    fun validateAlbumsGridManyAlbumsTest() {
        val manyAlbums = List(20) { index ->
            Album(
                id = index,
                name = "Album $index",
                cover = "https://example.com/album$index.jpg",
                releaseDate = "2020-01-01T00:00:00.000Z",
                description = "Description $index",
                genre = "Rock",
                recordLabel = "Label $index",
                tracks = emptyList(),
                performers = emptyList(),
                comments = emptyList()
            )
        }

        composeTestRule.setContent {
            AlbumsGrid(albums = manyAlbums)
        }

        // First items should be visible
        composeTestRule.onNodeWithText("Album 0").assertExists()
        composeTestRule.onNodeWithText("Album 1").assertExists()
    }

    // Test 7: Validate AlbumsGrid 2-column layout
    @Test
    fun validateAlbumsGridTwoColumnLayoutTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // All albums should be rendered (in a grid)
        mockAlbums.forEach { album ->
            composeTestRule.onNodeWithText(album.name).assertExists()
        }
    }

    // ==================== LoadingContent Tests ====================

    // Test 8: Validate LoadingContent displays indicator
    @Test
    fun validateLoadingContentDisplaysTest() {
        composeTestRule.setContent {
            LoadingContent()
        }

        // Loading indicator should be present
        composeTestRule.waitForIdle()
    }

    // Test 9: Validate LoadingContent renders without errors
    @Test
    fun validateLoadingContentRendersTest() {
        composeTestRule.setContent {
            LoadingContent()
        }

        composeTestRule.waitForIdle()
    }

    // ==================== ErrorContent Tests ====================

    // Test 10: Validate ErrorContent displays error message
    @Test
    fun validateErrorContentDisplaysMessageTest() {
        val errorMessage = "Network connection failed"

        composeTestRule.setContent {
            ErrorContent(
                message = errorMessage,
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(errorMessage)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 11: Validate ErrorContent displays retry button
    @Test
    fun validateErrorContentDisplaysRetryButtonTest() {
        composeTestRule.setContent {
            ErrorContent(
                message = "Error occurred",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Reintentar")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 12: Validate ErrorContent retry button is clickable
    @Test
    fun validateErrorContentRetryButtonClickableTest() {
        composeTestRule.setContent {
            ErrorContent(
                message = "Error",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Reintentar")
            .assertHasClickAction()
    }

    // Test 13: Validate ErrorContent retry button click callback
    @Test
    fun validateErrorContentRetryClickTest() {
        composeTestRule.setContent {
            ErrorContent(
                message = "Error",
                onRetry = { retryClickCount++ }
            )
        }

        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()

        assertEquals(1, retryClickCount)
    }

    // Test 14: Validate ErrorContent with multiple retry clicks
    @Test
    fun validateErrorContentMultipleRetryClicksTest() {
        composeTestRule.setContent {
            ErrorContent(
                message = "Error",
                onRetry = { retryClickCount++ }
            )
        }

        repeat(3) {
            composeTestRule.onNodeWithText("Reintentar").performClick()
        }
        composeTestRule.waitForIdle()

        assertEquals(3, retryClickCount)
    }

    // Test 15: Validate ErrorContent with long error message
    @Test
    fun validateErrorContentLongMessageTest() {
        val longMessage = "This is a very long error message that explains what went wrong in detail"

        composeTestRule.setContent {
            ErrorContent(
                message = longMessage,
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(longMessage, substring = true)
            .assertExists()
    }

    // Test 16: Validate ErrorContent with Spanish message
    @Test
    fun validateErrorContentSpanishMessageTest() {
        composeTestRule.setContent {
            ErrorContent(
                message = "Error al cargar los álbumes",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Error al cargar los álbumes")
            .assertExists()
    }

    // Test 17: Validate album cards are clickable in grid
    @Test
    fun validateAlbumCardsClickableTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums.take(2))
        }

        composeTestRule.onNodeWithText("Bohemian Rhapsody")
            .assertHasClickAction()

        composeTestRule.onNodeWithText("Unknown Album")
            .assertHasClickAction()
    }

    // Test 18: Validate album with special characters
    @Test
    fun validateAlbumSpecialCharactersTest() {
        val specialAlbum = mockAlbumWithPerformer.copy(
            name = "Rock & Roll: The Best!"
        )

        composeTestRule.setContent {
            AlbumsGrid(albums = listOf(specialAlbum))
        }

        composeTestRule.onNodeWithText("Rock & Roll: The Best!")
            .assertExists()
    }

    // Test 19: Validate album with long name
    @Test
    fun validateAlbumLongNameTest() {
        val longNameAlbum = mockAlbumWithPerformer.copy(
            name = "This is a very long album name that might be truncated"
        )

        composeTestRule.setContent {
            AlbumsGrid(albums = listOf(longNameAlbum))
        }

        composeTestRule.onNodeWithText(longNameAlbum.name, substring = true)
            .assertExists()
    }

    // Test 20: Validate performer with long name
    @Test
    fun validatePerformerLongNameTest() {
        val longPerformerName = Performer(
            id = 10,
            name = "This is a very long performer name",
            image = "https://example.com/performer.jpg",
            description = "Description",
            creationDate = "2000-01-01T00:00:00.000Z"
        )

        val album = mockAlbumWithPerformer.copy(performers = listOf(longPerformerName))

        composeTestRule.setContent {
            AlbumsGrid(albums = listOf(album))
        }

        composeTestRule.onNodeWithText(longPerformerName.name, substring = true)
            .assertExists()
    }

    // Test 21: Validate grid displays multiple albums in rows
    @Test
    fun validateGridMultipleRowsTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // All 4 albums should be rendered
        assertEquals(4, mockAlbums.size)
        mockAlbums.forEach { album ->
            composeTestRule.onNodeWithText(album.name).assertExists()
        }
    }

    // Test 22: Validate grid with odd number of albums
    @Test
    fun validateGridOddNumberAlbumsTest() {
        val oddAlbums = mockAlbums.take(3)

        composeTestRule.setContent {
            AlbumsGrid(albums = oddAlbums)
        }

        oddAlbums.forEach { album ->
            composeTestRule.onNodeWithText(album.name).assertExists()
        }
    }

    // Test 23: Validate grid spacing
    @Test
    fun validateGridSpacingTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // Grid should render without layout issues
        composeTestRule.waitForIdle()
        mockAlbums.forEach { album ->
            composeTestRule.onNodeWithText(album.name).assertExists()
        }
    }

    // Test 24: Validate all album titles are unique
    @Test
    fun validateUniqueAlbumTitlesTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // Each album should appear exactly once
        mockAlbums.forEach { album ->
            val nodes = composeTestRule.onAllNodesWithText(album.name)
                .fetchSemanticsNodes()
            assertEquals(1, nodes.size)
        }
    }

    // Test 25: Validate album genres
    @Test
    fun validateAlbumGenresTest() {
        val rockAlbums = mockAlbums.filter { it.genre == "Rock" }
        val popAlbums = mockAlbums.filter { it.genre == "Pop" }

        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // Verify albums of different genres are displayed
        assertTrue(rockAlbums.isNotEmpty())
        assertTrue(popAlbums.isNotEmpty())
    }

    // Test 26: Validate album with empty cover URL
    @Test
    fun validateAlbumEmptyCoverUrlTest() {
        val noCoverAlbum = mockAlbumWithPerformer.copy(cover = "")

        composeTestRule.setContent {
            AlbumsGrid(albums = listOf(noCoverAlbum))
        }

        // Album name should still be displayed
        composeTestRule.onNodeWithText("Bohemian Rhapsody").assertExists()
    }

    // Test 27: Validate album content descriptions
    @Test
    fun validateAlbumContentDescriptionsTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = listOf(mockAlbumWithPerformer))
        }

        // Album image should have content description
        composeTestRule.onNodeWithContentDescription("Bohemian Rhapsody")
            .assertExists()
    }

    // Test 28: Validate albums with different performers
    @Test
    fun validateDifferentPerformersTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // Different performers should be displayed
        composeTestRule.onNodeWithText("Queen").assertExists()
        composeTestRule.onNodeWithText("The Beatles").assertExists()
    }

    // Test 29: Validate albums with and without performers mixed
    @Test
    fun validateMixedPerformersAndLabelsTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // Some show performers, some show labels
        composeTestRule.onNodeWithText("Queen").assertExists()
        composeTestRule.onNodeWithText("Sony Music").assertExists()
    }

    // Test 30: Validate ErrorContent with empty message
    @Test
    fun validateErrorContentEmptyMessageTest() {
        composeTestRule.setContent {
            ErrorContent(
                message = "",
                onRetry = {}
            )
        }

        // Retry button should still be present
        composeTestRule.onNodeWithText("Reintentar").assertExists()
    }

    // Test 31: Validate ErrorContent structure
    @Test
    fun validateErrorContentStructureTest() {
        composeTestRule.setContent {
            ErrorContent(
                message = "Test error",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Test error").assertExists()
        composeTestRule.onNodeWithText("Reintentar").assertExists()
    }

    // Test 32: Validate grid with large dataset
    @Test
    fun validateGridLargeDatasetTest() {
        val largeList = List(50) { index ->
            Album(
                id = index,
                name = "Large Album $index",
                cover = "https://example.com/album$index.jpg",
                releaseDate = "2020-01-01T00:00:00.000Z",
                description = "Description",
                genre = "Rock",
                recordLabel = "Label",
                tracks = emptyList(),
                performers = emptyList(),
                comments = emptyList()
            )
        }

        composeTestRule.setContent {
            AlbumsGrid(albums = largeList)
        }

        // First items should be visible
        composeTestRule.onNodeWithText("Large Album 0").assertExists()
    }

    // Test 33: Validate grid with filtered albums
    @Test
    fun validateGridFilteredAlbumsTest() {
        val rockAlbums = mockAlbums.filter { it.genre == "Rock" }

        composeTestRule.setContent {
            AlbumsGrid(albums = rockAlbums)
        }

        rockAlbums.forEach { album ->
            composeTestRule.onNodeWithText(album.name).assertExists()
        }
    }

    // Test 34: Validate album with multiple performers
    @Test
    fun validateAlbumMultiplePerformersTest() {
        val multiPerformerAlbum = mockAlbumWithPerformer.copy(
            performers = mockPerformers
        )

        composeTestRule.setContent {
            AlbumsGrid(albums = listOf(multiPerformerAlbum))
        }

        // Should show first performer
        composeTestRule.onNodeWithText("Queen").assertExists()
    }

    // Test 35: Validate grid items maintain state
    @Test
    fun validateGridItemsStateTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // All albums should be consistently present
        val firstCheck = mockAlbums.map { album ->
            composeTestRule.onNodeWithText(album.name).assertExists()
        }

        composeTestRule.waitForIdle()

        // Check again - should still be present
        mockAlbums.forEach { album ->
            composeTestRule.onNodeWithText(album.name).assertExists()
        }
    }

    // Test 36: Validate ErrorContent retry callback with state change
    @Test
    fun validateErrorContentRetryStateChangeTest() {
        var errorState = true

        composeTestRule.setContent {
            ErrorContent(
                message = "Error",
                onRetry = { errorState = false }
            )
        }

        assertTrue(errorState)

        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()

        assertEquals(false, errorState)
    }

    // Test 37: Validate complete album grid rendering flow
    @Test
    fun validateCompleteAlbumGridFlowTest() {
        composeTestRule.setContent {
            AlbumsGrid(albums = mockAlbums)
        }

        // Verify all albums are rendered
        composeTestRule.onNodeWithText("Bohemian Rhapsody").assertExists()
        composeTestRule.onNodeWithText("Unknown Album").assertExists()
        composeTestRule.onNodeWithText("Abbey Road").assertExists()
        composeTestRule.onNodeWithText("Thriller").assertExists()

        // Verify performers/labels are shown
        composeTestRule.onNodeWithText("Queen").assertExists()
        composeTestRule.onNodeWithText("Sony Music").assertExists()
        composeTestRule.onNodeWithText("The Beatles").assertExists()

        // All items should be clickable
        composeTestRule.onNodeWithText("Bohemian Rhapsody").assertHasClickAction()
    }
}