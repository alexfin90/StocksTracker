package com.alexfin90.stockstracker.uimodels

import androidx.compose.runtime.Immutable

@Immutable
data class UiStock(
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val isUp: Boolean
)