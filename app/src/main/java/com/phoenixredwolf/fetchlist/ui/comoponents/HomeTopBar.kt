package com.phoenixredwolf.fetchlist.ui.comoponents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.phoenixredwolf.fetchlist.R

/**
 * A composable that displays the top app bar for the home screen.
 *
 * This composable presents a [TopAppBar] with a title and a refresh button.
 * The title is obtained from the string resource `R.string.screen_title`,
 * and the refresh button uses the `Icons.Filled.Refresh` icon with a content description from
 * `R.string.refresh_icon_description`.
 *
 * @param onRefresh A callback function that is invoked when the refresh button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(onRefresh: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.screen_title)) },
        actions = {
            IconButton(onClick = onRefresh) {
                Icon(Icons.Filled.Refresh, stringResource(R.string.refresh_icon_description))
            }
        }
    )
}
