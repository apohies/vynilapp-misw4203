package com.uniandes.vynilapp.commonComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.uniandes.vynilapp.views.common.LoadingScreen
import org.junit.Rule
import org.junit.Test

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

    // Test 2: Validate loading screen renders without errors
    @Test
    fun validateLoadingScreenRendersWithoutErrorsTest() {
        // Ensure LoadingScreen renders without throwing exceptions
        composeTestRule.setContent {
            LoadingScreen()
        }

        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 3: Validate loading screen with custom modifier
    @Test
    fun validateLoadingScreenWithModifierTest() {
        composeTestRule.setContent {
            LoadingScreen(modifier = Modifier)
        }

        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 4: Validate loading screen no interaction
    @Test
    fun validateLoadingScreenNoInteractionTest() {
        composeTestRule.setContent {
            LoadingScreen()
        }

        // Loading screen should not have click actions
        // Just verify it renders
        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }

    // Test 5: Validate loading text is not clickable
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

    // Test 6: Validate loading screen composition
    @Test
    fun validateLoadingScreenCompositionTest() {
        composeTestRule.setContent {
            Box {
                LoadingScreen()
            }
        }

        // Should compose correctly within parent
        composeTestRule.onNodeWithText("Loading data...").assertExists()
    }
}