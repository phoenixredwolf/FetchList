package com.phoenixredwolf.fetchlist.data.model

import com.squareup.moshi.JsonClass

/**
 * Represents an item fetched from a data source.
 *
 * This data class is used for data transfer and is annotated for Moshi JSON serialization.
 *
 * @property id The unique identifier of the item.
 * @property listId The identifier of the list to which the item belongs.
 * @property name The name of the item (nullable).
 */
@JsonClass(generateAdapter = true)
data class FetchItems(
    val id: Int,
    val listId: Int,
    val name: String?
)
