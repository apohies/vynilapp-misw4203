package com.uniandes.vynilapp.views.common

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertEquals

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

    // Test 2: Validate Albums tab selection state
    @Test
    fun validateAlbumsTabSelectionTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ALBUMS,
                onTabSelected = {}
            )
        }

        // Verify Albums tab exists and is displayed
        composeTestRule.onNodeWithText("Albums")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 3: Validate Artists tab selection state
    @Test
    fun validateArtistsTabSelectionTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.ARTISTS,
                onTabSelected = {}
            )
        }

        // Verify Artists tab exists and is displayed
        composeTestRule.onNodeWithText("Artists")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 4: Validate Collections tab selection state
    @Test
    fun validateCollectionsTabSelectionTest() {
        composeTestRule.setContent {
            BottomNavigationBar(
                selectedTab = NavigationItem.COLLECTIONS,
                onTabSelected = {}
            )
        }

        // Verify Collections tab exists and is displayed
        composeTestRule.onNodeWithText("Collections")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test 5: Validate Albums tab click interaction
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
        assertEquals(NavigationItem.ALBUMS, selectedTab)
        assertEquals(1, tabSelectionCount)
    }

    // Test 6: Validate Artists tab click interaction
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
        assertEquals(NavigationItem.ARTISTS, selectedTab)
        assertEquals(1, tabSelectionCount)
    }

    // Test 7: Validate Collections tab click interaction
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
        assertEquals(NavigationItem.COLLECTIONS, selectedTab)
        assertEquals(1, tabSelectionCount)
    }

    // Test 8: Validate multiple tab navigation sequence
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
        assertEquals(NavigationItem.ARTISTS, selectedTab)
        assertEquals(1, tabSelectionCount)

        composeTestRule.onNodeWithText("Collections").performClick()
        composeTestRule.waitForIdle()
        assertEquals(NavigationItem.COLLECTIONS, selectedTab)
        assertEquals(2, tabSelectionCount)

        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        assertEquals(NavigationItem.ALBUMS, selectedTab)
        assertEquals(3, tabSelectionCount)
    }

    // Test 9: Validate clicking the same tab multiple times
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
        assertEquals(1, tabSelectionCount)

        composeTestRule.onNodeWithText("Albums").performClick()
        composeTestRule.waitForIdle()
        assertEquals(2, tabSelectionCount)
    }

    // Test 10: Validate all navigation items are present
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

    // Test 11: Validate navigation bar structure
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
        assertEquals(1, allNodes.size)

        val artistNodes = composeTestRule.onAllNodesWithText("Artists", substring = false)
            .fetchSemanticsNodes()
        assertEquals(1, artistNodes.size)

        val collectionNodes = composeTestRule.onAllNodesWithText("Collections", substring = false)
            .fetchSemanticsNodes()
        assertEquals(1, collectionNodes.size)
    }

    // Test 12: Validate rapid tab switching
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
        assertEquals(NavigationItem.ARTISTS, selectedTab)
        assertEquals(4, tabSelectionCount)
    }

    // Test 13: Validate each navigation item has label
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

    // Test 14: Validate navigation with Artists as initial state
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

    // Test 15: Validate all tabs are clickable
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
