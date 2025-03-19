package com.phoenixredwolf.fetchlist.data.repo

import com.phoenixredwolf.fetchlist.data.model.FetchItems
import com.phoenixredwolf.fetchlist.data.network.ApiService
import com.phoenixredwolf.fetchlist.utils.Logger
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

class DataRepositoryTest {

    private val mockApiService: ApiService = mock()
    private val mockApiServiceWithMockK = mockk<ApiService>()
    private val mockLogger: Logger = mock()
    private val repository: DataRepository = DataRepositoryImpl(mockApiService, mockLogger)

    @Test
    fun `repository should filter out items with blank or null names`() = runTest {
        // Arrange: Prepare a list of FetchItems with some items having blank or null names.
        val mockData = listOf(
            FetchItems(1, 1, "Item 1"),
            FetchItems(2, 2, ""),
            FetchItems(3, 3, null),
            FetchItems(4, 4, "Item 4")
        )

        // Mock the ApiService to return the prepared data.
        whenever(mockApiService.getItems()).thenReturn(mockData)

        // Act: Call the getFilteredItems method of the repository.
        val items = repository.getFilteredItems()

        // Assert: Verify that the items with blank or null names are filtered out.
        assertTrue(items.size == 2)
        assertTrue(items.any { it.id == 1 })
        assertTrue(items.any { it.id == 4 })
        assertTrue(items.none { it.id == 2 })
        assertTrue(items.none { it.id == 3 })
    }

    @Test
    fun `repository should return empty list when api service returns empty list`() = runTest {
        // Arrange: Mock the ApiService to return an empty list.
        val mockData = listOf<FetchItems>()
        whenever(mockApiService.getItems()).thenReturn(mockData)

        // Act: Call the getFilteredItems method of the repository.
        val items = repository.getFilteredItems()

        // Assert: Verify that the repository returns an empty list.
        assertTrue(items.isEmpty())
    }

    @Test
    fun `repository should return empty list when api service returns null`() = runTest {
        // Arrange: Mock the ApiService to return null.
        whenever(mockApiService.getItems()).thenReturn(null)

        // Act: Call the getFilteredItems method of the repository.
        val items = repository.getFilteredItems()

        // Assert: Verify that the repository returns an empty list.
        assertTrue(items.isEmpty())
    }

    @Test
    fun `repository should throw IOException when api service throws IOException`() = runTest {
        // Arrange: Configure the mock ApiService to throw an IOException when getItems() is called.
        whenever(mockApiService.getItems()).thenAnswer { throw IOException("Network error") }

        // Act: Call the repository's getFilteredItems() method, which should throw the IOException.
        try {
            repository.getFilteredItems()
            // Fail the test if no exception is thrown, as an IOException is expected.
            fail("Expected IOException but no exception was thrown")
        } catch (e: IOException) {
            // Assert: Verify that the correct IOException is caught and that the logger logs the error.
            assertEquals("Network error", e.message)
            // Verify that the logger's logError method was called with the expected arguments.
            verify(mockLogger).logError(eq("DataRepositoryImpl"), eq("Network error:"), eq(e))
        }
    }

    @Test
    fun `repository should handle other exceptions from api service`() = runTest {
        // Arrange: Configure the mock ApiService to throw a RuntimeException when getItems() is called.
        whenever(mockApiService.getItems()).thenThrow(RuntimeException("Server error"))

        // Act: Call the repository's getFilteredItems() method, which should throw the RuntimeException.
        try {
            repository.getFilteredItems()
            // Fail the test if no exception is thrown, as a RuntimeException is expected.
            fail("Expected RuntimeException but no exception was thrown")
        } catch (e: RuntimeException) {
            // Assert: Verify that the correct RuntimeException is caught and that the logger logs the error.
            assertEquals("Server error", e.message)
            // Verify that the logger's logError method was called with the expected arguments.
            verify(mockLogger).logError(eq("DataRepositoryImpl"), eq("Error retrieving data:"), eq(e))
        }
    }

    @Test
    fun `repository should handle items with special characters in names`() = runTest {
        // Arrange: Prepare a list of FetchItems with special characters in names.
        val mockData = listOf(
            FetchItems(1, 1, "!@#$%^&*()"),
            FetchItems(2, 2, "Item with spaces"),
            FetchItems(3, 3, "")
        )
        // Mock the ApiService to return the prepared data.
        whenever(mockApiService.getItems()).thenReturn(mockData)

        // Act: Call the getFilteredItems method of the repository.
        val items = repository.getFilteredItems()

        // Assert: Verify that the items with special characters are handled correctly.
        assertEquals(2, items.size)
        assertTrue(items.any { it.id == 1 })
        assertTrue(items.any { it.id == 2 })
    }

    @Test
    fun `repository should handle coroutine cancellation using MockK`() = runTest {
        // Arrange: Ensure MockK is used properly
        coEvery { mockApiServiceWithMockK.getItems() } coAnswers {
            CompletableDeferred<List<FetchItems>>().await() // Suspends indefinitely but supports cancellation
        }

        val repository = DataRepositoryImpl(mockApiServiceWithMockK, mockLogger)

        // Launch a coroutine to call the repository method.
        val job = launch {
            try {
                repository.getFilteredItems()
                fail("Expected job to be cancelled")
            } catch (e: CancellationException) {
                // ✅ Test passes if cancellation occurs.
            }
        }

        // Allow the job to start execution before cancelling it.
        delay(100)

        // Cancel the coroutine job
        job.cancelAndJoin()
    }

}