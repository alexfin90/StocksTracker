package com.alexfin90.stockstracker.mappers

import com.alexfin90.stockstracker.entities.Stock
import com.alexfin90.stockstracker.uimodels.UiStockDetail

fun Stock.toUiModel(): UiStockDetail {
    return UiStockDetail(
        symbol = this.symbol,
        name = this.name,
        description = this.description,
        priceUsd = this.priceUsd,
        isUp = this.isUp == true
    )
}