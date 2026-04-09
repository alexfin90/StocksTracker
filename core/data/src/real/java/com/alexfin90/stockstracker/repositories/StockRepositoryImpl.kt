package com.alexfin90.stockstracker.repositories

import com.alexfin90.stockstracker.contracts.StockRepository
import com.alexfin90.stockstracker.dispatcher.ApplicationScope
import com.alexfin90.stockstracker.dispatcher.IoDispatcher
import com.alexfin90.stockstracker.entities.Stock
import com.alexfin90.stockstracker.mappers.toDomain
import com.alexfin90.stockstracker.model.STOCK_CATALOG
import com.alexfin90.stockstracker.model.StockPriceEvent
import com.alexfin90.stockstracker.model.StockSocketEvent
import com.alexfin90.stockstracker.remote.StockWebSocketDataSource
import com.alexfin90.stockstracker.remote.dto.StockPriceDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

class StockRepositoryImpl @Inject constructor(
    private val dataSource: StockWebSocketDataSource,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : StockRepository {

    companion object {
        private const val FEED_INTERVAL_MS = 2000L
        private const val PRICE_SWING_PERCENT = 0.05
    }

    private val _connectionActive = MutableStateFlow(false)
    private val _stockMap = MutableStateFlow(initialStocks())
    private val _connectionError = MutableStateFlow<String?>(null)


    private var feedJob: Job? = null

    init {
        observeSocketEvents()
    }

    override val connectionActive: Flow<Boolean> = _connectionActive.asStateFlow()

    override val connectionError: Flow<String?> = _connectionError.asStateFlow()

    override val stocks: Flow<List<Stock>> =
        _stockMap
            .map { stockMap -> stockMap.values.toList() }

    override fun start() {
        Timber.d("start")
        dataSource.connect()
        feedJob?.cancel()
        feedJob = applicationScope.launch(ioDispatcher) {
            while (isActive) {
                sendRandomUpdates()
                delay(FEED_INTERVAL_MS)
            }
        }
    }

    override fun stop() {
        Timber.d("stop")
        feedJob?.cancel()
        feedJob = null
        dataSource.disconnect()
        _connectionActive.value = false
    }

    override fun observeStock(symbol: String): Flow<Stock?> =
        _stockMap.map { stockMap ->
            stockMap[symbol]
        }

    private fun observeSocketEvents() {
        dataSource.events
            .onEach { event ->
                when (event) {
                    StockSocketEvent.Connected -> {
                        _connectionActive.value = true
                        _connectionError.value = null
                    }

                    StockSocketEvent.Disconnected -> {
                        _connectionActive.value = false
                    }

                    is StockSocketEvent.Failure -> {
                        _connectionActive.value = false
                        _connectionError.value = event.throwable.message
                    }

                    is StockSocketEvent.PriceUpdateReceived -> {
                        applyUpdate(priceEvent = event.value)
                    }
                }
            }
            .launchIn(applicationScope)
    }

    private fun applyUpdate(priceEvent: StockPriceEvent) {
        _stockMap.update { currentStockMap ->
            val currentStock = currentStockMap[priceEvent.symbol] ?: return@update currentStockMap
            currentStockMap + (
                    priceEvent.symbol to currentStock.copy(
                        priceUsd = priceEvent.priceUsd,
                        previousPriceUsd = currentStock.priceUsd,
                        updatedAtMillis = priceEvent.timestamp,
                    )
                    )
        }
    }

    private fun sendRandomUpdates() {
        val timestamp = System.currentTimeMillis()
        _stockMap.value.values.forEach { stock ->
            val priceSwing = stock.priceUsd * PRICE_SWING_PERCENT
            val nextPrice = (
                    stock.priceUsd + Random.nextDouble(
                        from = -priceSwing,
                        until = priceSwing,
                    )
                    ).coerceAtLeast(minimumValue = 0.1)

            dataSource.send(
                update = StockPriceDto(
                    symbol = stock.symbol,
                    priceUsd = nextPrice,
                    timestamp = timestamp,
                )
            )
        }
    }

    private fun initialStocks(): Map<String, Stock> =
        STOCK_CATALOG.associate { stockData ->
            stockData.symbol to stockData.toDomain()
        }
}