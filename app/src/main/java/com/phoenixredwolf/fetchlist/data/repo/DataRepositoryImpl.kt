package com.phoenixredwolf.fetchlist.data.repo

import com.phoenixredwolf.fetchlist.data.model.FetchItems
import com.phoenixredwolf.fetchlist.data.network.ApiService
import com.phoenixredwolf.fetchlist.utils.Logger
import java.io.IOException

/**
 * Implementation of the [DataRepository] interface.
 *
 * This class is responsible for fetching and processing [FetchItems] from the network using an [ApiService].
 * It filters out items with blank or null names and sorts the remaining items by listId and then by name.
 *
 * @property apiService The [ApiService] used to fetch data from the network.
 * @property logger The [Logger] used for logging errors.
 */
class DataRepositoryImpl(
    private val apiService: ApiService,
    private val logger: Logger
) : DataRepository {

    /**
     * Retrieves a list of [FetchItems] from the network, filters out items with blank or null names,
     * and sorts the remaining items by listId and then by name.
     *
     * @return A sorted list of [FetchItems] with valid names.
     * @throws IOException if a network error occurs during data retrieval.
     * @throws Exception if any other error occurs during data retrieval or processing.
     */
    override suspend fun getFilteredItems(): List<FetchItems> {

        val tag = "DataRepositoryImpl"

        try {
            // Fetch items from the ApiService, or return an empty list if null is returned.
            val items = apiService.getItems() ?: emptyList()

            // Filter out items with blank or null names and sort by listId and name.
            return items.filter { !it.name.isNullOrBlank() }
                .sortedWith(compareBy<FetchItems> { it.listId})

        } catch (e: IOException) {
            logger.logError(tag, "Network error:", e)
            // Re-throw IOException to propagate network errors.
            throw e
        } catch (e: Exception) {
            logger.logError(tag, "Error retrieving data:", e)
            // Re-throw other exceptions.
            throw e
        }
    }
}