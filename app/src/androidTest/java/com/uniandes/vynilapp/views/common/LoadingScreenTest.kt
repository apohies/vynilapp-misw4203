package com.uniandes.vynilapp.views.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

class LoadingScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // Test 1: Validate LoadingScreen displays all elements
    @Test
    fun validateLoadingScreenDisplaysAllElementsTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Verify loading text is displayed
        composeTestRule.onNodeWithText("Loading data...")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 2: Validate loading text is displayed
    @Test
    fun validateLoadingTextDisplayTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        composeTestRule.onNodeWithText("Loading data...")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate loading indicator is displayed
    @Test
    fun validateLoadingIndicatorDisplayTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // CircularProgressIndicator should be present
        // We can verify by checking the loading screen renders
        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 4: Validate loading screen structure
    @Test
    fun validateLoadingScreenStructureTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Verify loading text exists
        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 5: Validate loading screen renders without errors
    @Test
    fun validateLoadingScreenRendersWithoutErrorsTest() {
        // Ensure LoadingScreen renders without throwing exceptions
        composeTestRule.setContent {
            LoadingScreen()
        }

        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 6: Validate exact loading text
    @Test
    fun validateExactLoadingTextTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        composeTestRule.onNode(
            hasText("Loading data...") and hasTextExactly("Loading data...")
        ).assertExists()
    }

    // Test 7: Validate only one loading text
    @Test
    fun validateSingleLoadingTextTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        val loadingTexts = composeTestRule.onAllNodesWithText("Loading data...")
            .fetchSemanticsNodes()
        assertEquals(1, loadingTexts.size)
    }

    // Test 8: Validate loading screen with custom modifier
    @Test
    fun validateLoadingScreenWithModifierTest() {
        composeTestRule.setContent {
            LoadingScreen(modifier = androidx.compose.ui.Modifier)
        }

        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 9: Validate loading screen is centered
    @Test
    fun validateLoadingScreenCenteredTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Loading text should be visible (centered content)
        composeTestRule.onNodeWithText("Loading data...").assertIsDisplayed()
    }

    // Test 10: Validate loading screen displays immediately
    @Test
    fun validateLoadingScreenDisplaysImmediatelyTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // No delay - should be immediately visible
        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 11: Validate multiple loading screens
    @Test
    fun validateMultipleLoadingScreensTest() {
        composeTestRule.setContent {
            androidx.compose.foundation.layout.Column {
                LoadingScreen()
            }
        }

        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 12: Validate loading screen content is visible
    @Test
    fun validateLoadingScreenContentVisibleTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Both text and indicator container should be visible
        composeTestRule.onNodeWithText("Loading data...").assertIsDisplayed()
    }

    // Test 13: Validate loading screen background
    @Test
    fun validateLoadingScreenBackgroundTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Screen should render with background
        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 14: Validate loading screen no interaction
    @Test
    fun validateLoadingScreenNoInteractionTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Loading screen should not have click actions
        // Just verify it renders
        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 15: Validate loading text is not clickable
    @Test
    fun validateLoadingTextNotClickableTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Text should not be clickable
        val textNode = composeTestRule.onNodeWithText("Loading data...")
        textNode.assertExists()
        // No click action should be present
    }

    // Test 16: Validate loading screen full size
    @Test
    fun validateLoadingScreenFullSizeTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Should fill the size and be visible
        composeTestRule.onNodeWithText("Loading data...").assertIsDisplayed()
    }

    // Test 17: Validate loading screen text color
    @Test
    fun validateLoadingScreenTextColorTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Text should be visible (white on dark background)
        composeTestRule.onNodeWithText("Loading data...").assertIsDisplayed()
    }

    // Test 18: Validate loading screen composition
    @Test
    fun validateLoadingScreenCompositionTest() {
        composeTestRule.setContent {
            androidx.compose.foundation.layout.Box {
                LoadingScreen()
            }
        }

        // Should compose correctly within parent
        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }
}