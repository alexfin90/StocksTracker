package com.alexfin90.stockstracker


import com.alexfin90.stockstracker.contracts.StockRepository
import com.alexfin90.stockstracker.entities.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeStockRepository : StockRepository {

    private val _stocks = MutableStateFlow<List<Stock>>(emptyList())
    override val stocks: Flow<List<Stock>> = _stocks

    private val _connectionActive = MutableStateFlow(false)
    override val connectionActive: Flow<Boolean> = _connectionActive

    private val _connectionError = MutableStateFlow<String?>(null)
    override val connectionError: Flow<String?> = _connectionError

    override fun start() {
        _connectionActive.value = true
    }

    override fun stop() {
        _connectionActive.value = false
    }

    override fun observeStock(symbol: String): Flow<Stock?> =
        _stocks.map { list -> list.find { it.symbol == symbol } }

    fun emitStocks(stocks: List<Stock>) {
        _stocks.value = stocks
    }

    fun emitError(error: String?) {
        _connectionError.value = error
    }
}
