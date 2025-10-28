package com.uniandes.vynilapp.test

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule

@HiltAndroidTest
abstract class BaseEspressoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    fun ComposeTestRule.waitForNodeWithText(text: String, timeoutMillis: Long = 5000) {
        waitUntil(timeoutMillis = timeoutMillis) {
            onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun ComposeTestRule.waitForNodeWithContentDescription(contentDescription: String, timeoutMillis: Long = 5000) {
        waitUntil(timeoutMillis = timeoutMillis) {
            onAllNodesWithContentDescription(contentDescription).fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun ComposeTestRule.assertNodeWithTextIsVisible(text: String) {
        onNodeWithText(text).assertIsDisplayed()
    }

    fun ComposeTestRule.assertNodeWithContentDescriptionIsVisible(contentDescription: String) {
        onNodeWithContentDescription(contentDescription).assertIsDisplayed()
    }

    fun ComposeTestRule.clickNodeWithText(text: String) {
        onNodeWithText(text).performClick()
    }

    fun ComposeTestRule.clickNodeWithContentDescription(contentDescription: String) {
        onNodeWithContentDescription(contentDescription).performClick()
    }

    fun ComposeTestRule.typeTextInNodeWithText(text: String, input: String) {
        onNodeWithText(text).performTextInput(input)
    }

    fun ComposeTestRule.assertNodeContainsText(text: String) {
        onNodeWithText(text).assertExists()
    }

    fun ComposeTestRule.assertNodeWithTextDoesNotExist(text: String) {
        onNodeWithText(text).assertDoesNotExist()
    }

    fun ComposeTestRule.assertNodeWithContentDescriptionDoesNotExist(contentDescription: String) {
        onNodeWithContentDescription(contentDescription).assertDoesNotExist()
    }

    fun waitForDelay(milliseconds: Long) {
        Thread.sleep(milliseconds)
    }

    fun ComposeTestRule.assertLoadingState() {
        assertNodeWithTextIsVisible("Cargando...")
    }

    fun ComposeTestRule.assertErrorState() {
        assertNodeWithTextIsVisible("Error")
    }

    fun ComposeTestRule.assertContentLoaded() {
        // Verificar que no esté en estado de carga
        assertNodeWithTextDoesNotExist("Cargando...")
        // Verificar que no esté en estado de error
        assertNodeWithTextDoesNotExist("Error")
    }
}
