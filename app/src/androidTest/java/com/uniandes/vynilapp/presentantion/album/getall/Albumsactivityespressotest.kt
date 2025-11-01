package com.uniandes.vynilapp.presentantion.album.getall

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.uniandes.vynilapp.views.AlbumsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas de UI Espresso para AlbumsActivity
 * Verifica la funcionalidad de obtener y mostrar todos los álbumes
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class AlbumsActivityEspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<AlbumsActivity>()

    @Test
    fun testAlbumsScreenDisplayed() {
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Find in albums").assertExists()
    }

    @Test
    fun testLoadingStateShown() {
        composeTestRule.onNode(
            hasContentDescription("Loading") or hasTestTag("loading_indicator")
        ).assertExists()
    }

    @Test
    fun testAlbumListDisplayed() {
        composeTestRule.waitForIdle()
        Thread.sleep(2000)
        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(1)
    }

    @Test
    fun testSearchFunctionality() {
        composeTestRule.waitForIdle()
        Thread.sleep(1500)

        composeTestRule.onNodeWithText("Find in albums")
            .performTextInput("Rock")

        composeTestRule.waitForIdle()
    }

    @Test
    fun testAlbumCardClick() {
        composeTestRule.waitForIdle()
        Thread.sleep(2000)

        composeTestRule.onAllNodes(hasClickAction())
            .onFirst()
            .performClick()

        composeTestRule.waitForIdle()
    }
}