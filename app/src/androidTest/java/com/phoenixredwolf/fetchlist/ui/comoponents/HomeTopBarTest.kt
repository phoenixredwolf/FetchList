package com.phoenixredwolf.fetchlist.ui.comoponents

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class HomeTopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test if the title is displayed
    @Test
    fun homeTopBar_displaysTitle() {
        composeTestRule.setContent {
            HomeTopBar(onRefresh = {})
        }

        // Ensure the title text is displayed
        composeTestRule.onNodeWithText("Grouped Items").assertExists()
    }

    // Test if the refresh button exists
    @Test
    fun homeTopBar_hasRefreshButton() {
        composeTestRule.setContent {
            HomeTopBar(onRefresh = {})
        }

        // Ensure the refresh button is displayed
        composeTestRule.onNodeWithContentDescription("Refresh").assertExists()
    }

    // Test if clicking the refresh button calls `onRefresh()`
    @Test
    fun homeTopBar_callsOnRefreshWhenClicked() {
        var refreshClicked = false

        composeTestRule.setContent {
            HomeTopBar(onRefresh = { refreshClicked = true })
        }

        // Click the refresh button
        composeTestRule.onNodeWithContentDescription("Refresh").performClick()

        // Assert that `onRefresh` was called
        assertTrue(refreshClicked, "onRefresh() should have been triggered when clicking the refresh button.")
    }
}