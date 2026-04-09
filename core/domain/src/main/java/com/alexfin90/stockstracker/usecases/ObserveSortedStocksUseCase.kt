package com.alexfin90.stockstracker.usecases

import com.alexfin90.stockstracker.contracts.StockRepository
import com.alexfin90.stockstracker.entities.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveSortedStocksUseCase @Inject constructor(
    private val stockRepository: StockRepository,
) {
    operator fun invoke(): Flow<List<Stock>> = stockRepository.stocks.map { stocks ->
        stocks.sortedByDescending { it.priceUsd }
    }
}