package com.phoenixredwolf.fetchlist.ui.comoponents

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class ErrorContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorContent_displaysProvidedErrorMessage() {
        val errorMessage = "An unknown error occurred."

        composeTestRule.setContent {
            ErrorContent(errorMessage = errorMessage, onRetry = {})
        }

        composeTestRule.onNodeWithText("Error: $errorMessage").assertExists()
    }

    @Test
    fun errorContent_displaysDefaultErrorMessageWhenNull() {
        composeTestRule.setContent {
            ErrorContent(errorMessage = null, onRetry = {})
        }

        composeTestRule.onNodeWithText("Error: An unknown error occurred.").assertExists()
    }

    @Test
    fun errorContent_callsOnRetryWhenButtonClicked() {
        var retryClicked = false

        composeTestRule.setContent {
            ErrorContent(
                errorMessage = "Test Error",
                onRetry = { retryClicked = true }
            )
        }

        composeTestRule.onNodeWithText("Retry").performClick()

        assertTrue(retryClicked, "onRetry() should have been called.")
    }
}