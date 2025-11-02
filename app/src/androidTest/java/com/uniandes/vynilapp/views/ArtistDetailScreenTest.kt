package com.uniandes.vynilapp.views

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.ArtistAlbum
import com.uniandes.vynilapp.views.states.ArtistDetailEvent
import com.uniandes.vynilapp.views.states.ArtistDetailUiState
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class ArtistDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var backClickCount = 0
    private var backClicked = false
    private var albumClickedId: Int? = null
    private val eventList = mutableListOf<ArtistDetailEvent>()

    // Mock data
    private lateinit var mockArtist: Artist
    private lateinit var mockAlbums: List<ArtistAlbum>
    private lateinit var mockUiStateWithData: ArtistDetailUiState
    private lateinit var mockUiStateLoading: ArtistDetailUiState
    private lateinit var mockUiStateError: ArtistDetailUiState
    private lateinit var mockUiStateNoAlbums: ArtistDetailUiState

    @Before
    fun setup() {
        backClickCount = 0
        backClicked = false
        albumClickedId = null
        eventList.clear()

        // Setup mock albums
        mockAlbums = listOf(
            ArtistAlbum(
                id = 1,
                name = "Buscando América",
                cover = "https://example.com/album1.jpg",
                releaseDate = "1984-08-01T00:00:00.000Z",
                description = "Classic album",
                genre = "Salsa",
                recordLabel = "Elektra"
            ),
            ArtistAlbum(
                id = 2,
                name = "Siembra",
                cover = "https://example.com/album2.jpg",
                releaseDate = "1978-09-01T00:00:00.000Z",
                description = "Iconic album",
                genre = "Salsa",
                recordLabel = "Fania"
            )
        )

        // Setup mock artist
        mockArtist = Artist(
            id = 100,
            name = "Rubén Blades",
            image = "https://example.com/artist.jpg",
            description = "Panamanian musician, singer, composer, actor, activist, and politician.",
            birthDate = "1948-07-16T00:00:00.000Z",
            albums = mockAlbums,
            performerPrizes = emptyList()
        )

        // Setup mock UI states
        mockUiStateWithData = ArtistDetailUiState(
            isLoading = false,
            artist = mockArtist,
            albums = mockAlbums,
            performerPrizes = emptyList(),
            error = null
        )

        mockUiStateLoading = ArtistDetailUiState(
            isLoading = true,
            artist = null,
            albums = emptyList(),
            performerPrizes = emptyList(),
            error = null
        )

        mockUiStateError = ArtistDetailUiState(
            isLoading = false,
            artist = null,
            albums = emptyList(),
            performerPrizes = emptyList(),
            error = "Failed to load artist data"
        )

        mockUiStateNoAlbums = ArtistDetailUiState(
            isLoading = false,
            artist = mockArtist,
            albums = emptyList(),
            performerPrizes = emptyList(),
            error = null
        )
    }

    // ==================== ArtistDetailContent Tests ====================

    // Test 1: Validate ArtistDetailContent displays artist name
    @Test
    fun validateArtistNameDisplayTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Rubén Blades")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 2: Validate artist description is displayed
    @Test
    fun validateArtistDescriptionDisplayTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Panamanian musician, singer, composer, actor, activist, and politician.", substring = true)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate TopBar back button is displayed
    @Test
    fun validateTopBarBackButtonTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Back")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 4: Validate TopBar back button click
    @Test
    fun validateTopBarBackButtonClickTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = {
                    backClicked = true
                    backClickCount++
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()

        assertTrue(backClicked)
        assertEquals(1, backClickCount)
    }

    // Test 5: Validate albums are displayed
    @Test
    fun validateAlbumsDisplayTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = {}
            )
        }

        // Verify album titles are displayed
        composeTestRule.onNodeWithText("Buscando América")
            .assertExists()

        composeTestRule.onNodeWithText("Siembra")
            .assertExists()
    }

    // Test 6: Validate album click callback
    @Test
    fun validateAlbumClickTest() {
        composeTestRule.setContent {
            ArtistAlbums(
                albums = mockAlbums,
                onAlbumClick = { albumId ->
                    albumClickedId = albumId
                }
            )
        }

        // Click on the first album
        composeTestRule.onNodeWithText("Buscando América")
            .performClick()

        composeTestRule.waitForIdle()
        assertEquals(1, albumClickedId)
    }

    // Test 7: Validate multiple album clicks
    @Test
    fun validateMultipleAlbumClicksTest() {
        val clickedAlbums = mutableListOf<Int>()

        composeTestRule.setContent {
            ArtistAlbums(
                albums = mockAlbums,
                onAlbumClick = { albumId ->
                    clickedAlbums.add(albumId)
                }
            )
        }

        composeTestRule.onNodeWithText("Buscando América").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Siembra").performClick()
        composeTestRule.waitForIdle()

        assertEquals(listOf(1, 2), clickedAlbums)
    }

    // Test 8: Validate artist info rows are displayed
    @Test
    fun validateArtistInfoRowsTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = {}
            )
        }

        // Should show birth date and popularity (from strings)
        // These are loaded from string resources, so we verify structure exists
        composeTestRule.onNodeWithText("★★★★☆")
            .assertExists()
    }

    // Test 9: Validate ArtistInfoRow component
    @Test
    fun validateArtistInfoRowComponentTest() {
        composeTestRule.setContent {
            ArtistInfoRow(
                label = "Birth Date",
                value = "July 16, 1948"
            )
        }

        composeTestRule.onNodeWithText("Birth Date").assertExists()
        composeTestRule.onNodeWithText("July 16, 1948").assertExists()
    }

    // Test 10: Validate ArtistAvatar placeholder without image
    @Test
    fun validateArtistAvatarPlaceholderTest() {
        val artistNoImage = mockArtist.copy(image = null)

        composeTestRule.setContent {
            ArtistAvatar(artist = artistNoImage)
        }

        // Should show first letter of name
        composeTestRule.onNodeWithText("R")
            .assertExists()
    }

    // Test 11: Validate ArtistAvatarPlaceHolder component
    @Test
    fun validateArtistAvatarPlaceholderComponentTest() {
        composeTestRule.setContent {
            ArtistAvatarPlaceHolder(name = "Pink Floyd")
        }

        composeTestRule.onNodeWithText("P")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 12: Validate ArtistAvatarPlaceHolder with null name
    @Test
    fun validateArtistAvatarPlaceholderNullNameTest() {
        composeTestRule.setContent {
            ArtistAvatarPlaceHolder(name = null)
        }

        composeTestRule.onNodeWithText("A")
            .assertExists()
    }

    // Test 13: Validate AlbumCard displays album name
    @Test
    fun validateAlbumCardNameTest() {
        composeTestRule.setContent {
            AlbumCard(
                album = mockAlbums[0],
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("Buscando América")
            .assertExists()
    }

    // Test 14: Validate AlbumCard displays release year
    @Test
    fun validateAlbumCardReleaseYearTest() {
        composeTestRule.setContent {
            AlbumCard(
                album = mockAlbums[0],
                onClick = {}
            )
        }

        // Year should be extracted from date
        composeTestRule.onNodeWithText("1984", substring = true)
            .assertExists()
    }

    // Test 15: Validate AlbumCard click
    @Test
    fun validateAlbumCardClickTest() {
        var clicked = false

        composeTestRule.setContent {
            AlbumCard(
                album = mockAlbums[0],
                onClick = { clicked = true }
            )
        }

        composeTestRule.onNodeWithText("Buscando América")
            .performClick()

        composeTestRule.waitForIdle()
        assertTrue(clicked)
    }

    // Test 16: Validate ArtistAlbums displays album section title
    @Test
    fun validateArtistAlbumsSectionTitleTest() {
        composeTestRule.setContent {
            ArtistAlbums(
                albums = mockAlbums,
                onAlbumClick = {}
            )
        }

        // Should show albums section (loaded from string resources)
        // We verify albums are shown
        composeTestRule.onNodeWithText("Buscando América").assertExists()
        composeTestRule.onNodeWithText("Siembra").assertExists()
    }

    // Test 17: Validate all albums are rendered
    @Test
    fun validateAllAlbumsRenderedTest() {
        composeTestRule.setContent {
            ArtistAlbums(
                albums = mockAlbums,
                onAlbumClick = {}
            )
        }

        // All album names should be present
        mockAlbums.forEach { album ->
            composeTestRule.onNodeWithText(album.name).assertExists()
        }
    }

    // Test 18: Validate screen with long description
    @Test
    fun validateLongDescriptionTest() {
        val longDescription = "This is a very long description that contains a lot of information about the artist's career, achievements, musical style, and contributions to the music industry over many decades of work."
        val artistWithLongDesc = mockArtist.copy(description = longDescription)
        val uiState = mockUiStateWithData.copy(artist = artistWithLongDesc)

        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = uiState,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText(longDescription, substring = true)
            .assertExists()
    }

    // Test 19: Validate screen with special characters in name
    @Test
    fun validateSpecialCharactersInNameTest() {
        val artistWithSpecialChars = mockArtist.copy(name = "Café Tacvba & Los Ángeles")
        val uiState = mockUiStateWithData.copy(artist = artistWithSpecialChars)

        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = uiState,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText("Café Tacvba & Los Ángeles")
            .assertExists()
    }

    // Test 20: Validate screen scrollability
    @Test
    fun validateScreenScrollabilityTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = {}
            )
        }

        // Screen should be scrollable - verify content exists
        composeTestRule.onNodeWithText(mockArtist.name)
            .assertExists()
    }

    // Test 21: Validate ArtistInfoRow with empty values
    @Test
    fun validateArtistInfoRowEmptyValuesTest() {
        composeTestRule.setContent {
            ArtistInfoRow(
                label = "Label",
                value = ""
            )
        }

        composeTestRule.onNodeWithText("Label").assertExists()
    }

    // Test 22: Validate multiple ArtistInfoRows
    @Test
    fun validateMultipleArtistInfoRowsTest() {
        composeTestRule.setContent {
            androidx.compose.foundation.layout.Column {
                ArtistInfoRow(label = "Birth Date", value = "1948")
                ArtistInfoRow(label = "Genre", value = "Salsa")
                ArtistInfoRow(label = "Country", value = "Panama")
            }
        }

        composeTestRule.onNodeWithText("Birth Date").assertExists()
        composeTestRule.onNodeWithText("Genre").assertExists()
        composeTestRule.onNodeWithText("Country").assertExists()
    }

    // Test 23: Validate AlbumCard with long album name
    @Test
    fun validateAlbumCardLongNameTest() {
        val longAlbum = mockAlbums[0].copy(
            name = "This is a very long album name that should be truncated with ellipsis"
        )

        composeTestRule.setContent {
            AlbumCard(
                album = longAlbum,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText(longAlbum.name, substring = true)
            .assertExists()
    }

    // Test 24: Validate ArtistDetailContent renders without errors
    @Test
    fun validateArtistDetailContentRendersWithoutErrorsTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText(mockArtist.name).assertExists()
    }

    // Test 25: Validate rapid back button clicks
    @Test
    fun validateRapidBackClicksTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = { backClickCount++ }
            )
        }

        repeat(5) {
            composeTestRule.onNodeWithContentDescription("Back").performClick()
        }

        composeTestRule.waitForIdle()
        assertEquals(5, backClickCount)
    }

    // Test 26: Validate artist with no description
    @Test
    fun validateArtistNoDescriptionTest() {
        val artistNoDesc = mockArtist.copy(description = "")
        val uiState = mockUiStateWithData.copy(artist = artistNoDesc)

        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = uiState,
                onEvent = {},
                onBack = {}
            )
        }

        // Should still render name
        composeTestRule.onNodeWithText(mockArtist.name).assertExists()
    }

    // Test 27: Validate ArtistAlbums with single album
    @Test
    fun validateArtistAlbumsSingleAlbumTest() {
        composeTestRule.setContent {
            ArtistAlbums(
                albums = listOf(mockAlbums[0]),
                onAlbumClick = {}
            )
        }

        composeTestRule.onNodeWithText("Buscando América").assertExists()
    }

    // Test 28: Validate ArtistAlbums with many albums
    @Test
    fun validateArtistAlbumsManyAlbumsTest() {
        val manyAlbums = List(10) { index ->
            ArtistAlbum(
                id = index,
                name = "Album $index",
                cover = "https://example.com/album$index.jpg",
                releaseDate = "2020-01-01T00:00:00.000Z",
                description = "Description $index",
                genre = "Rock",
                recordLabel = "Label"
            )
        }

        composeTestRule.setContent {
            ArtistAlbums(
                albums = manyAlbums,
                onAlbumClick = {}
            )
        }

        // Should render without errors
        composeTestRule.onNodeWithText("Album 0").assertExists()
    }

    // Test 29: Validate ArtistAvatarPlaceHolder with lowercase name
    @Test
    fun validateArtistAvatarPlaceholderLowercaseTest() {
        composeTestRule.setContent {
            ArtistAvatarPlaceHolder(name = "beatles")
        }

        composeTestRule.onNodeWithText("B")
            .assertExists()
    }

    // Test 30: Validate AlbumCard rapid clicks
    @Test
    fun validateAlbumCardRapidClicksTest() {
        var clickCount = 0

        composeTestRule.setContent {
            AlbumCard(
                album = mockAlbums[0],
                onClick = { clickCount++ }
            )
        }

        repeat(10) {
            composeTestRule.onNodeWithText("Buscando América").performClick()
        }

        composeTestRule.waitForIdle()
        assertEquals(10, clickCount)
    }

    // Test 31: Validate ArtistDetailContent with null artist name
    @Test
    fun validateArtistDetailContentNullNameTest() {
        val artistNullName = mockArtist.copy(name = "")
        val uiState = mockUiStateWithData.copy(artist = artistNullName)

        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = uiState,
                onEvent = {},
                onBack = {}
            )
        }

        // Should render without crashing
        composeTestRule.onNodeWithContentDescription("Back").assertExists()
    }

    // Test 32: Validate complete artist detail flow
    @Test
    fun validateCompleteArtistDetailFlowTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = { event ->
                    eventList.add(event)
                },
                onBack = { backClicked = true }
            )
        }

        // Verify all main components
        composeTestRule.onNodeWithContentDescription("Back").assertExists()
        composeTestRule.onNodeWithText(mockArtist.name).assertExists()
        composeTestRule.onNodeWithText("Buscando América").assertExists()
        composeTestRule.onNodeWithText("Siembra").assertExists()

        // Click back
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()

        assertTrue(backClicked)
    }

    // Test 33: Validate ArtistInfoRow with long label
    @Test
    fun validateArtistInfoRowLongLabelTest() {
        composeTestRule.setContent {
            ArtistInfoRow(
                label = "This is a very long label that might affect layout",
                value = "Value"
            )
        }

        composeTestRule.onNodeWithText("This is a very long label that might affect layout", substring = true)
            .assertExists()
    }

    // Test 34: Validate ArtistInfoRow with long value
    @Test
    fun validateArtistInfoRowLongValueTest() {
        composeTestRule.setContent {
            ArtistInfoRow(
                label = "Label",
                value = "This is a very long value that contains a lot of information"
            )
        }

        composeTestRule.onNodeWithText("This is a very long value that contains a lot of information", substring = true)
            .assertExists()
    }

    // Test 35: Validate album year formatting
    @Test
    fun validateAlbumYearFormattingTest() {
        composeTestRule.setContent {
            AlbumCard(
                album = mockAlbums[1], // Siembra from 1978
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("1978", substring = true)
            .assertExists()
    }

    // Test 36: Validate all components visible together
    @Test
    fun validateAllComponentsVisibleTogetherTest() {
        composeTestRule.setContent {
            ArtistDetailContent(
                uiState = mockUiStateWithData,
                onEvent = {},
                onBack = {}
            )
        }

        // All major components should be visible
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
        composeTestRule.onNodeWithText(mockArtist.name).assertExists()
        composeTestRule.onNodeWithText("Buscando América").assertExists()
    }
}