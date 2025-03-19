package com.phoenixredwolf.fetchlist.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import com.phoenixredwolf.fetchlist.data.model.FetchItems
import org.junit.Rule
import org.junit.Test

class ItemsContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    //⃣ Test if the empty message is displayed when groupedItems is empty
    @Test
    fun itemsContent_displaysEmptyMessageWhenListIsEmpty() {
        composeTestRule.setContent {
            ItemsContent(groupedItems = emptyMap())
        }

        // Ensure the empty list message is displayed
        composeTestRule.onNodeWithText("No items to display.").assertExists()
    }

    // Test if GroupCards are displayed when the list is not empty
    @Test
    fun itemsContent_displaysGroupCardsWhenListIsNotEmpty() {
        val testItems = mapOf(
            1 to listOf(FetchItems(1, 1, "Item 1")),
            2 to listOf(FetchItems(2, 2, "Item 2"))
        )

        composeTestRule.setContent {
            ItemsContent(groupedItems = testItems)
        }

        // Ensure GroupCards are displayed for each list ID
        composeTestRule.onNodeWithText("List ID: 1").assertExists()
        composeTestRule.onNodeWithText("List ID: 2").assertExists()
    }

    // Test if the correct number of GroupCards are displayed
    @Test
    fun itemsContent_displaysCorrectNumberOfGroupCards() {
        val testItems = mapOf(
            1 to listOf(FetchItems(1, 1, "Item 1")),
            2 to listOf(FetchItems(2, 2, "Item 2")),
            3 to listOf(FetchItems(3, 3, "Item 3"))
        )

        composeTestRule.setContent {
            ItemsContent(groupedItems = testItems)
        }

        // Count the number of GroupCards rendered
        testItems.keys.forEach { listId ->
            composeTestRule.onNodeWithText("List ID: $listId").assertExists()
        }
    }

    // Test that GroupCard displays correct item content
    @Test
    fun itemsContent_verifiesGroupCardContent() {
        val testItems = mapOf(
            1 to listOf(FetchItems(1, 1, "Item A")),
            2 to listOf(FetchItems(2, 2, "Item B"))
        )

        composeTestRule.setContent {
            ItemsContent(groupedItems = testItems)
        }

        // Ensure each item's name is displayed inside GroupCard
        composeTestRule.onNodeWithText("Item A").assertExists()
        composeTestRule.onNodeWithText("Item B").assertExists()
    }

    // Test LazyColumn scrolling
    @Test
    fun itemsContent_supportsLazyColumnScrolling() {
        val testItems = (1..20).associateWith { listOf(FetchItems(it, it, "Item $it")) }

        composeTestRule.setContent {
            ItemsContent(groupedItems = testItems)
        }

        // Ensure the first item is visible
        composeTestRule.onNodeWithText("List ID: 1").assertExists()

        // Scroll down
        composeTestRule.onRoot().performTouchInput { swipeUp() }

        // Ensure the last item is now visible
        composeTestRule.onNodeWithText("List ID: 20").assertExists()
    }

    // Test if item content inside GroupCard is correctly displayed
    @Test
    fun itemsContent_verifiesItemContentInsideGroupCard() {
        val testItems = mapOf(
            1 to listOf(FetchItems(1, 1, "Special Item"))
        )

        composeTestRule.setContent {
            ItemsContent(groupedItems = testItems)
        }

        // Verify item name is inside GroupCard
        composeTestRule.onNodeWithText("Special Item").assertExists()
    }

    // Test LazyColumn scrolling (down and back up)
    @Test
    fun itemsContent_supportsScrollingUpAndDown() {
        val testItems = (1..20).associateWith { listOf(FetchItems(it, it, "Item $it")) }

        composeTestRule.setContent {
            ItemsContent(groupedItems = testItems)
        }

        // Ensure the first item is visible
        composeTestRule.onNodeWithText("List ID: 1").assertExists()

        // Scroll down
        composeTestRule.onRoot().performTouchInput { swipeUp() }

        // Ensure the last item is now visible
        composeTestRule.onNodeWithText("List ID: 20").assertExists()

        // Scroll back up
        composeTestRule.onRoot().performTouchInput { swipeDown() }

        // Ensure the first item is visible again
        composeTestRule.onNodeWithText("List ID: 1").assertExists()
    }
}
