package com.alexfin90.stockstracker.contracts

import com.alexfin90.stockstracker.entities.Stock
import kotlinx.coroutines.flow.Flow


interface StockRepository {
    val stocks: Flow<List<Stock>>
    val connectionActive: Flow<Boolean>
    val connectionError: Flow<String?>

    fun start()
    fun stop()
    fun observeStock(symbol: String): Flow<Stock?>
}