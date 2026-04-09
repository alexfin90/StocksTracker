package com.alexfin90.stockstracker.repositories

import com.alexfin90.stockstracker.contracts.StockRepository
import com.alexfin90.stockstracker.dispatcher.ApplicationScope
import com.alexfin90.stockstracker.entities.Stock
import com.alexfin90.stockstracker.mappers.toDomain
import com.alexfin90.stockstracker.model.STOCK_CATALOG
import com.alexfin90.stockstracker.model.StockPriceEvent
import com.alexfin90.stockstracker.model.StockSocketEvent
import com.alexfin90.stockstracker.remote.StockMockDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

class StockRepositoryMockImpl @Inject constructor(
    private val dataSource: StockMockDataSource,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
) : StockRepository {

    private val _connectionActive = MutableStateFlow(false)
    private val _stockMap = MutableStateFlow(initialStocks())


    init {
        observeSocketEvents()
    }

    override val connectionActive: Flow<Boolean> = _connectionActive.asStateFlow()


    override val stocks: Flow<List<Stock>> =
        _stockMap.map { stockMap -> stockMap.values.toList() }

    override fun start() {
        Timber.d("start")
        val initialPrices = _stockMap.value.mapValues { it.value.priceUsd }
        dataSource.connect(initialPrices)
    }

    override fun stop() {
        Timber.d("stop")
        dataSource.disconnect()
        _connectionActive.value = false
    }

    override fun observeStock(symbol: String): Flow<Stock?> =
        _stockMap.map { stockMap -> stockMap[symbol] }

    private fun observeSocketEvents() {
        dataSource.events
            .onEach { event ->
                when (event) {
                    StockSocketEvent.Connected -> {
                        _connectionActive.value = true
                    }

                    StockSocketEvent.Disconnected -> {
                        _connectionActive.value = false
                    }

                    is StockSocketEvent.Failure -> {
                        _connectionActive.value = false
                    }

                    is StockSocketEvent.PriceUpdateReceived -> {
                        applyUpdate(priceEvent = event.value)
                    }
                }
            }
            .launchIn(applicationScope)
    }

    //apply update to the stockMap
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

    private fun initialStocks(): Map<String, Stock> =
        STOCK_CATALOG.associate { stockData ->
            stockData.symbol to stockData.toDomain()
        }


}