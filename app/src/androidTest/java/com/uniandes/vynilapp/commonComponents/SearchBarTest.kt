package com.uniandes.vynilapp.commonComponents

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.uniandes.vynilapp.views.common.SearchBar
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    // Test 2: Validate text input is displayed
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

    // Test 3: Validate text input can be focused
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

    // Test 4: Validate onValueChange callback
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
        Assert.assertTrue(valueChangeCount > 0)
    }

    // Test 5: Validate multiple text inputs
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
        Assert.assertEquals("Hello", currentValue)

        // Input more text (it appends)
        composeTestRule.onNode(hasSetTextAction())
            .performTextInput(" World")

        composeTestRule.waitForIdle()
        Assert.assertEquals("Hello World", currentValue)
    }

    // Test 6: Validate text replacement
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

        Assert.assertEquals("Replaced", currentValue)
    }

    // Test 7: Validate empty value shows placeholder
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

    // Test 8: Validate SearchBar structure
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

    // Test 9: Validate SearchBar renders without errors
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

    // Test 10: Validate Spanish placeholder
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

    // Test 11: Validate special characters in search
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

    // Test 12: Validate numeric input
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
        Assert.assertEquals("12345", currentValue)
    }

    // Test 13: Validate mixed content input
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
        Assert.assertEquals("Album 123", newValue)
    }

    // Test 14: Validate search icon and text field together
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

    // Test 15: Validate text clearance
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
        Assert.assertEquals("", currentValue)
    }

    // Test 16: Validate placeholder reappears after clearing
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

    // Test 17: Validate long text input
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
        Assert.assertEquals(longText, currentValue)
    }

    // Test 18: Validate value change count tracking
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
        Assert.assertTrue(valueChangeCount > 0)
    }
}