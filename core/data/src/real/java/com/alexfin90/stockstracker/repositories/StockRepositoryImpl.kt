package com.alexfin90.stockstracker.repositories

import com.alexfin90.stockstracker.contracts.StockRepository
import com.alexfin90.stockstracker.entities.Stock
import kotlinx.coroutines.flow.Flow

class StockRepositoryImpl : StockRepository {
    override val stocks: Flow<List<Stock>>
        get() = TODO("Not yet implemented")
    override val connectionActive: Flow<Boolean>
        get() = TODO("Not yet implemented")
    override val connectionError: Flow<String?>
        get() = TODO("Not yet implemented")

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }

    override fun observeStock(symbol: String): Flow<Stock?> {
        TODO("Not yet implemented")
    }
}