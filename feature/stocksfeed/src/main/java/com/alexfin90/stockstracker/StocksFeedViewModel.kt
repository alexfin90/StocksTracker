package com.alexfin90.stockstracker

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StocksFeedViewModel @Inject constructor(
) : ViewModel()