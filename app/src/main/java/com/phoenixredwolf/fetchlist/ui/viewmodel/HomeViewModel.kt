package com.phoenixredwolf.fetchlist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixredwolf.fetchlist.data.repo.DataRepository
import com.phoenixredwolf.fetchlist.ui.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the HomeScreen, responsible for managing the UI state and fetching data.
 *
 * @property repository The data repository used to fetch items.
 */
class HomeViewModel(private val repository: DataRepository) : ViewModel() {

    /**
     * Mutable StateFlow representing the current UI state.
     * It starts with the Loading state.
     */
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)

    /**
     * Public StateFlow representing the current UI state.
     * This is exposed to the UI for observation.
     */
    val uiState: StateFlow<HomeUiState> = _uiState

    /**
     * Initializes the ViewModel by fetching items from the repository.
     */
    init {
        fetchItems()
    }

    /**
     * Fetches items from the repository and updates the UI state accordingly.
     *
     * This function is also used for pull-to-refresh functionality.
     *
     * It groups the fetched items by their `listId` and sorts the items within each group
     * numerically by the digits found within their `name` property.
     */
    fun fetchItems() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading // Set loading state before fetching
            try {
                val items = repository.getFilteredItems()
                val groupedItems = items.groupBy { it.listId }
                    .mapValues { (_, list) ->
                        list.sortedWith(compareBy {
                            it.name?.filter {
                                char -> char.isDigit() }?.toIntOrNull() ?: Int.MAX_VALUE })
                    }
                _uiState.value = HomeUiState.Success(groupedItems)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e, ::fetchItems) // Pass exception and retry action
            }
        }
    }

    /**
     * Sorts a list of strings numerically based on the number within each string.
     *
     * This function is not currently used, but was created for potential future use.
     *
     * @param items The list of strings to sort.
     * @return The sorted list of strings.
     */
    private fun sortItemsNumerically(items: List<String>): List<String> {
        return items.sortedBy { item ->
            val numberString = item.replace("Item ", "")
            numberString.toIntOrNull() ?: 0
        }
    }
}
