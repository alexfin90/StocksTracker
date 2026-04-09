package com.alexfin90.stockstracker

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow

@Stable
interface StocksDetailViewModelContract {
    val uiState: StateFlow<StockDetailScreenState>
}