# FetchList Android Application

This Android application demonstrates fetching, grouping, and displaying a list of items from a remote API. It utilizes modern Android development practices, including Jetpack Compose, Kotlin Coroutines, Koin for dependency injection, and Retrofit for network communication.

## Features

* **Data Fetching:** Fetches a list of items from a remote API.
* **Data Grouping and Sorting:** Groups items by `listId` and sorts them numerically by the digits in their `name`.
* **Jetpack Compose UI:** Modern and declarative UI using Jetpack Compose.
* **Pull-to-Refresh:** Allows users to refresh the list by pulling down on the screen.
* **Loading and Error Handling:** Displays appropriate UI states for loading, success, and error scenarios.
* **Dependency Injection:** Uses Koin for efficient dependency management.
* **Retrofit Network Communication:** Utilizes Retrofit for network requests.
* **Error Interceptor and Retry Interceptor:** Implements OkHttp interceptors for error handling and retry logic.
* **Splash Screen:** Features an animated splash screen.
* **Accessibility:** Includes semantic descriptions for UI elements.
* **Logging:** uses the Android Log system for debugging.

## Technologies Used

* **Kotlin:** Programming language.
* **Jetpack Compose:** UI framework.
* **Kotlin Coroutines:** Asynchronous programming.
* **Koin:** Dependency injection.
* **Retrofit:** Network communication.
* **OkHttp:** HTTP client.
* **Moshi:** JSON parsing.
* **Android Architecture Components:** ViewModel, StateFlow.
* **Material 3:** Compose Material Design.

## Getting Started

### Prerequisites

* Android Studio
* Git

### Installation

1.  Clone the repository:

    ```bash
    git clone [repository URL]
    ```

2.  Open the project in Android Studio.

3.  Build and run the application on an emulator or physical device.

### Configuration

* The base URL for the API is defined in the `BuildConfig.BASE_URL` field.
* The application uses Koin for dependency injection, with the `appModule` defined in `com.phoenixredwolf.fetchlist.di`.
* Network interceptors are configured in the `OkHttpClient` within the `appModule`.

### Dependencies

Dependencies are managed using Gradle. You can find the dependencies in the `build.gradle.kts` files.
