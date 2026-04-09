package com.alexfin90.stockstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexfin90.StocksFeedViewModelContract
import com.alexfin90.stockstracker.mappers.toUiModel
import com.alexfin90.stockstracker.usecases.ObserveSortedStocksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class StocksFeedViewModel @Inject constructor(
    private val observeSortedStocksUseCase: ObserveSortedStocksUseCase,
) : ViewModel(), StocksFeedViewModelContract {

    private var _uiState = MutableStateFlow(StocksFeedScreenState())
    override val uiState: StateFlow<StocksFeedScreenState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeSortedStocksUseCase().collect { stocks ->
                Timber.d("stocks: $stocks")
                _uiState.update {
                    StocksFeedScreenState(
                        isLoading = false,
                        stocks = stocks.map { it.toUiModel() }.toPersistentList(),
                        error = null,
                    )
                }
            }
        }
    }
}