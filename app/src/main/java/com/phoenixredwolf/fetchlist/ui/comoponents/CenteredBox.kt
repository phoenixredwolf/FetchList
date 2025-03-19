package com.phoenixredwolf.fetchlist.ui.comoponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * A composable that centers its content within the available space.
 *
 * This composable uses a [Box] with [Modifier.fillMaxSize] and [Alignment.Center]
 * to center its content both horizontally and vertically.
 *
 * @param content The composable content to be centered.
 */
@Composable
fun CenteredBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}