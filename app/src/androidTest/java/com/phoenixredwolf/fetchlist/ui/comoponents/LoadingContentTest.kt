package com.phoenixredwolf.fetchlist.ui.comoponents

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getBoundsInRoot
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import org.junit.Rule
import org.junit.Test
import kotlin.math.abs

class LoadingContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test if the progress indicator is displayed
    @Test
    fun loadingContent_displaysProgressIndicator() {
        composeTestRule.setContent {
            LoadingContent()
        }


        // Ensure CircularProgressIndicator exists
        composeTestRule
            .onNodeWithTag(testTag = "CircularProgressIndicator")
            .assertIsDisplayed()
    }

    // Test if the progress indicator is centered
    @Test
    fun loadingContent_centersProgressIndicator() {
        composeTestRule.setContent {
            LoadingContent()
        }

        // Get bounds of the progress indicator and the root container
        val nodeBounds = composeTestRule.onNode(hasAnyAncestor(isRoot())).getBoundsInRoot()
        val rootBounds = composeTestRule.onRoot().getBoundsInRoot()

        // Compute centers
        val nodeCenterX = nodeBounds.left + nodeBounds.width / 2
        val nodeCenterY = nodeBounds.top + nodeBounds.height / 2
        val rootCenterX = rootBounds.left + rootBounds.width / 2
        val rootCenterY = rootBounds.top + rootBounds.height / 2

        val nodeCenterXFloat = nodeCenterX.value
        val nodeCenterYFloat = nodeCenterY.value
        val rootCenterXFloat = rootCenterX.value
        val rootCenterYFloat = rootCenterY.value

        val tolerance = .5f

        assert(abs(nodeCenterXFloat - rootCenterXFloat) < tolerance) {
            "CircularProgressIndicator is not horizontally centered. Expected $rootCenterX but got $nodeCenterX"
        }
        assert(abs(nodeCenterYFloat - rootCenterYFloat) < tolerance) {
            "CircularProgressIndicator is not vertically centered. Expected $rootCenterY but got $nodeCenterY"
        }
    }

}
