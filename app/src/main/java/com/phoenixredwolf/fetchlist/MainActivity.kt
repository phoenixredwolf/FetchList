package com.phoenixredwolf.fetchlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.phoenixredwolf.fetchlist.ui.navigation.AppNavigator
import com.phoenixredwolf.fetchlist.ui.theme.FetchListTheme

/**
 * The main activity for the FetchList application.
 *
 * This activity sets up the Compose UI and manages the application's navigation.
 * It uses a [Scaffold] to provide a basic layout structure and integrates the [AppNavigator]
 * to handle navigation between different screens.
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is starting.
     *
     * This method initializes the Compose UI by:
     * - Enabling edge-to-edge display.
     * - Setting the content of the activity to a [Scaffold] with a background color.
     * - Integrating the [AppNavigator] to handle navigation.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     * Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FetchListTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { _ ->
                    AppNavigator()
                }
            }
        }
    }
}
