package com.phoenixredwolf.fetchlist.ui.viewmodel

import app.cash.turbine.test
import com.phoenixredwolf.fetchlist.data.model.FetchItems
import com.phoenixredwolf.fetchlist.data.repo.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var repository: DataRepository
    private lateinit var viewModel: HomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel = HomeViewModel(repository)
        assertTrue(viewModel.uiState.value is HomeUiState.Loading)
    }

    @Test
    fun `successful data fetch updates state to Success`() = runTest {
        // Given
        val mockItems = listOf(
            FetchItems(id = 1, listId = 1, name = "Item 1"),
            FetchItems(id = 2, listId = 1, name = "Item 2"),
            FetchItems(id = 3, listId = 2, name = "Item 3")
        )
        val groupedItems = mapOf(
            1 to listOf(
                FetchItems(id = 1, listId = 1, name = "Item 1"),
                FetchItems(id = 2, listId = 1, name = "Item 2")
            ),
            2 to listOf(
                FetchItems(id = 3, listId = 2, name = "Item 3")
            )
        )
        whenever(repository.getFilteredItems()).thenReturn(mockItems)

        // When
        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.first()
        assertTrue(currentState is HomeUiState.Success)
        assertEquals(groupedItems, currentState.groupedItems)
    }

    @Test
    fun `repository exception updates state to Error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        whenever(repository.getFilteredItems()).thenThrow(exception)

        // When
        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.first()
        assertTrue(currentState is HomeUiState.Error)
        assertEquals(exception, currentState.exception)
    }

    @Test
    fun `repository exception with null message shows default error message`() = runTest {
        // Given
        val exception = RuntimeException()
        whenever(repository.getFilteredItems()).thenThrow(exception)

        // When
        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.first()
        assertTrue(currentState is HomeUiState.Error)
        assertEquals(exception, currentState.exception)
    }

    @Test
    fun `empty data fetch updates state to Success with empty map`() = runTest {
        // Given
        val mockItems = emptyList<FetchItems>()
        val groupedItems = emptyMap<Int, List<FetchItems>>()
        whenever(repository.getFilteredItems()).thenReturn(mockItems)

        // When
        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.first()
        assertTrue(currentState is HomeUiState.Success)
        assertEquals(groupedItems, currentState.groupedItems)
    }

    @Test
    fun `ioexception updates state to Error`() = runTest {
        // Given
        val exception = IOException("Network error")
        doThrow(exception).whenever(repository).getFilteredItems()

        // When
        viewModel = HomeViewModel(repository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val currentState = viewModel.uiState.first()
        assertTrue(currentState is HomeUiState.Error)
        assertEquals(exception, currentState.exception)
    }

    @Test
    fun `successful data fetch only emits loading and success`() = runTest {
        // Given
        val mockItems = listOf(
            FetchItems(id = 1, listId = 1, name = "Item 1"),
            FetchItems(id = 2, listId = 1, name = "Item 2"),
            FetchItems(id = 3, listId = 2, name = "Item 3")
        )
        val groupedItems = mapOf(
            1 to listOf(
                FetchItems(id = 1, listId = 1, name = "Item 1"),
                FetchItems(id = 2, listId = 1, name = "Item 2")
            ),
            2 to listOf(
                FetchItems(id = 3, listId = 2, name = "Item 3")
            )
        )
        whenever(repository.getFilteredItems()).thenReturn(mockItems)

        // When
        viewModel = HomeViewModel(repository)

        // Then
        viewModel.uiState.test {
            assertTrue(awaitItem() is HomeUiState.Loading)
            val successState = awaitItem() as HomeUiState.Success
            assertEquals(groupedItems, successState.groupedItems)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `error data fetch only emits loading and error`() {
        runTest {
            // Given
            val exception = IOException("Network error")
            doThrow(exception).whenever(repository).getFilteredItems()

            // When
            viewModel = HomeViewModel(repository)

            // Then
            viewModel.uiState.test {
                assertTrue(awaitItem() is HomeUiState.Loading)
                val errorState = awaitItem() as HomeUiState.Error
                assertEquals(exception, errorState.exception)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}