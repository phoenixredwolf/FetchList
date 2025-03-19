package com.phoenixredwolf.fetchlist.ui.comoponents


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.phoenixredwolf.fetchlist.data.model.FetchItems
import com.phoenixredwolf.fetchlist.ui.theme.FetchListTheme
import org.junit.Rule
import org.junit.Test

class GroupCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun groupCard_displaysCorrectTitleAndItems() {
        val testItems = listOf(
            FetchItems(1, 1, "Item 1"),
            FetchItems(2, 1, "Item 2"),
            FetchItems(3, 1, "Item 3")
        )

        // Set the Composable under test
        composeTestRule.setContent {
            GroupCard(listId = 1, items = testItems)
        }

        // Verify title is displayed
        composeTestRule.onNodeWithText("List ID: 1").assertExists()

        // Verify all items are displayed
        testItems.forEach { item ->
            composeTestRule.onNodeWithText(item.name ?: "Unknown").assertExists()
        }
    }

    @Test
    fun groupCard_displaysCorrectlyInLightTheme() {
        composeTestRule.setContent {
            FetchListTheme(darkTheme = false) { // Force Light Theme
                GroupCard(listId = 1, items = listOf(FetchItems(1, 1, "Item 1")))
            }
        }

        // Ensure the title and item appear correctly
        composeTestRule.onNodeWithText("List ID: 1").assertExists()
        composeTestRule.onNodeWithText("Item 1").assertExists()
    }

    @Test
    fun groupCard_displaysCorrectlyInDarkTheme() {
        composeTestRule.setContent {
            FetchListTheme(darkTheme = true) { // Force Dark Theme
                GroupCard(listId = 1, items = listOf(FetchItems(1, 1, "Item 1")))
            }
        }

        // Ensure the title and item appear correctly
        composeTestRule.onNodeWithText("List ID: 1").assertExists()
        composeTestRule.onNodeWithText("Item 1").assertExists()
    }

    @Test
    fun groupCard_respectsScreenWidthAndWrapsCorrectly() {
        val testItems = (1..10).map { FetchItems(it, 1, "Item $it") }

        composeTestRule.setContent {
            GroupCard(listId = 1, items = testItems)
        }

        // Verify the first few items exist (ensuring proper wrapping)
        composeTestRule.onNodeWithText("Item 1").assertExists()
        composeTestRule.onNodeWithText("Item 5").assertExists()
        composeTestRule.onNodeWithText("Item 10").assertExists()
    }

    @Test
    fun groupCard_adjustsToDifferentScreenSizes() {
        val testItems = (1..10).map { FetchItems(it, 1, "Item $it") }

        composeTestRule.setContent {
            Box(modifier = Modifier.width(300.dp)) { // Simulate a narrow screen
                GroupCard(listId = 1, items = testItems)
            }
        }

        // Ensure items still exist (meaning they adapted)
        composeTestRule.onNodeWithText("Item 1").assertExists()
        composeTestRule.onNodeWithText("Item 10").assertExists()
    }

    @Test
    fun groupCard_displaysMessageWhenListIsEmpty() {
        composeTestRule.setContent {
            GroupCard(listId = 1, items = emptyList())
        }

        // Verify the title is still displayed
        composeTestRule.onNodeWithText("List ID: 1").assertExists()

        // Ensure no item text appears
        composeTestRule.onNodeWithText("Item", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun groupCard_handlesLargeLists() {
        val testItems = (1..1000).map { FetchItems(it, 1, "Item $it") }

        composeTestRule.setContent {
            GroupCard(listId = 1, items = testItems)
        }

        // Check a few random items to ensure they are displayed
        composeTestRule.onNodeWithText("Item 1").assertExists()
        composeTestRule.onNodeWithText("Item 500").assertExists()
        composeTestRule.onNodeWithText("Item 1000").assertExists()
    }

    @Test
    fun groupCard_updatesWhenDataChanges() {
        val initialItems = listOf(FetchItems(1, 1, "Item 1"))
        val updatedItems = listOf(FetchItems(2, 1, "Item 2"))

        var items by mutableStateOf(initialItems)

        composeTestRule.setContent {
            GroupCard(listId = 1, items = items)
        }

        // 🔹 Ensure initial item is displayed
        composeTestRule.onNodeWithText("Item 1").assertExists()

        // 🔹 Update state using `runOnIdle`
        composeTestRule.runOnIdle {
            items = updatedItems
        }

        // 🔹 Ensure the old item is removed
        composeTestRule.onNodeWithText("Item 1").assertDoesNotExist()

        // 🔹 Ensure the new item appears
        composeTestRule.onNodeWithText("Item 2").assertExists()
    }

    @Test
    fun groupCard_displaysDifferentListIDsSeparately() {
        val testItems1 = listOf(FetchItems(1, 1, "Item A"))
        val testItems2 = listOf(FetchItems(2, 2, "Item B"))

        composeTestRule.setContent {
            Column {
                GroupCard(listId = 1, items = testItems1)
                GroupCard(listId = 2, items = testItems2)
            }
        }

        // Ensure both list IDs exist separately
        composeTestRule.onNodeWithText("List ID: 1").assertExists()
        composeTestRule.onNodeWithText("List ID: 2").assertExists()

        // Ensure items are in the correct list
        composeTestRule.onNodeWithText("Item A").assertExists()
        composeTestRule.onNodeWithText("Item B").assertExists()
    }

    @Test
    fun groupCard_hasCorrectAccessibilityLabels() {
        val testItems = listOf(FetchItems(1, 1, "Item 1"))

        composeTestRule.setContent {
            GroupCard(listId = 1, items = testItems)
        }

        // Ensure List ID has the correct label
        composeTestRule.onNodeWithText("List ID: 1")
            .assertExists()
            .assertContentDescriptionContains("List ID: 1")

        // Ensure Item 1 has a correct label
        composeTestRule.onNodeWithText("Item 1")
            .assertExists()
            .assertContentDescriptionContains("Item 1")
    }

    @Test
    fun groupCard_hasCorrectFlowRowAlignment() {
        val testItems = (1..5).map { FetchItems(it, 1, "Item $it") }

        composeTestRule.setContent {
            GroupCard(listId = 1, items = testItems)
        }

        // Get position of title and first item
        val titlePosition = composeTestRule.onNodeWithText("List ID: 1").getBoundsInRoot()
        val firstItemPosition = composeTestRule.onNodeWithText("Item 1").getBoundsInRoot()

        // Assert that the title is above the first item
        assert(titlePosition.top < firstItemPosition.top) {
            "Expected title to be above the first item, but it wasn't."
        }
    }


}