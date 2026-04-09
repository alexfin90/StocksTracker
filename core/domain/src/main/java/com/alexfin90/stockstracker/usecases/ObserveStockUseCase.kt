package com.alexfin90.stockstracker.usecases

import com.alexfin90.stockstracker.contracts.StockRepository
import com.alexfin90.stockstracker.entities.Stock
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveStockUseCase @Inject constructor(
    private val stockRepository: StockRepository,
) {
    operator fun invoke(symbol: String): Flow<Stock?> =
        stockRepository.observeStock(symbol)
}