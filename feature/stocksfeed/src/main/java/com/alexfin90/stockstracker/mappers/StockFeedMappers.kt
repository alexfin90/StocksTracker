package com.alexfin90.stockstracker.mappers

import com.alexfin90.stockstracker.entities.Stock
import com.alexfin90.stockstracker.uimodels.UiStock

fun Stock.toUiModel() = UiStock(
    symbol = this.symbol,
    name = this.name,
    priceUsd = this.priceUsd,
    isUp = this.isUp == true
)