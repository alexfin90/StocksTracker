package com.alexfin90.stockstracker

import androidx.compose.runtime.Immutable
import com.alexfin90.stockstracker.uimodels.UiStockDetail

@Immutable
data class StockDetailScreenState(
    val stock: UiStockDetail? = null,
    val error: String? = null
)