package com.alexfin90.stockstracker.usecases

import com.alexfin90.stockstracker.contracts.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConnectionUseCase @Inject constructor(
    private val stockRepository: StockRepository,
) {
    operator fun invoke(): Flow<Boolean> =
        stockRepository.connectionActive
}