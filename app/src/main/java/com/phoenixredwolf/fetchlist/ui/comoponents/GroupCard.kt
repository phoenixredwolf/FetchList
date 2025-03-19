package com.phoenixredwolf.fetchlist.ui.comoponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.phoenixredwolf.fetchlist.data.model.FetchItems

/**
 * A composable that displays a group of items in a card format.
 *
 * This composable presents a list of [FetchItems] grouped by their `listId`.
 * It displays the `listId` as a title and the item names in a flow layout.
 *
 * @param listId The identifier of the list to which the items belong.
 * @param items The list of [FetchItems] to display in the card.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GroupCard(listId: Int, items: List<FetchItems>) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer),
            elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
            ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "List ID: $listId",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.semantics {
                        contentDescription = "List ID: $listId"
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Space between title and data
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f) // Semi-transparent
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    item.name?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(end = 16.dp, bottom = 8.dp)
                                .width(70.dp)
                                .semantics {
                                    contentDescription = it
                                },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GroupCardPreview() {
    val items = listOf(FetchItems(684, 1, "Item 684"), FetchItems(276,1, "Item 276"))
    GroupCard(
        listId = items[0].listId,
        items = items,
    )
}