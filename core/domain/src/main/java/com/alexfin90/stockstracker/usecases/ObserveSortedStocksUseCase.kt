package com.alexfin90.stockstracker.usecases

import com.alexfin90.stockstracker.entities.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ObserveSortedStocksUseCase @Inject constructor(
) {
    operator fun invoke(): Flow<List<Stock>> {
        return flowOf(mockStocks.sortedByDescending { it.priceUsd })
    }

    //TODO mock remove it
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
            priceUsd = 248.90,
            previousPriceUsd = 245.10,
            updatedAtMillis = System.currentTimeMillis(),
        ),
    )
}