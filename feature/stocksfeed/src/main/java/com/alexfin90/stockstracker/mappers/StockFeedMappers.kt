package com.alexfin90.stockstracker.mappers

import com.alexfin90.stockstracker.designsystem.atomic.molecules.UiStockRow
import com.alexfin90.stockstracker.entities.Stock

fun Stock.toUiModel() = UiStockRow(
    symbol = this.symbol,
    name = this.name,
    priceUsd = this.priceUsd,
    isUp = this.isUp == true
)