package com.phoenixredwolf.fetchlist.data.network

import com.phoenixredwolf.fetchlist.data.model.FetchItems
import retrofit2.http.GET
import java.io.IOException

/**
 * Interface representing the API service for fetching items.
 *
 * This interface defines the endpoints and methods for interacting with the remote data source.
 * It uses Retrofit annotations to specify the HTTP requests.
 */
interface ApiService {

    /**
     * Retrieves a list of items from the "/hiring.json" endpoint.
     *
     * This method performs a GET request to fetch the items.
     *
     * @return A list of [FetchItems] objects representing the fetched items.
     * @throws IOException If an I/O error occurs during the network request.
     */
    @Throws(IOException::class)
    @GET("/hiring.json")
    suspend fun getItems(): List<FetchItems>

}
