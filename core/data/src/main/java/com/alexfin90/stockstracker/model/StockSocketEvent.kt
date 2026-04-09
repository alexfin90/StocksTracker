package com.alexfin90.stockstracker.model

sealed interface StockSocketEvent {
    data object Connected : StockSocketEvent
    data object Disconnected : StockSocketEvent
    data class PriceUpdateReceived(val value: StockPriceEvent) : StockSocketEvent
    data class Failure(val throwable: Throwable) : StockSocketEvent
}