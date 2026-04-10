package com.alexfin90.stockstracker.designsystem.atomic.molecules

import androidx.compose.runtime.Immutable

@Immutable
data class UiStockRow(
    val symbol: String,
    val name: String,
    val priceUsd: Double,
    val isUp: Boolean
)