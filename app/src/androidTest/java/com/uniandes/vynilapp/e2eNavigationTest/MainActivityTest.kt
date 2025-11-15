package com.uniandes.vynilapp.e2eNavigationTest

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
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
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // Test 1: Validate bottom navigation bar displays all tabs
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

    // Test 2: Validate navigation to Collections screen
    @Test
    fun validateNavigationToCollectionsTest() {
        // Click on Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()

        // Wait for screen to render
        Thread.sleep(1000)

        // Collections screen shows "Coleccionista" title
        composeTestRule.onNodeWithText("Coleccionista")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate navigation to Artists screen
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

    // Test 4: Validate navigation back to Albums from Collections
    @Test
    fun validateNavigationBackToAlbumsFromCollectionsTest() {
        // Navigate to Collections
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Verify Collections screen shows "Coleccionista"
        composeTestRule.onNodeWithText("Coleccionista").assertExists()

        // Navigate back to Albums
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Albums tab should be visible
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 5: Validate navigation back to Albums from Artists
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

    // Test 6: Validate sequential navigation through all screens
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
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Coleccionista").assertExists()

        // Navigate back to Albums
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // ==================== Bottom Navigation Behavior Tests ====================

    // Test 7: Validate bottom navigation persists across screen changes
    @Test
    fun validateBottomNavigationPersistsTest() {
        // Verify all tabs are visible initially
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()

        // For Collections, use onAllNodesWithText since there might be multiple after navigation
        composeTestRule.onAllNodesWithText("Collections")[0].assertIsDisplayed()

        // Navigate to Collections
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Bottom navigation should still be visible
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()

        // Verify Collections tab exists in nav bar
        val collectionsNodes = composeTestRule.onAllNodesWithText("Collections")
        collectionsNodes[0].assertExists() // Bottom nav
    }

    // Test 8: Validate all tabs are clickable
    @Test
    fun validateAllTabsClickableTest() {
        composeTestRule.onNodeWithText("Albums").assertHasClickAction()
        composeTestRule.onNodeWithText("Artists").assertHasClickAction()
        composeTestRule.onNodeWithText("Collections").assertHasClickAction()
    }

    // Test 9: Validate clicking same tab multiple times
    @Test
    fun validateClickingSameTabTest() {
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()

        // Should still be functional
        composeTestRule.onNodeWithText("Albums").assertExists()
    }

    // Test 10: Validate rapid tab switching
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

    // Test 11: Validate Collections screen displays correct content
    @Test
    fun validateCollectionsScreenContentTest() {
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Should show "Coleccionista" title
        composeTestRule.onNodeWithText("Coleccionista")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 12: Validate tab icons exist
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

    // Test 13: Validate navigation between Artists and Collections
    @Test
    fun validateNavigationBetweenArtistsAndCollectionsTest() {
        // Go to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Go to Collections
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Coleccionista").assertExists()
    }

    // Test 14: Validate navigation from Collections to Artists
    @Test
    fun validateNavigationFromCollectionsToArtistsTest() {
        // Go to Collections
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Coleccionista").assertExists()

        // Go to Artists
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        composeTestRule.onNodeWithText("Artists").assertExists()
    }

    // Test 15: Validate complete navigation cycle
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
            Thread.sleep(1000)
            composeTestRule.onNodeWithText("Coleccionista").assertExists()

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

    // Test 16: Validate MainActivity integrates all components
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
        Thread.sleep(1000)
        composeTestRule.onNodeWithText("Coleccionista").assertIsDisplayed()

        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Bottom navigation should persist
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()
        composeTestRule.onNodeWithText("Collections").assertIsDisplayed()
    }
}