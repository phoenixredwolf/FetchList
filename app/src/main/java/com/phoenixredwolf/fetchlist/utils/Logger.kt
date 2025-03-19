package com.phoenixredwolf.fetchlist.utils

interface Logger {
    fun logError(tag: String, message: String, throwable: Throwable? = null)
}