package com.phoenixredwolf.fetchlist.data.network
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * An OkHttp interceptor that retries HTTP requests for 5xx server errors and network issues.
 *
 * This interceptor implements an exponential backoff retry strategy.
 * It retries up to [maxRetry] times for server errors (5xx) and network errors (IOException).
 *
 * For each retry attempt, it introduces a delay that increases exponentially.
 * This helps to avoid overwhelming the server during transient failures.
 */
class RetryInterceptor : Interceptor {

    private val TAG = "RetryInterceptor"
    private val maxRetry = 3

    /**
     * Intercepts an HTTP request and retries it if necessary.
     *
     * This method handles 5xx server errors and network errors (IOException) by retrying the request.
     * It uses an exponential backoff strategy, increasing the delay between retries.
     *
     * @param chain The [Interceptor.Chain] object that allows the interceptor to proceed with the request.
     * @return The [Response] object received from the server, or the last error response if max retries are reached.
     * @throws IOException If the maximum number of retries is exceeded due to network errors.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var retryCount = 0
        val lastResponse: Response? = null

        repeat(maxRetry) {
            try {
                lastResponse?.close() // Close any previous response
                val response = chain.proceed(request)
                Log.d(TAG, "Response code: ${response.code}")

                if (response.isSuccessful) {
                    return response
                }

                // For 5xx errors, retry
                if (response.code in 500..599) {
                    retryCount++
                    if (retryCount < maxRetry) {
                        response.close()
                        Log.w(TAG, "Retry attempt #$retryCount for 5xx error")
                        Thread.sleep((1000 * retryCount).toLong()) // Exponential backoff
                    } else {
                        return response // Return the last error response if max retries reached
                    }
                } else {
                    return response // Return non-5xx error responses without retry
                }

            } catch (e: IOException) {
                retryCount++
                if (retryCount >= maxRetry) {
                    throw IOException("Max retries exceeded due to network error: ${e.localizedMessage}", e)
                }
                println("Retry attempt #$retryCount due to network error: ${e.localizedMessage}")
                Thread.sleep((1000 * retryCount).toLong()) // Exponential backoff
            }
        }

        // This should never be reached due to the logic above, but keeping it as a safeguard
        return lastResponse ?: throw IOException("Max retries exceeded with no response")
    }
}
