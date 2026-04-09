package com.alexfin90.stockstracker.navigation

import kotlinx.serialization.Serializable

interface Route {

    @Serializable
    data object StocksFeed : Route

    @Serializable
    data class StockDetail(val symbol: String) : Route
}