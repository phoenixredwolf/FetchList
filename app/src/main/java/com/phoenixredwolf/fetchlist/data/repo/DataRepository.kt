package com.phoenixredwolf.fetchlist.data.repo

import com.phoenixredwolf.fetchlist.data.model.FetchItems
import java.io.IOException

/**
 * Interface representing the data repository for fetching and managing items.
 *
 * This interface defines the contract for accessing and manipulating item data.
 * Implementations of this interface are responsible for providing data from various sources,
 * such as local databases or remote APIs.
 */
interface DataRepository {

    /**
     * Retrieves a filtered list of items.
     *
     * This method fetches items from the data source and applies any necessary filtering or processing.
     *
     * @return A list of [FetchItems] objects representing the filtered items.
     * @throws IOException If an I/O error occurs during data retrieval, such as a network issue.
     */
    @Throws(IOException::class)
    suspend fun getFilteredItems(): List<FetchItems>
}