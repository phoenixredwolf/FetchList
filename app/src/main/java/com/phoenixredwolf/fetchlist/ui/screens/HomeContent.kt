package com.phoenixredwolf.fetchlist.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.phoenixredwolf.fetchlist.ui.comoponents.ErrorContent
import com.phoenixredwolf.fetchlist.ui.comoponents.LoadingContent
import com.phoenixredwolf.fetchlist.ui.viewmodel.HomeUiState
import kotlinx.coroutines.launch

/**
 * A composable that displays the main content of the home screen.
 *
 * This composable handles the different states of the home screen UI, including loading, success, and error.
 * It also implements pull-to-refresh functionality.
 *
 * @param uiState The current UI state of the home screen, represented by [HomeUiState].
 * @param pullToRefreshState The state of the pull-to-refresh functionality, represented by [PullToRefreshState].
 * @param paddingValues The padding values to apply to the content.
 * @param onRefresh A callback function that is invoked when the user triggers a refresh.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    pullToRefreshState: PullToRefreshState,
    paddingValues: PaddingValues,
    onRefresh: () -> Unit
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    PullToRefreshBox(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        state = pullToRefreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            coroutineScope.launch {
                onRefresh()
                isRefreshing = false
            }
        },
    ) {
        when (uiState) {
            is HomeUiState.Loading -> LoadingContent()
            is HomeUiState.Success -> ItemsContent(groupedItems = uiState.groupedItems)
            is HomeUiState.Error -> ErrorContent(
                errorMessage = uiState.exception.message,
                onRetry = uiState.retryAction
            )
        }
    }
}


