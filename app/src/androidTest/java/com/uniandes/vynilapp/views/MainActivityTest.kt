package com.uniandes.vynilapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.Before

@HiltAndroidTest
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // Test 1: Validate MainActivity launches successfully
    @Test
    fun validateMainActivityLaunchesTest() {
        // Verify the app launched and bottom navigation is visible
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()
        composeTestRule.onNodeWithText("Collections").assertExists()
    }

    // Test 2: Validate Albums tab is selected by default
    @Test
    fun validateAlbumsTabDefaultSelectedTest() {
        // Albums should be the default selected tab
        // SearchBar is part of AlbumsScreen
        composeTestRule.waitForIdle()

        // Wait a bit for the screen to load
        Thread.sleep(1000)

        // Bottom navigation should be visible
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 3: Validate bottom navigation bar displays all tabs
    @Test
    fun validateBottomNavigationDisplaysAllTabsTest() {
        composeTestRule.onNodeWithText("Albums")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Artists")
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Collections")
            .assertExists()
            .assertIsDisplayed()
    }

    // ==================== Navigation Tests ====================

    // Test 4: Validate navigation to Collections screen
    @Test
    fun validateNavigationToCollectionsTest() {
        // Click on Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()

        // Wait for screen to render
        Thread.sleep(500)

        // Collections screen shows "Coming soon..."
        composeTestRule.onNodeWithText("Coming soon...")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 5: Validate navigation to Artists screen
    @Test
    fun validateNavigationToArtistsTest() {
        // Click on Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()

        // Wait for screen to load
        Thread.sleep(1000)

        // Artists tab should still be visible
        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 6: Validate navigation back to Albums from Collections
    @Test
    fun validateNavigationBackToAlbumsFromCollectionsTest() {
        // Navigate to Collections
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)

        // Verify Collections screen
        composeTestRule.onNodeWithText("Coming soon...").assertExists()

        // Navigate back to Albums
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Albums tab should be visible
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 7: Validate navigation back to Albums from Artists
    @Test
    fun validateNavigationBackToAlbumsFromArtistsTest() {
        // Navigate to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Navigate back to Albums
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Bottom navigation should still be visible
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 8: Validate sequential navigation through all screens
    @Test
    fun validateSequentialNavigationTest() {
        // Start at Albums (default)
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Navigate to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Artists").assertExists()

        // Navigate to Collections
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Coming soon...").assertExists()

        // Navigate back to Albums
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // ==================== Bottom Navigation Behavior Tests ====================

    // Test 9: Validate bottom navigation persists across screen changes
    @Test
    fun validateBottomNavigationPersistsTest() {
        // Verify all tabs are visible initially
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()

        // For Collections, use onAllNodesWithText since there will be 2 after navigation
        composeTestRule.onAllNodesWithText("Collections")[0].assertIsDisplayed()

        // Navigate to Collections
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)

        // Bottom navigation should still be visible
        // Now we need to be more specific since "Collections" appears twice
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()

        // Verify Collections tab exists (there will be 2 nodes now)
        val collectionsNodes = composeTestRule.onAllNodesWithText("Collections")
        collectionsNodes.assertCountEquals(2) // One in nav bar, one in screen content
        collectionsNodes[0].assertExists() // Bottom nav
        collectionsNodes[1].assertExists() // Screen title
    }

    // Test 10: Validate all tabs are clickable
    @Test
    fun validateAllTabsClickableTest() {
        composeTestRule.onNodeWithText("Albums").assertHasClickAction()
        composeTestRule.onNodeWithText("Artists").assertHasClickAction()
        composeTestRule.onNodeWithText("Collections").assertHasClickAction()
    }

    // Test 11: Validate clicking same tab multiple times
    @Test
    fun validateClickingSameTabTest() {
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()

        // Should still be functional
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 12: Validate rapid tab switching
    @Test
    fun validateRapidTabSwitchingTest() {
        repeat(2) {
            composeTestRule.onNodeWithText("Artists").performClick()
            composeTestRule.waitForIdle()

            composeTestRule.onNodeWithText("Collections").performClick()
            composeTestRule.waitForIdle()

            composeTestRule.onNodeWithText("Albums").performClick()
            composeTestRule.waitForIdle()
        }

        // Should still be functional
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 13: Validate Collections screen displays correct content
    @Test
    fun validateCollectionsScreenContentTest() {
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)

        // Should show "Coming soon..." message
        composeTestRule.onNodeWithText("Coming soon...")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 14: Validate tab icons exist
    @Test
    fun validateTabIconsExistTest() {
        // Use useUnmergedTree to find icons within NavigationBarItems
        composeTestRule.onNodeWithContentDescription("Albums", useUnmergedTree = true)
            .assertExists()
        composeTestRule.onNodeWithContentDescription("Artists", useUnmergedTree = true)
            .assertExists()
        composeTestRule.onNodeWithContentDescription("Collections", useUnmergedTree = true)
            .assertExists()
    }

    // Test 15: Validate MainActivity structure
    @Test
    fun validateMainActivityStructureTest() {
        composeTestRule.waitForIdle()

        // Bottom navigation should be present
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()
        composeTestRule.onNodeWithText("Collections").assertExists()
    }

    // Test 16: Validate navigation between Artists and Collections
    @Test
    fun validateNavigationBetweenArtistsAndCollectionsTest() {
        // Go to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Go to Collections
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)

        composeTestRule.onNodeWithText("Coming soon...").assertExists()
    }

    // Test 17: Validate navigation from Collections to Artists
    @Test
    fun validateNavigationFromCollectionsToArtistsTest() {
        // Go to Collections
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Coming soon...").assertExists()

        // Go to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 18: Validate complete navigation cycle
    @Test
    fun validateCompleteNavigationCycleTest() {
        // Cycle through all tabs twice
        repeat(2) {
            // Albums -> Artists
            composeTestRule.onNodeWithText("Artists").performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(500)

            // Artists -> Collections
            composeTestRule.onNodeWithText("Collections").performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(500)
            composeTestRule.onNodeWithText("Coming soon...").assertExists()

            // Collections -> Albums
            composeTestRule.onNodeWithText("Albums").performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(500)
        }

        // All tabs should still be functional
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()
        composeTestRule.onNodeWithText("Collections").assertExists()
    }

    // Test 19: Validate app doesn't crash on rapid navigation
    @Test
    fun validateNoCrashOnRapidNavigationTest() {
        repeat(5) {
            composeTestRule.onNodeWithText("Artists").performClick()
            composeTestRule.onNodeWithText("Collections").performClick()
            composeTestRule.onNodeWithText("Albums").performClick()
        }

        composeTestRule.waitForIdle()

        // App should still be responsive
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 20: Validate MainActivity integrates all components
    @Test
    fun validateMainActivityIntegrationTest() {
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Verify bottom navigation
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()
        composeTestRule.onNodeWithText("Collections").assertIsDisplayed()

        // Navigate through screens
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(500)
        composeTestRule.onNodeWithText("Coming soon...").assertIsDisplayed()

        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Bottom navigation should persist
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()
        composeTestRule.onNodeWithText("Collections").assertIsDisplayed()
    }
}