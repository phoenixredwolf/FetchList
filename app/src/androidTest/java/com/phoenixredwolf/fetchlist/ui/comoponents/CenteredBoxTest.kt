package com.phoenixredwolf.fetchlist.ui.comoponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.math.abs

class CenteredBoxTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test if child content is rendered
    @Test
    fun centeredBox_displaysChildContent() {
        composeTestRule.setContent {
            CenteredBox {
                Box(modifier = Modifier.testTag("CenteredContent"))
            }
        }

        // Assert the content inside CenteredBox is displayed
        composeTestRule.onNodeWithTag("CenteredContent").assertExists()
    }

    // Test if CenteredBox fills the entire screen
    @Test
    fun centeredBox_fillsEntireScreen() {
        composeTestRule.setContent {
            CenteredBox {
                Box(modifier = Modifier.testTag("CenteredContent"))
            }
        }

        // Assert that CenteredBox takes up the entire screen
        val rootBounds = composeTestRule.onRoot().getBoundsInRoot()
        val expectedSize = rootBounds.width

        // Assert that width and height are equal to the expected full screen size
        composeTestRule.onRoot().assertWidthIsAtLeast(expectedSize)
        composeTestRule.onRoot().assertHeightIsAtLeast(expectedSize)
    }

    // Test if content is centered
    @Test
    fun centeredBox_centersContent() {
        composeTestRule.setContent {
            CenteredBox {
                Box(modifier = Modifier.testTag("CenteredContent").size(50.dp))
            }
        }

        // Get bounds of the child and root
        val nodeBounds = composeTestRule.onNodeWithTag("CenteredContent").getBoundsInRoot()
        val rootBounds = composeTestRule.onRoot().getBoundsInRoot()

        // Compute centers
        val nodeCenterX = nodeBounds.left + nodeBounds.width / 2
        val nodeCenterY = nodeBounds.top + nodeBounds.height / 2
        val rootCenterX = rootBounds.left + rootBounds.width / 2
        val rootCenterY = rootBounds.top + rootBounds.height / 2

        // Allow a small tolerance to account for floating-point errors
        val nodeCenterXFloat = nodeCenterX.value
        val nodeCenterYFloat = nodeCenterY.value
        val rootCenterXFloat = rootCenterX.value
        val rootCenterYFloat = rootCenterY.value

        val tolerance = .5f

        assert(abs(nodeCenterXFloat - rootCenterXFloat) < tolerance) {
            "Content is not horizontally centered. Expected $rootCenterXFloat but got $nodeCenterXFloat"
        }
        assert(abs(nodeCenterYFloat - rootCenterYFloat) < tolerance) {
            "Content is not vertically centered. Expected $rootCenterYFloat but got $nodeCenterYFloat"
        }
    }

    // Test multiple children do not break layout
    @Test
    fun centeredBox_allowsMultipleChildren() {
        composeTestRule.setContent {
            CenteredBox {
                Box(modifier = Modifier.testTag("Child1"))
                Box(modifier = Modifier.testTag("Child2"))
            }
        }

        // Ensure both children are present
        composeTestRule.onNodeWithTag("Child1").assertExists()
        composeTestRule.onNodeWithTag("Child2").assertExists()
    }

    // Test dynamic content updates
    @Test
    fun centeredBox_reactsToContentChanges() = runTest {
        // 🔹 Step 1: Define mutable state to track the tag dynamically
        val tagState = mutableStateOf("Initial")

        composeTestRule.setContent {
            CenteredBox {
                Box(modifier = Modifier.testTag(tagState.value))
            }
        }

        // 🔹 Step 2: Verify initial content exists
        composeTestRule.onNodeWithTag("Initial").assertExists()

        // 🔹 Step 3: Change state dynamically using `runOnIdle`
        composeTestRule.runOnIdle {
            tagState.value = "Updated" // UI will recompose automatically
        }

        // 🔹 Step 4: Wait for recomposition before checking assertions
        composeTestRule.waitForIdle()

        // 🔹 Step 5: Ensure updated content appears and old content disappears
        composeTestRule.onNodeWithTag("Updated").assertExists()
        composeTestRule.onNodeWithTag("Initial").assertDoesNotExist()
    }

}
