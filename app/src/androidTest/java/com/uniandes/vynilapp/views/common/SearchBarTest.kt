package com.uniandes.vynilapp.views.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class SearchBarTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var currentValue = ""
    private var valueChangeCount = 0

    @Before
    fun setup() {
        currentValue = ""
        valueChangeCount = 0
    }

    // Test 1: Validate SearchBar displays all elements
    @Test
    fun validateSearchBarDisplaysAllElementsTest() {
        composeTestRule.setContent {
            SearchBar(
                value = "",
                onValueChange = {},
                placeholder = "Find in albums"
            )
        }

        // Verify search icon is displayed
        composeTestRule.onNodeWithContentDescription("Search")
            .assertExists()
            .assertIsDisplayed()

        // Verify placeholder is displayed when empty
        composeTestRule.onNodeWithText("Find in albums")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 2: Validate search icon is displayed
    @Test
    fun validateSearchIconDisplayTest() {
        composeTestRule.setContent {
            SearchBar(
                value = "",
                onValueChange = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Search")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate default placeholder
    @Test
    fun validateDefaultPlaceholderTest() {
        composeTestRule.setContent {
            SearchBar(
                value = "",
                onValueChange = {}
            )
        }

        // Default placeholder should be "Find in albums"
        composeTestRule.onNodeWithText("Find in albums")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 4: Validate custom placeholder
    @Test
    fun validateCustomPlaceholderTest() {
        val customPlaceholder = "Search artists..."

        composeTestRule.setContent {
            SearchBar(
                value = "",
                onValueChange = {},
                placeholder = customPlaceholder
            )
        }

        composeTestRule.onNodeWithText(customPlaceholder)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 5: Validate placeholder hides when value is entered
    @Test
    fun validatePlaceholderHidesWithValueTest() {
        composeTestRule.setContent {
            SearchBar(
                value = "Test Value",
                onValueChange = {},
                placeholder = "Find in albums"
            )
        }

        // Placeholder should not be visible when there's a value
        composeTestRule.onNodeWithText("Find in albums")
            .assertDoesNotExist()
    }

    // Test 6: Validate text input is displayed
    @Test
    fun validateTextInputDisplayTest() {
        val testValue = "Search Query"

        composeTestRule.setContent {
            SearchBar(
                value = testValue,
                onValueChange = {}
            )
        }

        composeTestRule.onNodeWithText(testValue)
            .assertExists()
    }

    // Test 7: Validate text input can be focused
    @Test
    fun validateTextInputFocusTest() {
        composeTestRule.setContent {
            SearchBar(
                value = "",
                onValueChange = {}
            )
        }

        // Find the text field and verify it exists
        composeTestRule.onNode(hasSetTextAction())
            .assertExists()
    }

    // Test 8: Validate onValueChange callback
    @Test
    fun validateOnValueChangeCallbackTest() {
        composeTestRule.setContent {
            SearchBar(
                value = currentValue,
                onValueChange = { newValue ->
                    valueChangeCount++
                }
            )
        }

        // Perform text input
        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Test")

        composeTestRule.waitForIdle()
        assertTrue(valueChangeCount > 0)
    }

    // Test 9: Validate multiple text inputs
    @Test
    fun validateMultipleTextInputsTest() {
        composeTestRule.setContent {
            SearchBar(
                value = currentValue,
                onValueChange = { newValue ->
                    currentValue = currentValue + newValue
                }
            )
        }

        // Input text
        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Hello")

        composeTestRule.waitForIdle()
        assertEquals("Hello", currentValue)

        // Input more text (it appends)
        composeTestRule.onNode(hasSetTextAction())
            .performTextInput(" World")

        composeTestRule.waitForIdle()
        assertEquals("Hello World", currentValue)
    }

    // Test 10: Validate text replacement
    @Test
    fun validateTextReplacementTest() {
        composeTestRule.setContent {
            SearchBar(
                value = currentValue,
                onValueChange = { currentValue = it }
            )
        }

        // Input initial text
        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Initial")

        composeTestRule.waitForIdle()

        // Clear and replace
        composeTestRule.onNode(hasSetTextAction())
            .performTextClearance()

        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Replaced")

        assertEquals("Replaced", currentValue)
    }

    // Test 11: Validate empty value shows placeholder
    @Test
    fun validateEmptyValueShowsPlaceholderTest() {
        composeTestRule.setContent {
            SearchBar(
                value = "",
                onValueChange = {},
                placeholder = "Type here..."
            )
        }

        composeTestRule.onNodeWithText("Type here...")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 12: Validate SearchBar structure
    @Test
    fun validateSearchBarStructureTest() {
        composeTestRule.setContent {
            SearchBar(
                value = "",
                onValueChange = {},
                placeholder = "Search"
            )
        }

        // Verify search icon exists
        composeTestRule.onNodeWithContentDescription("Search").assertExists()

        // Verify text input exists
        composeTestRule.onNode(hasSetTextAction()).assertExists()
    }

    // Test 13: Validate SearchBar renders without errors
    @Test
    fun validateSearchBarRendersWithoutErrorsTest() {
        // Ensure SearchBar renders without throwing exceptions
        composeTestRule.setContent {
            SearchBar(
                value = "Test",
                onValueChange = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Search").assertExists()
    }

    // Test 14: Validate Spanish placeholder
    @Test
    fun validateSpanishPlaceholderTest() {
        val spanishPlaceholder = "Buscar en álbumes"

        composeTestRule.setContent {
            SearchBar(
                value = "",
                onValueChange = {},
                placeholder = spanishPlaceholder
            )
        }

        composeTestRule.onNodeWithText(spanishPlaceholder)
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 15: Validate special characters in search
    @Test
    fun validateSpecialCharactersTest() {
        val specialText = "Rock & Roll!"

        composeTestRule.setContent {
            SearchBar(
                value = specialText,
                onValueChange = {}
            )
        }

        composeTestRule.onNodeWithText(specialText, substring = true)
            .assertExists()
    }

    // Test 16: Validate numeric input
    @Test
    fun validateNumericInputTest() {
        composeTestRule.setContent {
            SearchBar(
                value = currentValue,
                onValueChange = { currentValue = currentValue + it }
            )
        }

        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("12345")

        composeTestRule.waitForIdle()
        assertEquals("12345", currentValue)
    }

    // Test 17: Validate mixed content input
    @Test
    fun validateMixedContentInputTest() {
        var newValue: String = ""
        composeTestRule.setContent {
            SearchBar(
                value = currentValue,
                onValueChange = { newValue = newValue + it }
            )
        }

        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Album 123")

        composeTestRule.waitForIdle()
        assertEquals("Album 123", newValue)
    }

    // Test 18: Validate search icon and text field together
    @Test
    fun validateSearchIconAndTextFieldTogetherTest() {
        composeTestRule.setContent {
            SearchBar(
                value = "Test",
                onValueChange = {}
            )
        }

        // Both should be visible
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
        composeTestRule.onNode(hasSetTextAction()).assertExists()
    }

    // Test 19: Validate only one search icon
    @Test
    fun validateSingleSearchIconTest() {
        composeTestRule.setContent {
            SearchBar(
                value = "",
                onValueChange = {}
            )
        }

        val searchIcons = composeTestRule.onAllNodesWithContentDescription("Search")
            .fetchSemanticsNodes()
        assertEquals(1, searchIcons.size)
    }

    // Test 20: Validate text clearance
    @Test
    fun validateTextClearanceTest() {
        composeTestRule.setContent {
            SearchBar(
                value = currentValue,
                onValueChange = { currentValue = it }
            )
        }

        // Input text
        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Clear Me")
        composeTestRule.waitForIdle()

        // Clear text
        composeTestRule.onNode(hasSetTextAction())
            .performTextClearance()
        composeTestRule.waitForIdle()
        assertEquals("", currentValue)
    }

    // Test 21: Validate placeholder reappears after clearing
    @Test
    fun validatePlaceholderReappearsAfterClearingTest() {
        val placeholder = "Search here"

        composeTestRule.setContent {
            SearchBar(
                value = currentValue,
                onValueChange = { currentValue = it },
                placeholder = placeholder
            )
        }

        // Initially placeholder should be visible
        composeTestRule.onNodeWithText(placeholder).assertExists()

        // Input text - placeholder should disappear
        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Text")
        composeTestRule.waitForIdle()

        // Clear text - placeholder should reappear
        composeTestRule.onNode(hasSetTextAction())
            .performTextClearance()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText(placeholder).assertExists()
    }

    // Test 22: Validate long text input
    @Test
    fun validateLongTextInputTest() {
        val longText = "This is a very long search query that might span multiple lines but should be handled by the search bar"

        composeTestRule.setContent {
            SearchBar(
                value = currentValue,
                onValueChange = { currentValue = currentValue + it }
            )
        }

        composeTestRule.onNode(hasSetTextAction())
            .performTextInput(longText)

        composeTestRule.waitForIdle()
        assertEquals(longText, currentValue)
    }

    // Test 23: Validate value change count tracking
    @Test
    fun validateValueChangeCountTrackingTest() {
        composeTestRule.setContent {
            SearchBar(
                value = currentValue,
                onValueChange = {
                    currentValue = it
                    valueChangeCount++
                }
            )
        }

        // Each character input should trigger the callback
        composeTestRule.onNode(hasSetTextAction())
            .performTextInput("Test")

        composeTestRule.waitForIdle()
        assertTrue(valueChangeCount > 0)
    }
}