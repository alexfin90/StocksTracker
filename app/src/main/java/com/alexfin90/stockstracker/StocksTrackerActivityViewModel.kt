package com.alexfin90.stockstracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexfin90.stockstracker.usecases.ObserveConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeConnectionUseCase: ObserveConnectionUseCase,
) : ViewModel(), StocksTrackerActivityViewModelContract {

    override val connectionActive = observeConnectionUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    override fun onToggleConnection() {
    }
}