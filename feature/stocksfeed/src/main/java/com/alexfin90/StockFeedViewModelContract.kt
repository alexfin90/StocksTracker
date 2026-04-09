package com.alexfin90

import androidx.compose.runtime.Stable
import com.alexfin90.stockstracker.StocksFeedScreenState
import kotlinx.coroutines.flow.StateFlow

@Stable
interface StocksFeedViewModelContract {
    val uiState: StateFlow<StocksFeedScreenState>
}