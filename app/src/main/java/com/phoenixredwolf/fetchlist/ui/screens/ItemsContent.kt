package com.phoenixredwolf.fetchlist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.phoenixredwolf.fetchlist.R
import com.phoenixredwolf.fetchlist.data.model.FetchItems
import com.phoenixredwolf.fetchlist.ui.comoponents.CenteredBox
import com.phoenixredwolf.fetchlist.ui.comoponents.GroupCard

/**
 * A composable that displays a list of grouped items.
 *
 * This composable presents a [LazyColumn] of [FetchItems] grouped by their `listId`.
 * If the list is empty, it displays a message indicating that the list is empty.
 *
 * @param groupedItems A map of `listId` to a list of [FetchItems] to display.
 */
@Composable
fun ItemsContent(groupedItems: Map<Int, List<FetchItems>>) {
    if (groupedItems.isEmpty()) {
        CenteredBox {
            Text(stringResource(R.string.empty_list_message))
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            groupedItems.forEach { (listId, items) ->
                item {
                    GroupCard(listId = listId, items = items)
                }
            }
        }
    }
}
