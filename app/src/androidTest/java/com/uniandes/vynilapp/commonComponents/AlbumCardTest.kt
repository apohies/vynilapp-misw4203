package com.uniandes.vynilapp.commonComponents

import androidx.compose.foundation.layout.Row
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilapp.views.common.AlbumCard
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AlbumCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var clickCount = 0
    private var cardClicked = false

    @Before
    fun setup() {
        clickCount = 0
        cardClicked = false
    }

    // Test 1: Validate AlbumCard displays all elements
    @Test
    fun validateAlbumCardDisplaysAllElementsTest() {
        val albumTitle = "Test Album"
        val artistName = "Test Artist"
        val imageUrl = "https://example.com/image.jpg"

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = albumTitle,
                artistName = artistName,
                imageUrl = imageUrl
            )
        }

        // Verify album title is displayed
        composeTestRule.onNodeWithText(albumTitle)
            .assertExists()
            .assertIsDisplayed()

        // Verify artist name is displayed
        composeTestRule.onNodeWithText(artistName)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 2: Validate album title is displayed
    @Test
    fun validateAlbumTitleDisplayTest() {
        val title = "Thriller"

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = title,
                artistName = "Michael Jackson",
                imageUrl = ""
            )
        }

        composeTestRule.onNodeWithText(title)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate artist name is displayed
    @Test
    fun validateArtistNameDisplayTest() {
        val artist = "The Beatles"

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Abbey Road",
                artistName = artist,
                imageUrl = ""
            )
        }

        composeTestRule.onNodeWithText(artist)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 4: Validate album image content description
    @Test
    fun validateAlbumImageContentDescriptionTest() {
        val albumTitle = "Dark Side of the Moon"

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = albumTitle,
                artistName = "Pink Floyd",
                imageUrl = "https://example.com/image.jpg"
            )
        }

        // Verify image has correct content description
        composeTestRule.onNodeWithContentDescription(albumTitle)
            .assertExists()
    }

    // Test 5: Validate card is clickable
    @Test
    fun validateCardIsClickableTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Test Album",
                artistName = "Test Artist",
                imageUrl = "",
                onClick = {}
            )
        }

        // Verify card has click action
        composeTestRule.onNodeWithText("Test Album")
            .assertHasClickAction()
    }

    // Test 6: Validate card click triggers callback
    @Test
    fun validateCardClickTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Test Album",
                artistName = "Test Artist",
                imageUrl = "",
                onClick = {
                    cardClicked = true
                    clickCount++
                }
            )
        }

        // Click the card
        composeTestRule.onNodeWithText("Test Album")
            .performClick()

        composeTestRule.waitForIdle()
        Assert.assertTrue(cardClicked)
        Assert.assertEquals(1, clickCount)
    }

    // Test 7: Validate multiple card clicks
    @Test
    fun validateMultipleCardClicksTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Test Album",
                artistName = "Test Artist",
                imageUrl = "",
                onClick = { clickCount++ }
            )
        }

        // Click multiple times
        repeat(3) {
            composeTestRule.onNodeWithText("Test Album")
                .performClick()
        }

        composeTestRule.waitForIdle()
        Assert.assertEquals(3, clickCount)
    }

    // Test 8: Validate long album title
    @Test
    fun validateLongAlbumTitleTest() {
        val longTitle = "This is a very long album title that should be truncated to one line"

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = longTitle,
                artistName = "Artist",
                imageUrl = ""
            )
        }

        composeTestRule.onNodeWithText(longTitle, substring = true)
            .assertExists()
    }

    // Test 9: Validate long artist name
    @Test
    fun validateLongArtistNameTest() {
        val longArtist = "This is a very long artist name that should be truncated"

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Album",
                artistName = longArtist,
                imageUrl = ""
            )
        }

        composeTestRule.onNodeWithText(longArtist, substring = true)
            .assertExists()
    }

    // Test 10: Validate card with empty image URL
    @Test
    fun validateCardWithEmptyImageUrlTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Test Album",
                artistName = "Test Artist",
                imageUrl = ""
            )
        }

        // Card should still render with title and artist
        composeTestRule.onNodeWithText("Test Album").assertExists()
        composeTestRule.onNodeWithText("Test Artist").assertExists()
    }

    // Test 11: Validate Spanish content
    @Test
    fun validateSpanishContentTest() {
        val spanishTitle = "Amor Prohibido"
        val spanishArtist = "Selena"

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = spanishTitle,
                artistName = spanishArtist,
                imageUrl = ""
            )
        }

        composeTestRule.onNodeWithText(spanishTitle).assertExists()
        composeTestRule.onNodeWithText(spanishArtist).assertExists()
    }

    // Test 12: Validate special characters in title
    @Test
    fun validateSpecialCharactersInTitleTest() {
        val specialTitle = "Rock & Roll: The Best!"

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = specialTitle,
                artistName = "Various Artists",
                imageUrl = ""
            )
        }

        composeTestRule.onNodeWithText(specialTitle).assertExists()
    }

    // Test 13: Validate card structure
    @Test
    fun validateCardStructureTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Structure Test",
                artistName = "Test Artist",
                imageUrl = ""
            )
        }

        // Verify both title and artist exist
        composeTestRule.onNodeWithText("Structure Test").assertExists()
        composeTestRule.onNodeWithText("Test Artist").assertExists()
    }

    // Test 14: Validate multiple cards can be displayed
    @Test
    fun validateMultipleCardsTest() {
        composeTestRule.setContent {
            Row {
                AlbumCard(
                    albumTitle = "Album 1",
                    artistName = "Artist 1",
                    imageUrl = ""
                )
                AlbumCard(
                    albumTitle = "Album 2",
                    artistName = "Artist 2",
                    imageUrl = ""
                )
            }
        }

        composeTestRule.onNodeWithText("Album 1").assertExists()
        composeTestRule.onNodeWithText("Album 2").assertExists()
    }

    // Test 15: Validate card renders without errors
    @Test
    fun validateCardRendersWithoutErrorsTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Test",
                artistName = "Test",
                imageUrl = "https://example.com/image.jpg"
            )
        }

        composeTestRule.onNodeWithText("Test").assertExists()
    }

    // Test 16: Validate onClick callback with state change
    @Test
    fun validateOnClickCallbackWithStateChangeTest() {
        var selectedAlbum = ""

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Selected Album",
                artistName = "Artist",
                imageUrl = "",
                onClick = {
                    selectedAlbum = "Selected Album"
                }
            )
        }

        Assert.assertEquals("", selectedAlbum)

        composeTestRule.onNodeWithText("Selected Album")
            .performClick()

        composeTestRule.waitForIdle()
        Assert.assertEquals("Selected Album", selectedAlbum)
    }

    // Test 17: Validate default onClick does nothing
    @Test
    fun validateDefaultOnClickTest() {
        // Should not throw error with default onClick
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Test",
                artistName = "Test",
                imageUrl = ""
            )
        }

        composeTestRule.onNodeWithText("Test")
            .performClick()

        composeTestRule.waitForIdle()
        // No assertion needed - just verifying no crash
    }

    // Test 18: Validate rapid clicks
    @Test
    fun validateRapidClicksTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Test Album",
                artistName = "Test Artist",
                imageUrl = "",
                onClick = { clickCount++ }
            )
        }

        repeat(10) {
            composeTestRule.onNodeWithText("Test Album")
                .performClick()
        }

        composeTestRule.waitForIdle()
        Assert.assertEquals(10, clickCount)
    }

    // Test 19: Validate both text elements visible together
    @Test
    fun validateBothTextElementsVisibleTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Album Title",
                artistName = "Artist Name",
                imageUrl = ""
            )
        }

        composeTestRule.onNodeWithText("Album Title").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artist Name").assertIsDisplayed()
    }

    // Test 20: Validate numeric content
    @Test
    fun validateNumericContentTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "1989",
                artistName = "Taylor Swift",
                imageUrl = ""
            )
        }

        composeTestRule.onNodeWithText("1989").assertExists()
        composeTestRule.onNodeWithText("Taylor Swift").assertExists()
    }

    // Test 21: Validate empty strings
    @Test
    fun validateEmptyStringsTest() {
        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "",
                artistName = "",
                imageUrl = ""
            )
        }

        // Should render without crashing
        composeTestRule.waitForIdle()
    }

    // Test 22: Validate image with valid URL format
    @Test
    fun validateImageWithValidUrlTest() {
        val albumTitle = "URL Test"
        val validUrl = "https://i.pinimg.com/564x/aa/5f/ed/aa5fed7fac61cc8f41d1e79db917a7cd.jpg"

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = albumTitle,
                artistName = "Artist",
                imageUrl = validUrl
            )
        }

        composeTestRule.onNodeWithContentDescription(albumTitle).assertExists()
    }

    // Test 23: Validate click callback sequence
    @Test
    fun validateClickCallbackSequenceTest() {
        val clickSequence = mutableListOf<Int>()

        composeTestRule.setContent {
            AlbumCard(
                albumTitle = "Sequence Test",
                artistName = "Artist",
                imageUrl = "",
                onClick = {
                    clickSequence.add(clickSequence.size + 1)
                }
            )
        }

        composeTestRule.onNodeWithText("Sequence Test").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(listOf(1), clickSequence)

        composeTestRule.onNodeWithText("Sequence Test").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(listOf(1, 2), clickSequence)
    }
}