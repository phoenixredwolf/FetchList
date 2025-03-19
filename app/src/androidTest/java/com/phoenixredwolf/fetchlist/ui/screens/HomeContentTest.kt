package com.phoenixredwolf.fetchlist.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.phoenixredwolf.fetchlist.data.model.FetchItems
import com.phoenixredwolf.fetchlist.ui.viewmodel.HomeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalMaterial3Api::class)
class HomeContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    //  Test if LoadingContent is displayed when uiState is Loading
    @Test
    fun homeContent_displaysLoadingState() {
        composeTestRule.setContent {
            HomeContent(
                uiState = HomeUiState.Loading,
                pullToRefreshState = rememberPullToRefreshState(),
                paddingValues = PaddingValues(),
                onRefresh = {}
            )
        }

        // Ensure LoadingContent is displayed
        composeTestRule.onNodeWithTag("CircularProgressIndicator").assertExists()
    }

    // Test if ItemsContent is displayed when uiState is Success
    @Test
    fun homeContent_displaysSuccessState() {
        val testItems = mapOf(
            1 to listOf(FetchItems(1, 1, "Item 1")),
            2 to listOf(FetchItems(2, 2, "Item 2"))
        )

        composeTestRule.setContent {
            HomeContent(
                uiState = HomeUiState.Success(groupedItems = testItems),
                pullToRefreshState = rememberPullToRefreshState(),
                paddingValues = PaddingValues(),
                onRefresh = {}
            )
        }

        // Ensure ItemsContent is displayed
        composeTestRule.onNodeWithText("List ID: 1").assertExists()
        composeTestRule.onNodeWithText("List ID: 2").assertExists()
    }

    // Test if ErrorContent is displayed when uiState is Error
    @Test
    fun homeContent_displaysErrorState() {
        val testException = Exception("Test Error")
        var retryClicked = false

        composeTestRule.setContent {
            HomeContent(
                uiState = HomeUiState.Error(exception = testException, retryAction = { retryClicked = true }),
                pullToRefreshState = rememberPullToRefreshState(),
                paddingValues = PaddingValues(),
                onRefresh = {}
            )
        }

        // Ensure ErrorContent is displayed
        composeTestRule.onNodeWithText("Error: ${testException.message}").assertExists()

        // Click retry and verify action is triggered
        composeTestRule.onNodeWithText("Retry").performClick()
        assertTrue(retryClicked, "Retry button should trigger retryAction")
    }

    // Test if PullToRefresh calls onRefresh
    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun homeContent_callsOnRefreshWhenPulledToRefresh() {
        composeTestRule.setContent {

            val refreshState = rememberPullToRefreshState()
            val testItems1 = mapOf(
                1 to listOf(FetchItems(1, 1, "Item 1")),
                2 to listOf(FetchItems(2, 2, "Item 2"))
            )

            val testItems2 = mapOf(
                1 to listOf(FetchItems(3, 1, "Item 9")),
                2 to listOf(FetchItems(4, 2, "Item 5"))
            )

            val uiState = remember { mutableStateOf<HomeUiState>(HomeUiState.Success(groupedItems = testItems1)) }

            val onRefresh = {
                runBlocking {
                    delay(1000L)
                    uiState.value = HomeUiState.Success(groupedItems = testItems2)
                }
            }

            HomeContent(
                uiState = uiState.value,
                pullToRefreshState = refreshState,
                paddingValues = PaddingValues(),
                onRefresh = onRefresh
            )
        }

        Log.d("HomeContentTest", "about to make first assertion")

        // Ensure initial empty state
        composeTestRule.onNodeWithText("Item 1").assertExists()

        Log.d("HomeContentTest", "about to swipe down")

        // Simulate pull-to-refresh gesture
        composeTestRule.onRoot()
            .performTouchInput {
                swipeDown(
                    startY = 1f,
                    endY = 1000f
                )
                Log.d("HomeContentTest", "swiped down")
            }
        // Wait for recomposition after refresh
        composeTestRule.waitForIdle()

        // Ensure the new item appears
        composeTestRule.onNodeWithText("Item 5").assertExists()
    }


}