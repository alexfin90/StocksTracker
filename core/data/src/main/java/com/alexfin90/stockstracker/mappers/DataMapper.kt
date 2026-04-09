package com.alexfin90.stockstracker.mappers

import com.alexfin90.stockstracker.entities.Stock
import com.alexfin90.stockstracker.model.StockData
import com.alexfin90.stockstracker.model.StockPriceEvent
import com.alexfin90.stockstracker.remote.dto.StockPriceDto


fun StockData.toDomain(): Stock =
    Stock(
        symbol = symbol,
        name = name,
        description = description,
        priceUsd = basePriceUsd,
        previousPriceUsd = null,
        updatedAtMillis = System.currentTimeMillis(),
    )

fun StockPriceDto.toStockPriceEvent(): StockPriceEvent =
    StockPriceEvent(
        symbol = symbol,
        priceUsd = priceUsd,
        timestamp = timestamp,
    )


