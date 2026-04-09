package com.alexfin90.stockstracker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.alexfin90.stockstracker.mappers.toUiModel
import com.alexfin90.stockstracker.navigation.Route
import com.alexfin90.stockstracker.usecases.ObserveConnectionErrorUseCase
import com.alexfin90.stockstracker.usecases.ObserveStockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class StockDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeStockUseCase: ObserveStockUseCase,
    observeConnectionErrorUseCase: ObserveConnectionErrorUseCase,
) : ViewModel(), StocksDetailViewModelContract {

    private val symbol: String = savedStateHandle.toRoute<Route.StockDetail>().symbol

    override val uiState: StateFlow<StockDetailScreenState> =
        combine(
            observeStockUseCase(symbol),
            observeConnectionErrorUseCase(),
        ) { stock, error ->
            StockDetailScreenState(
                stock = stock?.toUiModel(),
                error = if (stock == null && error != null) {
                    "$error\nTrying to reconnect..."
                } else {
                    null
                }
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StockDetailScreenState(),
        )
}