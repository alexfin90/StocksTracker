package com.alexfin90.stockstracker.remote

import com.alexfin90.stockstracker.dispatcher.ApplicationScope
import com.alexfin90.stockstracker.dispatcher.IoDispatcher
import com.alexfin90.stockstracker.model.StockPriceEvent
import com.alexfin90.stockstracker.model.StockSocketEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class StockMockDataSource @Inject constructor(
    @param:ApplicationScope private val applicationScope: CoroutineScope,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    companion object {
        private const val FEED_INTERVAL_MS = 2000L
        private const val PRICE_SWING_PERCENT = 0.05
    }

    private val _events = MutableSharedFlow<StockSocketEvent>(extraBufferCapacity = 64)
    val events: SharedFlow<StockSocketEvent> = _events.asSharedFlow()

    private var feedJob: Job? = null
    private var currentPrices: MutableMap<String, Double> = mutableMapOf()

    fun connect(initialPrices: Map<String, Double>) {
        Timber.d("MockDataSource connect")
        currentPrices = initialPrices.toMutableMap()
        _events.tryEmit(StockSocketEvent.Connected)
        startFeed()
    }

    fun disconnect() {
        Timber.d("MockDataSource disconnect")
        feedJob?.cancel()
        feedJob = null
        _events.tryEmit(StockSocketEvent.Disconnected)
    }

    private fun startFeed() {
        feedJob?.cancel()
        feedJob = applicationScope.launch(ioDispatcher) {
            while (isActive) {
                delay(FEED_INTERVAL_MS)
                sendRandomUpdates()
            }
        }
    }

    private fun sendRandomUpdates() {
        val timestamp = System.currentTimeMillis()
        currentPrices.forEach { (symbol, price) ->
            val swing = price * PRICE_SWING_PERCENT
            val nextPrice = (price + Random.nextDouble(-swing, swing))
                .coerceAtLeast(0.1)
            currentPrices[symbol] = nextPrice

            _events.tryEmit(
                StockSocketEvent.PriceUpdateReceived(
                    StockPriceEvent(
                        symbol = symbol,
                        priceUsd = nextPrice,
                        timestamp = timestamp,
                    )
                )
            )
        }
    }
}