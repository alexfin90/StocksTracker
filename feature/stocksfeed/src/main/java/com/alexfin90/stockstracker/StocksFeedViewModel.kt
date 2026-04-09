package com.alexfin90.stockstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexfin90.StocksFeedViewModelContract
import com.alexfin90.stockstracker.dispatcher.DefaultDispatcher
import com.alexfin90.stockstracker.mappers.toUiModel
import com.alexfin90.stockstracker.usecases.ObserveConnectionErrorUseCase
import com.alexfin90.stockstracker.usecases.ObserveSortedStocksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class StocksFeedViewModel @Inject constructor(
    observeSortedStocksUseCase: ObserveSortedStocksUseCase,
    observeConnectionErrorUseCase: ObserveConnectionErrorUseCase,
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel(), StocksFeedViewModelContract {

    override val uiState: StateFlow<StocksFeedScreenState> =
        combine(
            observeSortedStocksUseCase(),
            observeConnectionErrorUseCase(),
        ) { stocks, error ->
            StocksFeedScreenState(
                isLoading = false,
                stocks = stocks.map { it.toUiModel() }.toPersistentList(),
                error = if (error != null) "$error\nTrying to reconnect..." else null,
            )
        }.flowOn(defaultDispatcher).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StocksFeedScreenState(isLoading = true),
        )
}