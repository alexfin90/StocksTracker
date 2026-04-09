package com.alexfin90.stockstracker.uimodels

import androidx.compose.runtime.Immutable

@Immutable
data class UiStockDetail(
    val symbol: String,
    val name: String,
    val description: String,
    val priceUsd: Double,
    val isIncrease: Boolean
)