package com.phoenixredwolf.fetchlist.utils

import android.util.Log

/**
 * CustomLogger is a concrete implementation of the [Logger] interface, providing a logging mechanism
 * using Android's built-in Log.e() method.
 *
 * This class is designed to be used in Android applications where error logging is required. It encapsulates
 * the Android Log.e() functionality, allowing for a more abstracted and testable logging approach.
 *
 * Reasoning for Creation:
 * -----------------------
 *
 * 1. Abstraction and Testability:
 * - By creating an interface [Logger] and a concrete implementation [CustomLogger], we decouple
 * the logging mechanism from the classes that use it (e.g., DataRepositoryImpl).
 * - This allows for easier testing, as we can mock the [Logger] interface in unit tests and verify
 * that the logError() method is called with the expected arguments.
 * - It also allows for easy switching of logging mechanisms in the future, if needed, without modifying
 * the classes that use the logger.
 *
 * 2. Encapsulation of Android Log.e():
 * - This class encapsulates the Android-specific Log.e() method, making it easier to manage and
 * potentially replace with other logging solutions (e.g., Timber) without impacting other
 * parts of the application.
 *
 * 3. Consistent Logging:
 * - By using a centralized logging class, we can ensure consistent logging practices throughout
 * the application.
 *
 * 4. Nullable Throwable Support:
 * - The [throwable] parameter is nullable. This allows the logger to be used in cases where an
 * exception might not always be present, but we still want to log an error message.
 *
 * Example Usage:
 * --------------
 *
 * val logger = CustomLogger()
 * logger.logError("MyTag", "An error occurred", myException)
 *
 * @see Logger
 */
class CustomLogger : Logger {

    /**
     * Logs an error message with the given tag, message, and optional throwable.
     *
     * @param tag The tag used to identify the source of the log message.
     * @param message The error message to be logged.
     * @param throwable An optional throwable associated with the error. Can be null if no exception is available.
     */
    override fun logError(tag: String, message: String, throwable: Throwable?) {
        Log.e(tag, message, throwable)
    }
}