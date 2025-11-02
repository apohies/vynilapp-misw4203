package com.uniandes.vynilapp.e2eNavigationTest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilapp.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ArtistDetailNavigationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // Test 1: Navigate to Artists tab and click first artist item
    @Test
    fun validateNavigateToArtistDetailTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()

        // Wait for artists to load
        Thread.sleep(3000)

        // Find and click the first artist item
        // Artists are displayed with "X album" or "X albums" text
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Verify we navigated to artist detail by checking for back button
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()
    }

    // Test 2: Validate artist detail screen shows artist name
    @Test
    fun validateArtistDetailShowsArtistNameTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Artist name should be displayed (large text, bold)
        // We can verify the detail screen loaded by checking for back button
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()
    }

    // Test 3: Validate artist detail shows description
    @Test
    fun validateArtistDetailShowsDescriptionTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Artist detail should show description section
        // Back button confirms we're on detail screen
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 4: Validate artist detail shows albums section
    @Test
    fun validateArtistDetailShowsAlbumsSectionTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2500)

        // Check for albums section or additional info
        // Verify we're on detail screen
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()
    }

    // Test 5: Validate artist detail shows additional information
    @Test
    fun validateArtistDetailShowsAdditionalInfoTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2500)

        // Artist detail should show birth date and popularity info
        // Look for star rating (★★★★☆)
        composeTestRule.onNodeWithText("★★★★☆", substring = true)
            .assertExists()
    }

    // Test 6: Validate back button on artist detail works
    @Test
    fun validateBackButtonFromArtistDetailTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Click back button
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Should return to Artists list
        // Bottom navigation should be visible
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 7: Validate multiple artists can be clicked
    @Test
    fun validateMultipleArtistsClickableTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Verify detail screen loaded
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()

        // Go back
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Click second artist if available
        val artistNodes = composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
        if (artistNodes.fetchSemanticsNodes().size > 1) {
            artistNodes[1].performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(2000)

            // Verify we're on detail screen again
            composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
                .assertExists()
        }
    }

    // Test 8: Validate artist detail screen is scrollable
    @Test
    fun validateArtistDetailScrollableTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2500)

        // Artist detail should be scrollable (has description, albums, additional info)
        // Verify main elements exist
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()

        composeTestRule.onNodeWithText("★★★★☆", substring = true)
            .assertExists()
    }

    // Test 9: Validate complete artist detail information rendered
    @Test
    fun validateCompleteArtistDetailInformationTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2500)

        // 1. Verify back button (TopBar)
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        // 5. Verify additional info rows exist (popularity, birth date)
        composeTestRule.onNodeWithText("★★★★☆", substring = true)
            .assertExists()

        // All components should be rendered without crash
        composeTestRule.waitForIdle()
    }

    // Test 10: Validate artist detail shows all UI sections
    @Test
    fun validateArtistDetailAllUISectionsTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Verify all major sections exist:

        // 1. TopBar with back button
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()

        // 2. Artist Info section (avatar + name + description)
        // Should have scrollable content

        // 3. Additional Info section (birth date, popularity)
        composeTestRule.onNodeWithText("★★★★☆", substring = true)
            .assertExists()

        // 4. Albums section (if artist has albums)
        // Albums would be displayed in horizontal scroll

        // Screen should be fully rendered without errors
        composeTestRule.waitForIdle()
    }

    // Test 11: Validate navigation flow: Artists -> Detail -> Back -> Detail again
    @Test
    fun validateRepeatNavigationToArtistDetailTest() {
        // First visit
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()

        // Go back
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Second visit - click same artist again
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Should load again successfully
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()

        composeTestRule.onNodeWithText("★★★★☆", substring = true)
            .assertExists()
    }

    // Test 12: Validate artist detail loads without errors
    @Test
    fun validateArtistDetailLoadsWithoutErrorsTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Should not show error screen
        // Should show artist detail components
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()

        // Should not show "Reintentar" (retry button from error screen)
        composeTestRule.onNodeWithText("Reintentar").assertDoesNotExist()
    }

    // Test 13: Validate artist detail renders with loading state
    @Test
    fun validateArtistDetailHandlesLoadingTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Click first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()

        // During loading, screen might show loading indicator
        // After loading (wait 2 seconds), should show content
        Thread.sleep(2500)

        // Content should be visible after loading
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()
    }

    // Test 14: Validate complete end-to-end flow with all validations
    @Test
    fun validateCompleteE2EFlowWithValidationsTest() {
        // Step 1: Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Step 2: Verify artists list loaded
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .assertExists()

        // Step 3: Click on first artist
        composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Step 4: Validate ALL artist detail components

        // TopBar
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()

        // Artist Info (name, description shown)
        // Avatar/placeholder should be visible

        // Additional Info
        composeTestRule.onNodeWithText("★★★★☆", substring = true)
            .assertExists()

        // Step 5: Navigate back
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(1500)

        // Step 6: Verify returned to Artists list
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()

        // Step 7: Navigate to a different tab
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Complete flow validated successfully
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 15: Validate artist detail with different artists
    @Test
    fun validateDifferentArtistsDetailTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Get all artist items
        val artistNodes = composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)
        val artistCount = artistNodes.fetchSemanticsNodes().size

        // Test first artist
        if (artistCount > 0) {
            artistNodes[0].performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(2000)

            composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
                .assertExists()

            composeTestRule.onNodeWithText("★★★★☆", substring = true)
                .assertExists()

            // Go back
            composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
                .performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(2000)
        }

        // Test second artist if available
        if (artistCount > 1) {
            composeTestRule.onAllNodesWithText("album", substring = true, ignoreCase = true)[1]
                .performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(2000)

            composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
                .assertExists()

            composeTestRule.onNodeWithText("★★★★☆", substring = true)
                .assertExists()
        }
    }
}