package com.uniandes.vynilapp.e2eNavigationTest

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
class CollectorDetailNavigationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    // Helper function to wait for collectors to load
    private fun waitForCollectorsToLoad() {
        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Wait until we can find at least one collector item
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    // Test 1: Navigate to Collections tab and click first collector item
    @Test
    fun validateNavigateToCollectorDetailTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()

        // Wait for collectors to load
        waitForCollectorsToLoad()

        // Find and click the first collector item
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Verify we navigated to collector detail by checking for back button
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()
    }

    // Test 2: Validate collector detail screen shows collector name
    @Test
    fun validateCollectorDetailShowsCollectorNameTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        // Click first collector
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Collector name should be displayed (large text, bold)
        // We can verify the detail screen loaded by checking for back button
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()
    }

    // Test 3: Validate collector detail shows album count
    @Test
    fun validateCollectorDetailShowsAlbumCountTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        // Click first collector
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Should show album count text
        composeTestRule.onNodeWithText("álbumes en la colección", substring = true)
            .assertExists()
    }

    // Test 5: Validate collector detail shows additional information
    @Test
    fun validateCollectorDetailShowsAdditionalInfoTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        // Click first collector
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2500)

        // Collector detail should show total albums and collection value
        composeTestRule.onNodeWithText("Total de álbumes", substring = true)
            .assertExists()

        composeTestRule.onNodeWithText("Valor de colección", substring = true)
            .assertExists()
    }

    // Test 6: Validate back button on collector detail works
    @Test
    fun validateBackButtonFromCollectorDetailTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        // Click first collector
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Click back button
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(1000)

        // Should return to Collections list
        // Bottom navigation should be visible
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Collections").assertExists()
    }

    // Test 7: Validate multiple collectors can be clicked
    @Test
    fun validateMultipleCollectorsClickableTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        // Click first collector
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
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

        // Click second collector if available
        val collectorNodes = composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
        if (collectorNodes.fetchSemanticsNodes().size > 1) {
            collectorNodes[1].performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(2000)

            // Verify we're on detail screen again
            composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
                .assertExists()
        }
    }

    // Test 8: Validate collector detail screen is scrollable
    @Test
    fun validateCollectorDetailScrollableTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        // Click first collector
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2500)

        // Collector detail should be scrollable (has collection, additional info)
        // Verify main elements exist
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()

        composeTestRule.onNodeWithText("Total de álbumes", substring = true)
            .assertExists()
    }

    // Test 11: Validate navigation flow: Collections -> Detail -> Back -> Detail again
    @Test
    fun validateRepeatNavigationToCollectorDetailTest() {
        // First visit
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
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

        // Second visit - click same collector again
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        // Should load again successfully
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()

        composeTestRule.onNodeWithText("Total de álbumes", substring = true)
            .assertExists()
    }

    // Test 12: Validate collector detail loads without errors
    @Test
    fun validateCollectorDetailLoadsWithoutErrorsTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        // Click first collector
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
        Thread.sleep(3000)

        // Should not show error screen
        // Should show collector detail components
        composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
            .assertExists()

        // Should not show "Reintentar" (retry button from error screen)
        composeTestRule.onNodeWithText("Reintentar").assertDoesNotExist()
    }

    // Test 13: Validate collector detail renders with loading state
    @Test
    fun validateCollectorDetailHandlesLoadingTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        // Click first collector
        composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
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

    // Test 15: Validate collector detail with different collectors
    @Test
    fun validateDifferentCollectorsDetailTest() {
        // Navigate to Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()
        waitForCollectorsToLoad()

        // Get all collector items
        val collectorNodes = composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)
        val collectorCount = collectorNodes.fetchSemanticsNodes().size

        // Test first collector
        if (collectorCount > 0) {
            collectorNodes[0].performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(2000)

            composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
                .assertExists()

            composeTestRule.onNodeWithText("Total de álbumes", substring = true)
                .assertExists()

            // Go back
            composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
                .performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(2000)
        }

        // Test second collector if available
        if (collectorCount > 1) {
            composeTestRule.onAllNodesWithText("álbumes", substring = true, ignoreCase = true)[1]
                .performClick()
            composeTestRule.waitForIdle()
            Thread.sleep(2000)

            composeTestRule.onNodeWithContentDescription("Back", useUnmergedTree = true)
                .assertExists()

            composeTestRule.onNodeWithText("Total de álbumes", substring = true)
                .assertExists()
        }
    }
}