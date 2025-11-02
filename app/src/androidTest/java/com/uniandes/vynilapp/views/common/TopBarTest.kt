package com.uniandes.vynilapp.views.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

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

    // Test 4: Validate back button is clickable
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

    // Test 5: Validate back button click triggers callback
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
        assertTrue(backClicked)
        assertEquals(1, backClickCount)
    }

    // Test 6: Validate multiple back button clicks
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
        assertEquals(1, backClickCount)

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        assertEquals(2, backClickCount)

        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        assertEquals(3, backClickCount)
    }

    // Test 7: Validate different title texts
    @Test
    fun validateDifferentTitlesTest() {
        val title1 = "Artist Details"

        composeTestRule.setContent {
            TopBar(title = title1, onBack = {})
        }

        composeTestRule.onNodeWithText(title1).assertExists()
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

    // Test 10: Validate title with special characters
    @Test
    fun validateTitleWithSpecialCharactersTest() {
        val specialTitle = "Album's Details & Info!"

        composeTestRule.setContent {
            TopBar(
                title = specialTitle,
                onBack = {}
            )
        }

        composeTestRule.onNodeWithText(specialTitle)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 11: Validate rapid back button clicks
    @Test
    fun validateRapidBackClicksTest() {
        composeTestRule.setContent {
            TopBar(
                title = "Test",
                onBack = {
                    backClickCount++
                }
            )
        }

        // Rapidly click back button
        repeat(5) {
            composeTestRule.onNodeWithContentDescription("Back").performClick()
        }
        composeTestRule.waitForIdle()

        // Verify all clicks were registered
        assertEquals(5, backClickCount)
    }

    // Test 12: Validate TopBar structure
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

    // Test 13: Validate back callback with state change
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
        assertEquals("initial", navigationState)

        // Click back
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()

        // Verify state changed
        assertEquals("back_pressed", navigationState)
    }

    // Test 14: Validate TopBar renders without errors
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

    // Test 15: Validate exact title content
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

    // Test 16: Validate only one back button exists
    @Test
    fun validateSingleBackButtonTest() {
        composeTestRule.setContent {
            TopBar(
                title = "Test",
                onBack = {}
            )
        }

        // Verify exactly one back button
        val backButtons = composeTestRule.onAllNodesWithContentDescription("Back")
            .fetchSemanticsNodes()
        assertEquals(1, backButtons.size)
    }

    // Test 17: Validate only one title exists
    @Test
    fun validateSingleTitleTest() {
        val title = "Unique Title"

        composeTestRule.setContent {
            TopBar(
                title = title,
                onBack = {}
            )
        }

        // Verify exactly one title
        val titleNodes = composeTestRule.onAllNodesWithText(title, substring = false)
            .fetchSemanticsNodes()
        assertEquals(1, titleNodes.size)
    }

    // Test 18: Validate callback interaction flow
    @Test
    fun validateCallbackInteractionFlowTest() {
        var lastClickTime = 0L
        var clickSequence = mutableListOf<Int>()

        composeTestRule.setContent {
            TopBar(
                title = "Flow Test",
                onBack = {
                    lastClickTime = System.currentTimeMillis()
                    clickSequence.add(clickSequence.size + 1)
                }
            )
        }

        val startTime = System.currentTimeMillis()

        // First click
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        assertEquals(listOf(1), clickSequence)
        assertTrue(lastClickTime >= startTime)

        // Second click
        composeTestRule.onNodeWithContentDescription("Back").performClick()
        composeTestRule.waitForIdle()
        assertEquals(listOf(1, 2), clickSequence)
    }
}