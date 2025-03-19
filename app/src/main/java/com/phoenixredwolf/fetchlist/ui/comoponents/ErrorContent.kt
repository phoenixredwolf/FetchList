package com.phoenixredwolf.fetchlist.ui.comoponents

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.phoenixredwolf.fetchlist.R

/**
 * A composable that displays an error message and a retry button.
 *
 * This composable is used to indicate that an error has occurred and provide a way to retry the operation.
 * It displays the error message using a [Text] composable and a retry button using a [Button] composable.
 *
 * @param errorMessage The error message to display. If null, a default error message is used.
 * @param onRetry A callback function that is invoked when the retry button is clicked.
 */
@Composable
fun ErrorContent(errorMessage: String?, onRetry: () -> Unit) {
    CenteredBox {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Error: ${errorMessage ?: stringResource(R.string.error_message_default)}")
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry_button_text))
            }
        }
    }
}