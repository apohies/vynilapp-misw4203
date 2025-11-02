package com.uniandes.vynilapp.views.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class OfflineErrorScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var retryClickCount = 0
    private var retryClicked = false

    @Before
    fun setup() {
        retryClickCount = 0
        retryClicked = false
    }

    // Test 1: Validate all UI components are displayed together
    @Test
    fun validateOfflineErrorScreenDisplaysAllElementsTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Verify icon with content description
        composeTestRule.onNodeWithContentDescription("No internet connection")
            .assertExists()
            .assertIsDisplayed()

        // Verify title text
        composeTestRule.onNodeWithText("Sin conexión")
            .assertExists()
            .assertIsDisplayed()

        // Verify message text
        composeTestRule.onNodeWithText(
            "Parece que no tienes conexión a internet.\nPor favor, verifica tu conexión e intenta de nuevo.",
            substring = true
        ).assertExists()
            .assertIsDisplayed()

        // Verify retry button
        composeTestRule.onNodeWithText("Reintentar")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 2: Validate offline icon is displayed
    @Test
    fun validateOfflineIconIsDisplayedTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Verify the CloudOff icon is present
        composeTestRule.onNodeWithContentDescription("No internet connection")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate title text is displayed correctly
    @Test
    fun validateTitleTextTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Verify the title "Sin conexión" is displayed
        composeTestRule.onNodeWithText("Sin conexión")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 4: Validate message text is displayed correctly
    @Test
    fun validateMessageTextTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Verify the error message is displayed
        composeTestRule.onNodeWithText("Parece que no tienes conexión a internet", substring = true)
            .assertExists()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Por favor, verifica tu conexión e intenta de nuevo", substring = true)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 5: Validate retry button is displayed
    @Test
    fun validateRetryButtonIsDisplayedTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Verify the "Reintentar" button is displayed
        composeTestRule.onNodeWithText("Reintentar")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 6: Validate retry button is clickable
    @Test
    fun validateRetryButtonIsClickableTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Verify the retry button has click action
        composeTestRule.onNodeWithText("Reintentar")
            .assertHasClickAction()
    }

    // Test 7: Validate retry button click triggers callback
    @Test
    fun validateRetryButtonClickTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(
                onRetry = {
                    retryClicked = true
                    retryClickCount++
                }
            )
        }

        // Click the retry button
        composeTestRule.onNodeWithText("Reintentar").performClick()

        // Verify the callback was triggered
        composeTestRule.waitForIdle()
        assertTrue(retryClicked)
        assertEquals(1, retryClickCount)
    }

    // Test 8: Validate multiple retry button clicks
    @Test
    fun validateMultipleRetryClicksTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(
                onRetry = {
                    retryClickCount++
                }
            )
        }

        // Click the retry button multiple times
        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()
        assertEquals(1, retryClickCount)

        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()
        assertEquals(2, retryClickCount)

        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()
        assertEquals(3, retryClickCount)
    }

    // Test 9: Validate screen layout structure
    @Test
    fun validateScreenLayoutStructureTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Verify all elements exist in the hierarchy
        composeTestRule.onNodeWithContentDescription("No internet connection").assertExists()
        composeTestRule.onNodeWithText("Sin conexión").assertExists()
        composeTestRule.onNodeWithText("Parece que no tienes conexión a internet", substring = true).assertExists()
        composeTestRule.onNodeWithText("Reintentar").assertExists()
    }

    // Test 10: Validate icon content description for accessibility
    @Test
    fun validateIconContentDescriptionTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Verify the icon has proper content description for accessibility
        composeTestRule.onNodeWithContentDescription("No internet connection", useUnmergedTree = true)
            .assertExists()
    }

    // Test 11: Validate text content is correct (exact match)
    @Test
    fun validateExactTextContentTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Verify exact title
        composeTestRule.onNode(
            hasText("Sin conexión") and hasTextExactly("Sin conexión")
        ).assertExists()

        // Verify exact button text
        composeTestRule.onNode(
            hasText("Reintentar") and hasTextExactly("Reintentar")
        ).assertExists()
    }

    // Test 12: Validate all text elements are present together
    @Test
    fun validateAllTextElementsPresentTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // Count all text nodes
        val titleNodes = composeTestRule.onAllNodesWithText("Sin conexión", substring = false)
            .fetchSemanticsNodes()
        assertEquals(1, titleNodes.size)

        val buttonNodes = composeTestRule.onAllNodesWithText("Reintentar", substring = false)
            .fetchSemanticsNodes()
        assertEquals(1, buttonNodes.size)
    }

    // Test 13: Validate retry callback with state change
    @Test
    fun validateRetryCallbackWithStateChangeTest() {
        var connectionState = false

        composeTestRule.setContent {
            OfflineErrorScreen(
                onRetry = {
                    connectionState = true
                }
            )
        }

        // Initial state should be false
        assertEquals(false, connectionState)

        // Click retry button
        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()

        // Verify state changed
        assertEquals(true, connectionState)
    }

    // Test 14: Validate rapid retry clicks
    @Test
    fun validateRapidRetryClicksTest() {
        composeTestRule.setContent {
            OfflineErrorScreen(
                onRetry = {
                    retryClickCount++
                }
            )
        }

        // Rapidly click the retry button
        repeat(5) {
            composeTestRule.onNodeWithText("Reintentar").performClick()
        }
        composeTestRule.waitForIdle()

        // Verify all clicks were registered
        assertEquals(5, retryClickCount)
    }

    // Test 15: Validate screen renders without errors
    @Test
    fun validateScreenRendersWithoutErrorsTest() {
        // This test ensures the composable renders without throwing exceptions
        composeTestRule.setContent {
            OfflineErrorScreen(onRetry = {})
        }

        // If we get here without exception, the test passes
        composeTestRule.onNodeWithText("Sin conexión").assertExists()
    }

    // Test 16: Validate button interaction flow
    @Test
    fun validateButtonInteractionFlowTest() {
        var interactionCount = 0
        var lastInteractionTime = 0L

        composeTestRule.setContent {
            OfflineErrorScreen(
                onRetry = {
                    interactionCount++
                    lastInteractionTime = System.currentTimeMillis()
                }
            )
        }

        val startTime = System.currentTimeMillis()

        // First click
        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()
        assertEquals(1, interactionCount)
        assertTrue(lastInteractionTime >= startTime)

        // Second click
        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()
        assertEquals(2, interactionCount)
    }
}