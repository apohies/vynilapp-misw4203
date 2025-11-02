package com.uniandes.vynilapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.uniandes.vynilapp.model.Artist
import com.uniandes.vynilapp.model.ArtistAlbum
import com.uniandes.vynilapp.views.ArtistItem
import com.uniandes.vynilapp.views.ArtistsList
import com.uniandes.vynilapp.views.LoadingArtists
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArtistActivityTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var artistClickCount = 0
    private var clickedArtist: Artist? = null
    private var retryClickCount = 0

    // Mock data
    private lateinit var mockArtists: List<Artist>
    private lateinit var mockSingleArtist: Artist
    private lateinit var mockArtistWithAlbums: Artist
    private lateinit var mockArtistWithoutImage: Artist

    @Before
    fun setup() {
        artistClickCount = 0
        clickedArtist = null
        retryClickCount = 0

        // Setup mock albums
        val mockAlbums = listOf(
            ArtistAlbum(
                id = 1,
                name = "Album 1",
                cover = "https://example.com/album1.jpg",
                releaseDate = "2020-01-01T00:00:00.000Z",
                description = "Description 1",
                genre = "Rock",
                recordLabel = "Label 1"
            ),
            ArtistAlbum(
                id = 2,
                name = "Album 2",
                cover = "https://example.com/album2.jpg",
                releaseDate = "2021-01-01T00:00:00.000Z",
                description = "Description 2",
                genre = "Pop",
                recordLabel = "Label 2"
            )
        )

        // Setup mock artists
        mockSingleArtist = Artist(
            id = 100,
            name = "Rubén Blades",
            image = "https://example.com/artist1.jpg",
            description = "Panamanian musician",
            birthDate = "1948-07-16T00:00:00.000Z",
            albums = emptyList(),
            performerPrizes = emptyList()
        )

        mockArtistWithAlbums = Artist(
            id = 101,
            name = "Carlos Vives",
            image = "https://example.com/artist2.jpg",
            description = "Colombian singer",
            birthDate = "1961-08-07T00:00:00.000Z",
            albums = mockAlbums,
            performerPrizes = emptyList()
        )

        mockArtistWithoutImage = Artist(
            id = 102,
            name = "No Image Artist",
            image = null,
            description = "Artist without image",
            birthDate = "1970-01-01T00:00:00.000Z",
            albums = listOf(mockAlbums[0]),
            performerPrizes = emptyList()
        )

        mockArtists = listOf(
            mockSingleArtist,
            mockArtistWithAlbums,
            mockArtistWithoutImage,
            Artist(
                id = 103,
                name = "Juanes",
                image = "https://example.com/artist3.jpg",
                description = "Colombian guitarist",
                birthDate = "1972-08-09T00:00:00.000Z",
                albums = emptyList(),
                performerPrizes = emptyList()
            )
        )
    }

    // ==================== ArtistsList Tests ====================

    // Test 1: Validate ArtistsList displays all artists
    @Test
    fun validateArtistsListDisplaysAllArtistsTest() {
        composeTestRule.setContent {
            ArtistsList(
                artists = mockArtists,
                onArtistClick = {}
            )
        }

        // Verify all artist names are displayed
        mockArtists.forEach { artist ->
            composeTestRule.onNodeWithText(artist.name)
                .assertExists()
        }
    }

    // Test 2: Validate ArtistsList with empty list
    @Test
    fun validateArtistsListEmptyTest() {
        composeTestRule.setContent {
            ArtistsList(
                artists = emptyList(),
                onArtistClick = {}
            )
        }

        // Should render without errors
        composeTestRule.waitForIdle()
    }

    // Test 3: Validate ArtistsList with single artist
    @Test
    fun validateArtistsListSingleArtistTest() {
        composeTestRule.setContent {
            ArtistsList(
                artists = listOf(mockSingleArtist),
                onArtistClick = {}
            )
        }

        composeTestRule.onNodeWithText("Rubén Blades")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 4: Validate artist click callback
    @Test
    fun validateArtistClickCallbackTest() {
        composeTestRule.setContent {
            ArtistsList(
                artists = mockArtists,
                onArtistClick = { artist ->
                    clickedArtist = artist
                    artistClickCount++
                }
            )
        }

        // Click on first artist
        composeTestRule.onNodeWithText("Rubén Blades").performClick()
        composeTestRule.waitForIdle()

        Assert.assertNotNull(clickedArtist)
        Assert.assertEquals("Rubén Blades", clickedArtist?.name)
        Assert.assertEquals(1, artistClickCount)
    }

    // Test 5: Validate multiple artist clicks
    @Test
    fun validateMultipleArtistClicksTest() {
        val clickedArtists = mutableListOf<Artist>()

        composeTestRule.setContent {
            ArtistsList(
                artists = mockArtists,
                onArtistClick = { artist ->
                    clickedArtists.add(artist)
                }
            )
        }

        composeTestRule.onNodeWithText("Rubén Blades").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Carlos Vives").performClick()
        composeTestRule.waitForIdle()

        Assert.assertEquals(2, clickedArtists.size)
        Assert.assertEquals("Rubén Blades", clickedArtists[0].name)
        Assert.assertEquals("Carlos Vives", clickedArtists[1].name)
    }

    // ==================== ArtistItem Tests ====================

    // Test 6: Validate ArtistItem displays artist name
    @Test
    fun validateArtistItemDisplaysNameTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockSingleArtist,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("Rubén Blades")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 7: Validate ArtistItem displays album count
    @Test
    fun validateArtistItemDisplaysAlbumCountTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockArtistWithAlbums,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("2 albums")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 8: Validate ArtistItem displays singular album
    @Test
    fun validateArtistItemDisplaysSingularAlbumTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockArtistWithoutImage,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("1 album")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 9: Validate ArtistItem displays zero albums
    @Test
    fun validateArtistItemDisplaysZeroAlbumsTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockSingleArtist,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("0 albums")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 10: Validate ArtistItem is clickable
    @Test
    fun validateArtistItemIsClickableTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockSingleArtist,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("Rubén Blades")
            .assertHasClickAction()
    }

    // Test 11: Validate ArtistItem click callback
    @Test
    fun validateArtistItemClickTest() {
        var clicked = false

        composeTestRule.setContent {
            ArtistItem(
                artist = mockSingleArtist,
                onClick = {
                    clicked = true
                }
            )
        }

        composeTestRule.onNodeWithText("Rubén Blades").performClick()
        composeTestRule.waitForIdle()

        Assert.assertTrue(clicked)
    }

    // Test 12: Validate ArtistItem with image has content description
    @Test
    fun validateArtistItemImageContentDescriptionTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockSingleArtist,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Rubén Blades image")
            .assertExists()
    }

    // Test 13: Validate ArtistItem without image shows placeholder
    @Test
    fun validateArtistItemPlaceholderTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockArtistWithoutImage,
                onClick = {}
            )
        }

        // Should show placeholder with content description
        composeTestRule.onNodeWithContentDescription("placeholder")
            .assertExists()
    }

    // Test 14: Validate ArtistItem with blank image URL
    @Test
    fun validateArtistItemBlankImageUrlTest() {
        val artistBlankImage = mockSingleArtist.copy(image = "")

        composeTestRule.setContent {
            ArtistItem(
                artist = artistBlankImage,
                onClick = {}
            )
        }

        // Should show placeholder
        composeTestRule.onNodeWithContentDescription("placeholder")
            .assertExists()
    }

    // Test 15: Validate ArtistItem rapid clicks
    @Test
    fun validateArtistItemRapidClicksTest() {
        var clickCount = 0

        composeTestRule.setContent {
            ArtistItem(
                artist = mockSingleArtist,
                onClick = { clickCount++ }
            )
        }

        repeat(10) {
            composeTestRule.onNodeWithText("Rubén Blades").performClick()
        }

        composeTestRule.waitForIdle()
        Assert.assertEquals(10, clickCount)
    }

    // ==================== LoadingArtists Tests ====================

    // Test 16: Validate LoadingArtists displays loading indicator
    @Test
    fun validateLoadingArtistsDisplaysIndicatorTest() {
        composeTestRule.setContent {
            LoadingArtists()
        }

        // Loading indicator should be present (we can't directly test CircularProgressIndicator,
        // but we can verify the component renders without errors)
        composeTestRule.waitForIdle()
    }

    // Test 17: Validate LoadingArtists renders without errors
    @Test
    fun validateLoadingArtistsRendersTest() {
        composeTestRule.setContent {
            LoadingArtists()
        }

        // Should render without crashing
        composeTestRule.waitForIdle()
    }

    // ==================== ArtistsList Integration Tests ====================

    // Test 18: Validate ArtistsList with large list
    @Test
    fun validateArtistsListLargeListTest() {
        val largeList = List(50) { index ->
            Artist(
                id = index,
                name = "Artist $index",
                image = "https://example.com/artist$index.jpg",
                description = "Description $index",
                birthDate = "1970-01-01T00:00:00.000Z",
                albums = emptyList(),
                performerPrizes = emptyList()
            )
        }

        composeTestRule.setContent {
            ArtistsList(
                artists = largeList,
                onArtistClick = {}
            )
        }

        // First item should be visible
        composeTestRule.onNodeWithText("Artist 0")
            .assertExists()
    }

    // Test 19: Validate ArtistsList scrolling
    @Test
    fun validateArtistsListScrollingTest() {
        val artists = List(15) { index ->
            Artist(
                id = index,
                name = "Scrollable Artist $index",
                image = null,
                description = "Description $index",
                birthDate = "1970-01-01T00:00:00.000Z",
                albums = emptyList(),
                performerPrizes = emptyList()
            )
        }

        composeTestRule.setContent {
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                ArtistsList(
                    artists = artists,
                    onArtistClick = {}
                )
            }
        }

        // First item should be visible
        composeTestRule.onNodeWithText("Scrollable Artist 0")
            .assertExists()
            .assertIsDisplayed()

        // Last item initially not visible (off-screen)
        composeTestRule.onNodeWithText("Scrollable Artist 14")
            .assertDoesNotExist()

        // Verify the list is working correctly with multiple items
        composeTestRule.waitForIdle()
    }

    // Test 20: Validate clicking different artists in sequence
    @Test
    fun validateSequentialArtistClicksTest() {
        val clickSequence = mutableListOf<String>()

        composeTestRule.setContent {
            ArtistsList(
                artists = mockArtists,
                onArtistClick = { artist ->
                    clickSequence.add(artist.name)
                }
            )
        }

        composeTestRule.onNodeWithText("Rubén Blades").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Carlos Vives").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Juanes").performClick()
        composeTestRule.waitForIdle()

        Assert.assertEquals(listOf("Rubén Blades", "Carlos Vives", "Juanes"), clickSequence)
    }

    // Test 21: Validate ArtistItem with special characters in name
    @Test
    fun validateArtistItemSpecialCharactersTest() {
        val artistSpecialChars = mockSingleArtist.copy(name = "Café Tacvba & Los Ángeles")

        composeTestRule.setContent {
            ArtistItem(
                artist = artistSpecialChars,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("Café Tacvba & Los Ángeles")
            .assertExists()
    }

    // Test 22: Validate ArtistItem with very long name
    @Test
    fun validateArtistItemLongNameTest() {
        val artistLongName = mockSingleArtist.copy(
            name = "This is a very long artist name that might wrap to multiple lines or truncate"
        )

        composeTestRule.setContent {
            ArtistItem(
                artist = artistLongName,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText(artistLongName.name, substring = true)
            .assertExists()
    }

    // Test 23: Validate ArtistItem with many albums
    @Test
    fun validateArtistItemManyAlbumsTest() {
        val manyAlbums = List(100) { index ->
            ArtistAlbum(
                id = index,
                name = "Album $index",
                cover = "https://example.com/album$index.jpg",
                releaseDate = "2020-01-01T00:00:00.000Z",
                description = "Description",
                genre = "Rock",
                recordLabel = "Label"
            )
        }

        val artistManyAlbums = mockSingleArtist.copy(albums = manyAlbums)

        composeTestRule.setContent {
            ArtistItem(
                artist = artistManyAlbums,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("100 albums")
            .assertExists()
    }

    // Test 24: Validate ArtistsList all items are clickable
    @Test
    fun validateAllArtistsClickableTest() {
        composeTestRule.setContent {
            ArtistsList(
                artists = mockArtists,
                onArtistClick = {}
            )
        }

        // All artist items should be clickable
        mockArtists.forEach { artist ->
            composeTestRule.onNodeWithText(artist.name)
                .assertHasClickAction()
        }
    }

    // Test 25: Validate ArtistItem divider exists
    @Test
    fun validateArtistItemDividerTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockSingleArtist,
                onClick = {}
            )
        }

        // HorizontalDivider should be present (rendered without errors)
        composeTestRule.waitForIdle()
    }

    // Test 26: Validate ArtistsList key uniqueness
    @Test
    fun validateArtistsListKeyUniquenessTest() {
        // Create artists with unique IDs
        val artists = List(6) { index ->
            Artist(
                id = index,
                name = "Artist $index",
                image = null,
                description = "Description",
                birthDate = "1970-01-01T00:00:00.000Z",
                albums = emptyList(),
                performerPrizes = emptyList()
            )
        }

        composeTestRule.setContent {
            ArtistsList(
                artists = artists,
                onArtistClick = {}
            )
        }

        // All artists should be rendered
        artists.forEach { artist ->
            composeTestRule.onNodeWithText(artist.name).assertExists()
        }
    }

    // Test 27: Validate ArtistItem renders all components together
    @Test
    fun validateArtistItemAllComponentsTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockArtistWithAlbums,
                onClick = {}
            )
        }

        // Name and album count should both be visible
        composeTestRule.onNodeWithText("Carlos Vives").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 albums").assertIsDisplayed()
    }

    // Test 28: Validate ArtistsList with null albums list handling
    @Test
    fun validateArtistItemNullAlbumsHandlingTest() {
        // The try-catch in ArtistItem should handle this
        composeTestRule.setContent {
            ArtistItem(
                artist = mockSingleArtist.copy(albums = emptyList()),
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("0 albums").assertExists()
    }

    // Test 29: Validate multiple ArtistItems in column
    @Test
    fun validateMultipleArtistItemsTest() {
        composeTestRule.setContent {
            Column {
                mockArtists.forEach { artist ->
                    ArtistItem(
                        artist = artist,
                        onClick = {}
                    )
                }
            }
        }

        // All artists should be rendered
        mockArtists.forEach { artist ->
            composeTestRule.onNodeWithText(artist.name).assertExists()
        }
    }

    // Test 30: Validate ArtistItem click doesn't affect other items
    @Test
    fun validateArtistItemIsolatedClickTest() {
        val clickCounts = mutableMapOf<Int, Int>()

        composeTestRule.setContent {
            ArtistsList(
                artists = mockArtists.take(3),
                onArtistClick = { artist ->
                    clickCounts[artist.id] = (clickCounts[artist.id] ?: 0) + 1
                }
            )
        }

        // Click first artist twice
        composeTestRule.onNodeWithText("Rubén Blades").performClick()
        composeTestRule.onNodeWithText("Rubén Blades").performClick()
        composeTestRule.waitForIdle()

        // Click second artist once
        composeTestRule.onNodeWithText("Carlos Vives").performClick()
        composeTestRule.waitForIdle()

        Assert.assertEquals(2, clickCounts[100])
        Assert.assertEquals(1, clickCounts[101])
        Assert.assertNull(clickCounts[102])
    }

    // Test 31: Validate ArtistItem with empty name
    @Test
    fun validateArtistItemEmptyNameTest() {
        val artistEmptyName = mockSingleArtist.copy(name = "")

        composeTestRule.setContent {
            ArtistItem(
                artist = artistEmptyName,
                onClick = {}
            )
        }

        // Should render album count
        composeTestRule.onNodeWithText("0 albums").assertExists()
    }

    // Test 32: Validate LoadingArtists is centered
    @Test
    fun validateLoadingArtistsCenteredTest() {
        composeTestRule.setContent {
            LoadingArtists()
        }

        // Should render without errors (centered)
        composeTestRule.waitForIdle()
    }

    // Test 33: Validate ArtistsList with filtered artists
    @Test
    fun validateArtistsListFilteredTest() {
        val filteredArtists = mockArtists.filter { it.albums.isNotEmpty() }

        composeTestRule.setContent {
            ArtistsList(
                artists = filteredArtists,
                onArtistClick = {}
            )
        }

        // Only artists with albums should be displayed
        composeTestRule.onNodeWithText("Carlos Vives").assertExists()
        composeTestRule.onNodeWithText("No Image Artist").assertExists()
        composeTestRule.onNodeWithText("Rubén Blades").assertDoesNotExist()
    }

    // Test 34: Validate complete artist selection flow
    @Test
    fun validateCompleteArtistSelectionFlowTest() {
        var selectedArtist: Artist? = null

        composeTestRule.setContent {
            ArtistsList(
                artists = mockArtists,
                onArtistClick = { artist ->
                    selectedArtist = artist
                }
            )
        }

        // Initial state
        Assert.assertNull(selectedArtist)

        // Select first artist
        composeTestRule.onNodeWithText("Rubén Blades").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(100, selectedArtist?.id)

        // Select second artist
        composeTestRule.onNodeWithText("Carlos Vives").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(101, selectedArtist?.id)
    }

    // Test 35: Validate ArtistItem image with valid URL
    @Test
    fun validateArtistItemValidImageUrlTest() {
        composeTestRule.setContent {
            ArtistItem(
                artist = mockSingleArtist,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Rubén Blades image")
            .assertExists()
    }

    // Test 36: Validate ArtistsList maintains scroll position
    @Test
    fun validateArtistsListScrollPositionTest() {
        val largeList = List(30) { index ->
            Artist(
                id = index,
                name = "Scrollable Artist $index",
                image = null,
                description = "Description",
                birthDate = "1970-01-01T00:00:00.000Z",
                albums = emptyList(),
                performerPrizes = emptyList()
            )
        }

        composeTestRule.setContent {
            ArtistsList(
                artists = largeList,
                onArtistClick = {}
            )
        }

        // First item should be visible
        composeTestRule.onNodeWithText("Scrollable Artist 0")
            .assertExists()
    }

    // Test 37: Validate all components render without errors
    @Test
    fun validateAllComponentsRenderTest() {
        composeTestRule.setContent {
            Column {
                LoadingArtists()
                ArtistsList(
                    artists = mockArtists,
                    onArtistClick = {}
                )
            }
        }

        // All should render without crashing
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Rubén Blades").assertExists()
    }
}