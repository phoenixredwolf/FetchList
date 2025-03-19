package com.phoenixredwolf.fetchlist.data.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An OkHttp interceptor that logs the HTTP response code and returns the response as-is.
 *
 * This interceptor is designed to be used with Retrofit or other OkHttp clients.
 * It intercepts the HTTP response after it has been received from the server, logs the response code,
 * and then allows the response to proceed to the calling code without throwing exceptions.
 *
 * This approach allows for more flexible error handling in the application layer,
 * as the calling code can inspect the response code and handle errors accordingly.
 */
class ErrorInterceptor : Interceptor {

    private val TAG = "ErrorInterceptor"

    /**
     * Intercepts the HTTP response, logs the response code, and returns the response.
     *
     * This method is called by OkHttp when a response is received from the server.
     * It logs the response code using println and then returns the response without modification.
     *
     * @param chain The [Interceptor.Chain] object that allows the interceptor to proceed with the request and response.
     * @return The [Response] object received from the server.
     */
    override fun intercept(chain: Interceptor.Chain): Response {

        // Proceed with the request and get the response.
        val response = chain.proceed(chain.request())

        // Log the response code for debugging or monitoring purposes.
        Log.d(TAG, "Response code: ${response.code}")

        // Instead of throwing an exception, return the error response
        // This allows better error handling in the calling code
        return response
    }
}
