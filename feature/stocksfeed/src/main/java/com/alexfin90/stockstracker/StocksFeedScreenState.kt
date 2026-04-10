package com.alexfin90.stockstracker

import androidx.compose.runtime.Immutable
import com.alexfin90.stockstracker.designsystem.atomic.molecules.UiStockRow
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class StocksFeedScreenState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val stocks: PersistentList<UiStockRow> = persistentListOf()
)