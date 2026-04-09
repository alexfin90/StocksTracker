package com.alexfin90.stockstracker.usecases

import com.alexfin90.stockstracker.contracts.StockRepository
import javax.inject.Inject

class StopConnectionUseCase @Inject constructor(
    private val stockRepository: StockRepository,
) {
    operator fun invoke() = stockRepository.stop()
}