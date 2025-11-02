package com.uniandes.vynilapp.commonComponents

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilapp.views.common.ErrorScreen
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    // Test 2: Validate multiple retry clicks
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
        Assert.assertEquals(3, retryClickCount)
    }

    // Test 3: Validate long error message
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

    // Test 4: Validate Spanish error message
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

    // Test 5: Validate empty error message
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

    // Test 6: Validate error screen structure
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

    // Test 7: Validate retry callback with state change
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

        Assert.assertEquals(false, loadingState)

        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()

        Assert.assertEquals(true, loadingState)
    }

    // Test 8: Validate rapid retry clicks
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
        Assert.assertEquals(10, retryClickCount)
    }

    // Test 9: Validate error screen renders without errors
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

    // Test 10: Validate exact button text
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

    // Test 11: Validate both elements visible together
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

    // Test 12: Validate network error message
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

    // Test 13: Validate server error message
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

    // Test 14: Validate callback interaction flow
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
        Assert.assertEquals(listOf(1), retrySequence)

        composeTestRule.onNodeWithText("Reintentar").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(listOf(1, 2), retrySequence)
    }

    // Test 15: Validate error with special characters
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