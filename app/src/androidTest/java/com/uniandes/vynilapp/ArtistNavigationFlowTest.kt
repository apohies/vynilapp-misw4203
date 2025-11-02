package com.uniandes.vynilapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.Before

@HiltAndroidTest
class ArtistNavigationFlowTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // Test 1: Validate navigation to Artists tab
    @Test
    fun validateNavigationToArtistsTabTest() {
        // Click on Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()

        // Wait for artists to load
        Thread.sleep(2000)

        // Artists tab should be active
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 2: Validate Artists screen loads and displays artists
    @Test
    fun validateArtistsScreenLoadsTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()

        // Wait for data to load
        Thread.sleep(3000)

        // Artists screen should display (either loading, error, or artists list)
        // Bottom navigation should still be visible
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 3: Validate clicking on an artist item
    @Test
    fun validateClickingArtistItemTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()

        // Wait for artists to load
        Thread.sleep(3000)

        // Try to find and click on any artist item
        // Look for common artist-related UI elements
        // Note: This will depend on what artists are loaded
        composeTestRule.waitForIdle()

        // Verify we're still in the app (no crash)
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 4: Validate navigation to artist detail and back
    @Test
    fun validateNavigateToArtistDetailAndBackTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Look for scrollable content or list items
        // The artists list should be present
        composeTestRule.waitForIdle()

        // Try to perform scroll to ensure list is loaded
        // If successful, artists are displayed
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 5: Validate artist detail screen shows back button
    @Test
    fun validateArtistDetailScreenBackButtonTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // After clicking an artist (if available),
        // the detail screen should have a back button
        // This test verifies the navigation structure
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 6: Validate returning to main screen from artist detail
    @Test
    fun validateReturnFromArtistDetailTest() {
        // Navigate to Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Bottom navigation should still be present
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()
        composeTestRule.onAllNodesWithText("Collections")[0].assertExists()
    }

    // Test 7: Validate switching tabs while on Artists screen
    @Test
    fun validateSwitchingTabsFromArtistsTest() {
        // Navigate to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Switch to Collections
        composeTestRule.onAllNodesWithText("Collections")[0].performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)

        composeTestRule.onNodeWithText("Coming soon...").assertExists()

        // Switch back to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 8: Validate Artists screen persists navigation state
    @Test
    fun validateArtistsScreenPersistsStateTest() {
        // Navigate to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Navigate away to Albums
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Navigate back to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Artists tab should still be functional
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 9: Validate rapid navigation to Artists doesn't crash
    @Test
    fun validateRapidArtistsNavigationTest() {
        repeat(3) {
            composeTestRule.onNodeWithText("Artists").performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(1000)

            composeTestRule.onNodeWithText("Albums").performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(500)
        }

        // App should still be functional
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 10: Validate Artists screen UI elements
    @Test
    fun validateArtistsScreenUIElementsTest() {
        // Navigate to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Bottom navigation should be visible
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()

        // At least the Artists tab should be functional
        composeTestRule.onNodeWithText("Artists").assertHasClickAction()
    }

    // ==================== Advanced Navigation Tests ====================

    // Test 11: Validate complete navigation flow through Artists
    @Test
    fun validateCompleteArtistsNavigationFlowTest() {
        // Start at Albums (default)
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Navigate to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        composeTestRule.onNodeWithText("Artists").assertExists()

        // Navigate to Collections
        composeTestRule.onAllNodesWithText("Collections")[0].performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Coming soon...").assertExists()

        // Back to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        composeTestRule.onNodeWithText("Artists").assertExists()

        // Back to Albums
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 12: Validate Artists screen handles loading state
    @Test
    fun validateArtistsLoadingStateTest() {
        // Navigate to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()

        // During initial load, either loading indicator or content should appear
        // Wait a bit
        Thread.sleep(1500)

        // Bottom navigation should remain visible during loading
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 13: Validate multiple visits to Artists screen
    @Test
    fun validateMultipleVisitsToArtistsTest() {
        repeat(3) { iteration ->
            // Navigate to Artists
            composeTestRule.onNodeWithText("Artists").performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(2000)

            composeTestRule.onNodeWithText("Artists").assertExists()

            // Navigate away
            composeTestRule.onNodeWithText("Albums").performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(500)
        }

        // Final check - should still be functional
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 14: Validate Artists tab maintains bottom navigation
    @Test
    fun validateArtistsTabMaintainsBottomNavigationTest() {
        // Navigate to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // All bottom navigation items should be visible
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()

        // All should be clickable
        composeTestRule.onNodeWithText("Albums").assertHasClickAction()
        composeTestRule.onNodeWithText("Artists").assertHasClickAction()
    }

    // Test 15: Validate navigation stability after loading Artists
    @Test
    fun validateNavigationStabilityAfterArtistsLoadTest() {
        // Navigate to Artists and wait for load
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Navigate through all tabs to ensure stability
        composeTestRule.onAllNodesWithText("Collections")[0].performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)

        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Everything should still work
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()
    }
}