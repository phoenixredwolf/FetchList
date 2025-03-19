package com.phoenixredwolf.fetchlist.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        mockWebServer.start(0)

        val baseUrl = "http://localhost:${mockWebServer.port}/"

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(RetryInterceptor())
            .addInterceptor(ErrorInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `api service should handle retry logic`() = runTest {
        // Arrange: Prepare a 500 response that will be retried.
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("[]")) // On retry, success

        // Act: Call the ApiService's getItems() method.
        val items = apiService.getItems()

        // Assert: Verify that the response is not null and is an empty list.
        assertNotNull(items)
        assertTrue(items.isEmpty())
        assertEquals(2, mockWebServer.requestCount) // Ensure that the request was made twice.
    }

    @Test
    fun `api service should fetch data successfully`() = runTest {
        // Arrange: Prepare a successful 200 response with valid JSON.
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                [
                    {"id": 1, "listId": 1, "name": "Item 1"},
                    {"id": 2, "listId": 2, "name": "Item 2"}
                ]
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        // Act: Call the ApiService's getItems() method.
        val items = apiService.getItems()

        // Assert: Verify that the response is not null and contains data.
        assertNotNull(items)
        assert(items.isNotEmpty())
    }

    @Test
    fun `api service should handle 404 Not Found`() = runTest {
        // Arrange: Prepare a 404 Not Found response.
        val mockResponse = MockResponse().setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        // Act and Assert: Verify that an HttpException is thrown for 404.
        val deferred = CompletableDeferred<Result<Any?>>()

        try {
            apiService.getItems()
        } catch (e: Exception) {
            deferred.complete(Result.failure(e))
        }

        deferred.await().onFailure { e ->
            assertTrue(e is HttpException)
            assertEquals("HTTP 404 Client Error", e.message)
            assertEquals(404, (e as HttpException).code())
        }
    }

    @Test
    fun `api service should handle 500 Internal Server Error`() = runBlocking {
        // Arrange: Prepare a 500 Internal Server Error response
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("Server Error")
        mockWebServer.enqueue(mockResponse)

        // For retry attempts
        repeat(2) {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(500)
                    .setBody("Server Error")
            )
        }

        // Act & Assert
        try {
            apiService.getItems()
            fail("Expected HttpException for 500 Internal Server Error")
        } catch (e: HttpException) {
            assertEquals(500, e.code())
        }
    }

    @Test
    fun `api service should handle socket timeout`() = runTest {
        // Arrange: Configure MockWebServer to simulate a timeout
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        // Act and Assert: Verify that an IOException is thrown
        val exception = assertThrows<IOException> {
            apiService.getItems()
        }

        // Verify that the exception is related to timeout
        assertTrue(
            "Expected timeout-related exception, but got: ${exception.javaClass.simpleName}",
            exception is SocketTimeoutException ||
                    exception.message?.contains("timeout", ignoreCase = true) == true
        )
    }

    @Test
    fun `api service should handle malformed JSON`() = runTest {
        // Arrange: Prepare a 200 response with malformed JSON.
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("This is not valid JSON")
        mockWebServer.enqueue(mockResponse)

        // Act and Assert: Verify that an IOException is thrown for malformed JSON.
        try {
            apiService.getItems()
            fail("Expected IOException for malformed JSON")
        } catch (e: IOException) {
            // Test passes if IOException is thrown.
        }
    }

    @Test
    fun `api service should handle empty response`() = runTest {
        // Arrange: Prepare a 200 response with an empty JSON array.
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("[]")
        mockWebServer.enqueue(mockResponse)

        // Act: Call the ApiService's getItems() method.
        val items = apiService.getItems()

        // Assert: Verify that the response is not null and is an empty list.
        assertNotNull(items)
        assertTrue(items.isEmpty())
    }

    @Test
    fun `api service should handle network timeout`() = runTest {
        // Arrange: Simulate a network timeout by not sending a response.

        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        // Act & Assert: Verify that an IOException is thrown.
        assertThrows<IOException> {
            apiService.getItems()
        }
    }

    @Test
    fun `api service should handle connection refused`() = runTest {
        // Arrange: Shutdown mockwebserver to simulate a connection refused.
        mockWebServer.shutdown()

        // Act and Assert: Verify that an IOException is thrown for connection refused.
        try {
            apiService.getItems()
            fail("Expected IOException for connection refused")
        } catch (e: IOException) {
            // Test passes if IOException is thrown.
        }
    }

    @Test
    fun `api service should handle large data sets`() = runTest {
        //Arrange
        val largeJson = (1..1000).joinToString(",", "[", "]"){"{\"id\": $it, \"listId\": $it, \"name\": \"Item $it\"}"}
        val mockResponse = MockResponse().setResponseCode(200).setBody(largeJson)
        mockWebServer.enqueue(mockResponse)

        //Act
        val items = apiService.getItems()

        //Assert
        assertEquals(1000, items.size)
    }
}