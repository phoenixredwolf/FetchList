package com.phoenixredwolf.fetchlist.ui.comoponents

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

/**
 * A composable that displays a loading indicator.
 *
 * This composable presents a [CircularProgressIndicator] centered within the available space.
 * It is used to indicate that an operation is in progress, such as data fetching or processing.
 *
 * The [CircularProgressIndicator] is given a test tag "CircularProgressIndicator" for UI testing purposes.
 */
@Composable
fun LoadingContent() {
    CenteredBox {
        CircularProgressIndicator(modifier = Modifier.testTag("CircularProgressIndicator"))
    }
}
