package com.uniandes.vynilapp.commonComponents

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.uniandes.vynilapp.views.common.BottomNavigationBar
import com.uniandes.vynilapp.views.common.NavigationItem
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BottomNavigationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var selectedTab: NavigationItem? = null
    private var tabSelectionCount = 0

    @Before
    fun setup() {
        selectedTab = null
        tabSelectionCount = 0
    }

    // Test 1: Validate all navigation items are displayed
    @Test
    fun validateBottomNavigationBarOptionsTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {}
            )
        }

        // Verify all three navigation items are present
        composeTestRule.onNodeWithText("Albums").assertIsDisplayed()
        composeTestRule.onNodeWithText("Artists").assertIsDisplayed()
        composeTestRule.onNodeWithText("Collections").assertIsDisplayed()
    }

    // Test 2: Validate Albums tab click interaction
    @Test
    fun validateAlbumsTabClickTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ARTISTS,
                onTabSelected = {
                    selectedTab = it
                    tabSelectionCount++
                }
            )
        }

        // Click on Albums tab
        composeTestRule.onNodeWithText("Albums").performClick()

        // Verify the callback was triggered
        composeTestRule.waitForIdle()
        Assert.assertEquals(NavigationItem.ALBUMS, selectedTab)
        Assert.assertEquals(1, tabSelectionCount)
    }

    // Test 3: Validate Artists tab click interaction
    @Test
    fun validateArtistsTabClickTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {
                    selectedTab = it
                    tabSelectionCount++
                }
            )
        }

        // Click on Artists tab
        composeTestRule.onNodeWithText("Artists").performClick()

        // Verify the callback was triggered
        composeTestRule.waitForIdle()
        Assert.assertEquals(NavigationItem.ARTISTS, selectedTab)
        Assert.assertEquals(1, tabSelectionCount)
    }

    // Test 4: Validate Collections tab click interaction
    @Test
    fun validateCollectionsTabClickTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {
                    selectedTab = it
                    tabSelectionCount++
                }
            )
        }

        // Click on Collections tab
        composeTestRule.onNodeWithText("Collections").performClick()

        // Verify the callback was triggered
        composeTestRule.waitForIdle()
        Assert.assertEquals(NavigationItem.COLLECTIONS, selectedTab)
        Assert.assertEquals(1, tabSelectionCount)
    }

    // Test 5: Validate multiple tab navigation sequence
    @Test
    fun validateMultipleTabNavigationTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {
                    selectedTab = it
                    tabSelectionCount++
                }
            )
        }

        // Navigate through all tabs in sequence
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(NavigationItem.ARTISTS, selectedTab)
        Assert.assertEquals(1, tabSelectionCount)

        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(NavigationItem.COLLECTIONS, selectedTab)
        Assert.assertEquals(2, tabSelectionCount)

        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(NavigationItem.ALBUMS, selectedTab)
        Assert.assertEquals(3, tabSelectionCount)
    }

    // Test 6: Validate clicking the same tab multiple times
    @Test
    fun validateSameTabMultipleClicksTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {
                    selectedTab = it
                    tabSelectionCount++
                }
            )
        }

        // Click Albums tab multiple times
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(1, tabSelectionCount)

        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(2, tabSelectionCount)
    }

    // Test 7: Validate all navigation items are present
    @Test
    fun validateAllNavigationItemsArePresentTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {}
            )
        }

        // Verify all navigation items are present
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()
        composeTestRule.onNodeWithText("Collections").assertExists()
    }

    // Test 8: Validate navigation bar structure
    @Test
    fun validateNavigationBarStructureTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {}
            )
        }

        // Verify the navigation bar contains exactly 3 items
        val allNodes = composeTestRule.onAllNodesWithText("Albums", substring = false)
            .fetchSemanticsNodes()
        Assert.assertEquals(1, allNodes.size)

        val artistNodes = composeTestRule.onAllNodesWithText("Artists", substring = false)
            .fetchSemanticsNodes()
        Assert.assertEquals(1, artistNodes.size)

        val collectionNodes = composeTestRule.onAllNodesWithText("Collections", substring = false)
            .fetchSemanticsNodes()
        Assert.assertEquals(1, collectionNodes.size)
    }

    // Test 9: Validate rapid tab switching
    @Test
    fun validateRapidTabSwitchingTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {
                    selectedTab = it
                    tabSelectionCount++
                }
            )
        }

        // Rapidly switch between tabs
        composeTestRule.onNodeWithText("Artists").performClick()
        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.onNodeWithText("Artists").performClick()

        composeTestRule.waitForIdle()

        // Verify final state
        Assert.assertEquals(NavigationItem.ARTISTS, selectedTab)
        Assert.assertEquals(4, tabSelectionCount)
    }

    // Test 10: Validate each navigation item has label
    @Test
    fun validateNavigationItemsHaveLabelsTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {}
            )
        }

        // Verify all labels are present
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Artists").assertExists()
        composeTestRule.onNodeWithText("Collections").assertExists()
    }

    // Test 11: Validate navigation with Artists as initial state
    @Test
    fun validateArtistsAsInitialStateTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ARTISTS,
                onTabSelected = { selectedTab = it }
            )
        }
        composeTestRule.onNodeWithText("Artists").assertExists()
        composeTestRule.onNodeWithText("Albums").assertExists()
        composeTestRule.onNodeWithText("Collections").assertExists()
    }

    // Test 12: Validate all tabs are clickable
    @Test
    fun validateAllTabsAreClickableTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {}
            )
        }

        // Verify all tabs are clickable
        composeTestRule.onNodeWithText("Albums").assertHasClickAction()
        composeTestRule.onNodeWithText("Artists").assertHasClickAction()
        composeTestRule.onNodeWithText("Collections").assertHasClickAction()
    }
}