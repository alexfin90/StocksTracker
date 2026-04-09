package com.alexfin90.stockstracker.model

data class StockPriceEvent(
    val symbol: String,
    val priceUsd: Double,
    val timestamp: Long,
)