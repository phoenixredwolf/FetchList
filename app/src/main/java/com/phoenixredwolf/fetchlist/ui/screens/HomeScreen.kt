package com.phoenixredwolf.fetchlist.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.phoenixredwolf.fetchlist.ui.comoponents.HomeTopBar
import com.phoenixredwolf.fetchlist.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * HomeScreen is the main screen of the application, displaying a list of items fetched from the ViewModel.
 * It handles loading (displaying a loading indicator), success (displaying a list of grouped items),
 * and error (displaying an error message with a retry option) states, and provides pull-to-refresh functionality.
 *
 * This composable uses ExperimentalMaterial3Api, so it is required to opt into it.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    // Inject the HomeViewModel using Koin.
    val viewModel: HomeViewModel = koinViewModel()

    // Collect the uiState from the ViewModel as a Compose State.
    val uiState by viewModel.uiState.collectAsState()

    // Create a CoroutineScope to launch coroutines within this composable's lifecycle.
    val coroutineScope = rememberCoroutineScope()

    /**
     * onRefresh is triggered when the user performs a pull-to-refresh gesture.
     * It launches a coroutine to fetch items from the ViewModel and simulates a network delay.
     *
     * The delay is added to simulate network latency or any other time-consuming operations
     * that might occur during a real-world data fetching scenario.
     * This is for UI/UX purposes and should NOT be used in production code.
     * The viewModel.fetchItems() function is responsible for updating the uiState.
     */
    val onRefresh: () -> Unit = {
        coroutineScope.launch {
            // Initiate the data fetching process in the ViewModel.
            viewModel.fetchItems()

            // Simulate a 1-second delay to represent network latency or processing time.
            delay(1000L)
        }
    }

    // Initialize the pull-to-refresh state.
    val pullToRefreshState = rememberPullToRefreshState()

    // Scaffold provides the basic layout structure for the screen, including a top bar and content area.
    Scaffold(
        topBar = {
            // Display the HomeTopBar with the refresh button.
            HomeTopBar(onRefresh = onRefresh)
        }
    ) { paddingValues ->
        // Display the main content of the screen, handling different UI states.
        HomeContent(
            uiState = uiState,
            pullToRefreshState = pullToRefreshState,
            paddingValues = paddingValues,
            onRefresh = onRefresh
        )
    }
}