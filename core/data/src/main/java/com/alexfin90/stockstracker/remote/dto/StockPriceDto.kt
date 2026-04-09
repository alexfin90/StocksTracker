package com.alexfin90.stockstracker.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StockPriceDto(
    val symbol: String,
    val priceUsd: Double,
    val timestamp: Long,
)