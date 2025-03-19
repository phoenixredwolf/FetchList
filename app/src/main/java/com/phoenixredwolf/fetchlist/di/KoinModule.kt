package com.phoenixredwolf.fetchlist.di

import com.phoenixredwolf.fetchlist.BuildConfig
import com.phoenixredwolf.fetchlist.data.network.ApiService
import com.phoenixredwolf.fetchlist.data.network.ErrorInterceptor
import com.phoenixredwolf.fetchlist.data.network.RetryInterceptor
import com.phoenixredwolf.fetchlist.data.repo.DataRepository
import com.phoenixredwolf.fetchlist.data.repo.DataRepositoryImpl
import com.phoenixredwolf.fetchlist.ui.viewmodel.HomeViewModel
import com.phoenixredwolf.fetchlist.utils.CustomLogger
import com.phoenixredwolf.fetchlist.utils.Logger
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Koin module for providing dependencies for the application.
 *
 * This module configures and provides instances of various components, including
 * network-related classes, data repositories, and view models.
 */
val appModule = module {

    /**
     * Provides a singleton instance of [Logger].
     *
     * This instance is used for logging throughout the application.
     */
    single<Logger> { CustomLogger() }

    /**
     * Provides a singleton instance of [OkHttpClient].
     *
     * This client is configured with logging, retry, and error interceptors.
     */
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Or BASIC, HEADERS, NONE
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(RetryInterceptor())
            .addInterceptor(ErrorInterceptor())
            .build()
    }

    /**
     * Provides a singleton instance of [Retrofit].
     *
     * This instance is configured with the base URL, OkHttpClient, and Moshi converter factory.
     */
    single {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * Provides a singleton instance of [ApiService].
     *
     * This interface is created using the configured Retrofit instance.
     */
    single { get<Retrofit>().create(ApiService::class.java) }

    /**
     * Provides a singleton instance of [DataRepository].
     *
     * This interface is implemented by [DataRepositoryImpl] and depends on [ApiService] and [Logger].
     */
    singleOf(::DataRepositoryImpl) { bind<DataRepository>() }

    /**
     * Provides a new instance of [HomeViewModel] for each injection.
     *
     * This view model depends on [DataRepository].
     */
    viewModelOf(::HomeViewModel)
}