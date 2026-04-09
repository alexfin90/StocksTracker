package com.alexfin90.stockstracker.mappers

import com.alexfin90.stockstracker.entities.Stock
import com.alexfin90.stockstracker.model.StockData


fun StockData.toDomain(): Stock =
    Stock(
        symbol = symbol,
        name = name,
        description = description,
        priceUsd = basePriceUsd,
        previousPriceUsd = null,
        updatedAtMillis = System.currentTimeMillis(),
    )


