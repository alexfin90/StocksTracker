package com.alexfin90.stockstracker.repositories

import com.alexfin90.stockstracker.contracts.StockRepository
import com.alexfin90.stockstracker.entities.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

class StockRepositoryMockImpl @Inject constructor() : StockRepository {

    private val mockStocks = listOf(
        Stock(
            symbol = "AAPL",
            name = "Apple Inc.",
            description = "Apple designs, manufactures, and markets smartphones, personal computers, tablets, wearables, and accessories.",
            priceUsd = 189.84,
            previousPriceUsd = 187.50,
            updatedAtMillis = System.currentTimeMillis(),
        ),
        Stock(
            symbol = "GOOGL",
            name = "Alphabet Inc.",
            description = "Alphabet is a holding company that gives ambitious projects the resources, freedom, and focus to make their ideas happen.",
            priceUsd = 141.70,
            previousPriceUsd = 142.30,
            updatedAtMillis = System.currentTimeMillis(),
        ),
        Stock(
            symbol = "MSFT",
            name = "Microsoft Corp.",
            description = "Microsoft develops and supports software, services, devices, and solutions worldwide.",
            priceUsd = 415.60,
            previousPriceUsd = 410.20,
            updatedAtMillis = System.currentTimeMillis(),
        ),
        Stock(
            symbol = "AMZN",
            name = "Amazon.com Inc.",
            description = "Amazon engages in the retail sale of consumer products, advertising, and subscription services.",
            priceUsd = 185.25,
            previousPriceUsd = 186.00,
            updatedAtMillis = System.currentTimeMillis(),
        ),
        Stock(
            symbol = "TSLA",
            name = "Tesla Inc.",
            description = "Tesla designs, develops, manufactures, and sells fully electric vehicles, energy generation, and storage systems.",
            priceUsd = 948.90,
            previousPriceUsd = 245.10,
            updatedAtMillis = System.currentTimeMillis(),
        ),
    )

    private val _stocks = MutableStateFlow(mockStocks)
    override val stocks: StateFlow<List<Stock>> = _stocks.asStateFlow()

    private val _connectionActive = MutableStateFlow(false)
    override val connectionActive: StateFlow<Boolean> = _connectionActive.asStateFlow()

    override fun start() {
        Timber.d("start")
        _connectionActive.update {
            true
        }
    }

    override fun stop() {
        Timber.d("stop")
        _connectionActive.update {
            false
        }
    }

    override fun observeStock(symbol: String): Flow<Stock?> =
        _stocks.map { list -> list.find { it.symbol == symbol } }
}