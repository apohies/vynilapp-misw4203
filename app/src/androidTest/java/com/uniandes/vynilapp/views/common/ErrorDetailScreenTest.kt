package com.uniandes.vynilapp.views.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class ErrorDetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var retryClickCount = 0
    private var retryClicked = false

    @Before
    fun setup() {
        retryClickCount = 0
        retryClicked = false
    }

    // Test 1: Validate ErrorScreen displays all elements
    @Test
    fun validateErrorScreenDisplaysAllElementsTest() {
        val errorMessage = "Network error occurred"

        composeTestRule.setContent {
            ErrorScreen(
                error = errorMessage,
                onRetry = {}
            )
        }

        // Verify error message is displayed
        composeTestRule.onNodeWithText(errorMessage)
            .assertExists()
            .assertIsDisplayed()

        // Verify retry button is displayed
        composeTestRule.onNodeWithText("Reintentar")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 2: Validate error message is displayed
    @Test
    fun validateErrorMessageDisplayTest() {
        val errorText = "Failed to load data"

        composeTestRule.setContent {
            ErrorScreen(
                error = errorText,
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(errorText)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate retry button is displayed
    @Test
    fun validateRetryButtonDisplayTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Error",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Reintentar")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 4: Validate retry button is clickable
    @Test
    fun validateRetryButtonIsClickableTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Error",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Reintentar")
            .assertHasClickAction()
    }

    // Test 5: Validate retry button click triggers callback
    @Test
    fun validateRetryButtonClickTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Error",
                onRetry = {
                    retryClicked = true
                    retryClickCount++
                }
            )
        }

        composeTestRule.onNodeWithText("Reintentar").performClick()

        composeTestRule.waitForIdle()
        assertTrue(retryClicked)
        assertEquals(1, retryClickCount)
    }

    // Test 6: Validate multiple retry clicks
    @Test
    fun validateMultipleRetryClicksTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Error",
                onRetry = { retryClickCount++ }
            )
        }

        repeat(3) {
            composeTestRule.onNodeWithText("Reintentar").performClick()
        }

        composeTestRule.waitForIdle()
        assertEquals(3, retryClickCount)
    }

    // Test 7: Validate long error message
    @Test
    fun validateLongErrorMessageTest() {
        val longError = "This is a very long error message that explains in detail what went wrong with the network request and why it failed to complete successfully"

        composeTestRule.setContent {
            ErrorScreen(
                error = longError,
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(longError, substring = true)
            .assertExists()
    }

    // Test 8: Validate Spanish error message
    @Test
    fun validateSpanishErrorMessageTest() {
        val spanishError = "Error al cargar los datos"

        composeTestRule.setContent {
            ErrorScreen(
                error = spanishError,
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(spanishError)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 9: Validate empty error message
    @Test
    fun validateEmptyErrorMessageTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "",
                onRetry = {}
            )
        }

        // Retry button should still be displayed
        composeTestRule.onNodeWithText("Reintentar")
            .assertExists()
    }

    // Test 10: Validate error screen structure
    @Test
    fun validateErrorScreenStructureTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Structure test error",
                onRetry = {}
            )
        }

        // Both error message and button should exist
        composeTestRule.onNodeWithText("Structure test error").assertExists()
        composeTestRule.onNodeWithText("Reintentar").assertExists()
    }

    // Test 11: Validate retry callback with state change
    @Test
    fun validateRetryCallbackWithStateChangeTest() {
        var loadingState = false

        composeTestRule.setContent {
            ErrorScreen(
                error = "Error",
                onRetry = {
                    loadingState = true
                }
            )
        }

        assertEquals(false, loadingState)

        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()

        assertEquals(true, loadingState)
    }

    // Test 12: Validate rapid retry clicks
    @Test
    fun validateRapidRetryClicksTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Error",
                onRetry = { retryClickCount++ }
            )
        }

        repeat(10) {
            composeTestRule.onNodeWithText("Reintentar").performClick()
        }

        composeTestRule.waitForIdle()
        assertEquals(10, retryClickCount)
    }

    // Test 13: Validate error screen renders without errors
    @Test
    fun validateErrorScreenRendersWithoutErrorsTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Test error",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Test error").assertExists()
    }

    // Test 14: Validate exact button text
    @Test
    fun validateExactButtonTextTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Error",
                onRetry = {}
            )
        }

        composeTestRule.onNode(
            hasText("Reintentar") and hasTextExactly("Reintentar")
        ).assertExists()
    }

    // Test 15: Validate only one retry button
    @Test
    fun validateSingleRetryButtonTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Error",
                onRetry = {}
            )
        }

        val retryButtons = composeTestRule.onAllNodesWithText("Reintentar")
            .fetchSemanticsNodes()
        assertEquals(1, retryButtons.size)
    }

    // Test 16: Validate both elements visible together
    @Test
    fun validateBothElementsVisibleTogetherTest() {
        composeTestRule.setContent {
            ErrorScreen(
                error = "Visible error",
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText("Visible error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reintentar").assertIsDisplayed()
    }

    // Test 17: Validate network error message
    @Test
    fun validateNetworkErrorMessageTest() {
        val networkError = "No internet connection available"

        composeTestRule.setContent {
            ErrorScreen(
                error = networkError,
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(networkError).assertExists()
    }

    // Test 18: Validate server error message
    @Test
    fun validateServerErrorMessageTest() {
        val serverError = "500 Internal Server Error"

        composeTestRule.setContent {
            ErrorScreen(
                error = serverError,
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(serverError).assertExists()
    }

    // Test 19: Validate callback interaction flow
    @Test
    fun validateCallbackInteractionFlowTest() {
        val retrySequence = mutableListOf<Int>()

        composeTestRule.setContent {
            ErrorScreen(
                error = "Flow test",
                onRetry = {
                    retrySequence.add(retrySequence.size + 1)
                }
            )
        }

        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()
        assertEquals(listOf(1), retrySequence)

        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()
        assertEquals(listOf(1, 2), retrySequence)
    }

    // Test 20: Validate error with special characters
    @Test
    fun validateErrorWithSpecialCharactersTest() {
        val specialError = "Error: Failed to connect! (404)"

        composeTestRule.setContent {
            ErrorScreen(
                error = specialError,
                onRetry = {}
            )
        }

        composeTestRule.onNodeWithText(specialError).assertExists()
    }
}