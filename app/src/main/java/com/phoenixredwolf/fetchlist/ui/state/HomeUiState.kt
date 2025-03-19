package com.phoenixredwolf.fetchlist.ui.state

import com.phoenixredwolf.fetchlist.data.model.FetchItems

/**
 * Sealed class representing the different UI states of the HomeScreen.
 */
sealed class HomeUiState {

    /**
     * Represents the loading state.
     */
    data object Loading : HomeUiState()

    /**
     * Represents the success state with a list of fetched items.
     *
     * @property items The list of fetched items.
     */
    data class Success(val groupedItems: Map<Int, List<FetchItems>>) : HomeUiState()

    /**
     * Represents the error state with an exception and a retry action.
     *
     * @property exception The exception that occurred.
     * @property retryAction A lambda function to retry fetching items.
     */
    data class Error(val exception: Throwable, val retryAction: () -> Unit) : HomeUiState()
}

