package com.uniandes.vynilapp.commonComponents

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilapp.views.common.TopBar
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TopBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var backClickCount = 0
    private var backClicked = false

    @Before
    fun setup() {
        backClickCount = 0
        backClicked = false
    }

    // Test 1: Validate TopBar displays all elements
    @Test
    fun validateTopBarDisplaysAllElementsTest() {
        val testTitle = "Test Title"

        composeTestRule.setContent {
            TopBar(
                title = testTitle,
                onBack = {}
            )
        }

        // Verify back icon is displayed
        composeTestRule.onNodeWithContentDescription("Back")
            .assertExists()
            .assertIsDisplayed()

        // Verify title is displayed
        composeTestRule.onNodeWithText(testTitle)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 2: Validate title text is displayed correctly
    @Test
    fun validateTitleDisplayTest() {
        val expectedTitle = "Album Details"

        composeTestRule.setContent {
            TopBar(
                title = expectedTitle,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText(expectedTitle)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate back button icon is displayed
    @Test
    fun validateBackButtonIconDisplayTest() {
        composeTestRule.setContent {
            TopBar(
                title = "Test",
                onBack = {}
            )
        }

        // Verify back arrow icon with content description
        composeTestRule.onNodeWithContentDescription("Back")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 5: Validate back button is clickable
    @Test
    fun validateBackButtonIsClickableTest() {
        composeTestRule.setContent {
            TopBar(
                title = "Test",
                onBack = {}
            )
        }

        // Verify back button has click action
        composeTestRule.onNodeWithContentDescription("Back")
            .assertHasClickAction()
    }

    // Test 6: Validate back button click triggers callback
    @Test
    fun validateBackButtonClickTest() {
        composeTestRule.setContent {
            TopBar(
                title = "Test",
                onBack = {
                    backClicked = true
                    backClickCount++
                }
            )
        }

        // Click the back button
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Verify callback was triggered
        composeTestRule.waitForIdle()
        Assert.assertTrue(backClicked)
        Assert.assertEquals(1, backClickCount)
    }

    // Test 7: Validate multiple back button clicks
    @Test
    fun validateMultipleBackClicksTest() {
        composeTestRule.setContent {
            TopBar(
                title = "Test",
                onBack = {
                    backClickCount++
                }
            )
        }

        // Click back button multiple times
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(1, backClickCount)

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(2, backClickCount)

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(3, backClickCount)
    }

    // Test 8: Validate long title text
    @Test
    fun validateLongTitleTest() {
        val longTitle = "This is a very long title that might wrap or truncate"

        composeTestRule.setContent {
            TopBar(
                title = longTitle,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText(longTitle)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 9: Validate empty title
    @Test
    fun validateEmptyTitleTest() {
        composeTestRule.setContent {
            TopBar(
                title = "",
                onBack = {}
            )
        }

        // Back button should still be displayed
        composeTestRule.onNodeWithContentDescription("Back")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 10: Validate TopBar structure
    @Test
    fun validateTopBarStructureTest() {
        composeTestRule.setContent {
            TopBar(
                title = "Structure Test",
                onBack = {}
            )
        }

        // Verify both back button and title exist
        composeTestRule.onNodeWithContentDescription("Back").assertExists()
        composeTestRule.onNodeWithText("Structure Test").assertExists()
    }

    // Test 11: Validate back callback with state change
    @Test
    fun validateBackCallbackWithStateChangeTest() {
        var navigationState = "initial"

        composeTestRule.setContent {
            TopBar(
                title = "Test",
                onBack = {
                    navigationState = "back_pressed"
                }
            )
        }

        // Initial state
        Assert.assertEquals("initial", navigationState)

        // Click back
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()

        // Verify state changed
        Assert.assertEquals("back_pressed", navigationState)
    }

    // Test 12: Validate TopBar renders without errors
    @Test
    fun validateTopBarRendersWithoutErrorsTest() {
        // Ensure TopBar renders without throwing exceptions
        composeTestRule.setContent {
            TopBar(
                title = "Error Test",
                onBack = {}
            )
        }

        // If we get here, rendering was successful
        composeTestRule.onNodeWithContentDescription("Back").assertExists()
    }

    // Test 13: Validate exact title content
    @Test
    fun validateExactTitleContentTest() {
        val exactTitle = "Exact Match Test"

        composeTestRule.setContent {
            TopBar(
                title = exactTitle,
                onBack = {}
            )
        }

        // Verify exact text match
        composeTestRule.onNode(
            hasText(exactTitle) and hasTextExactly(exactTitle)
        ).assertExists()
    }
}