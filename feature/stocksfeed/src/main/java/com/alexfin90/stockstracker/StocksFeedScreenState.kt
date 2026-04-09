package com.alexfin90.stockstracker

import androidx.compose.runtime.Immutable


@Immutable
data class StocksFeedScreenState(
    val isLoading: Boolean = true,
    val error: String? = null,
    //TODO list
)
