package com.alexfin90.stockstracker.entities

data class Stock(
    val symbol: String,
    val name: String,
    val description: String,
    val priceUsd: Double,
    val previousPriceUsd: Double?,
    val updatedAtMillis: Long,
) {
    val isUp: Boolean?
        get() = previousPriceUsd?.let { priceUsd > it }
}