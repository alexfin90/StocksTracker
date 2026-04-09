package com.alexfin90.stockstracker


import com.alexfin90.stockstracker.entities.Stock
import com.alexfin90.stockstracker.uimodels.UiStock
import com.alexfin90.stockstracker.usecases.ObserveConnectionErrorUseCase
import com.alexfin90.stockstracker.usecases.ObserveSortedStocksUseCase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StocksFeedViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val fakeRepository = FakeStockRepository()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): StocksFeedViewModel {
        val observeSortedStocks = ObserveSortedStocksUseCase(fakeRepository)
        val observeConnectionError = ObserveConnectionErrorUseCase(fakeRepository)
        return StocksFeedViewModel(
            observeSortedStocksUseCase = observeSortedStocks,
            observeConnectionErrorUseCase = observeConnectionError,
            defaultDispatcher = dispatcher,
        )
    }

    @Test
    fun `initial state is loading with empty stocks`() = runTest {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertTrue(state.isLoading)
        assertEquals(persistentListOf<UiStock>(), state.stocks)
        assertNull(state.error)
    }

    @Test
    fun `emitting stocks produces sorted ui models`() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(testStocks)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertNull(state.error)
        assertEquals(3, state.stocks.size)
        assertEquals("GOOG", state.stocks[0].symbol)
        assertEquals("AAPL", state.stocks[1].symbol)
        assertEquals("TSLA", state.stocks[2].symbol)
    }

    @Test
    fun `stocks are mapped to UiStock correctly`() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(testStocks)
        advanceUntilIdle()

        val uiStock = viewModel.uiState.value.stocks.first { it.symbol == "AAPL" }
        assertEquals("Apple Inc.", uiStock.name)
        assertEquals(150.0, uiStock.priceUsd, 0.001)
        assertTrue(uiStock.isIncrease)
    }

    @Test
    fun `stock with no previous price maps isIncrease to false`() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.uiState.collect {} }

        val stockNoPrevious = Stock(
            symbol = "META",
            name = "Meta",
            description = "Meta Platforms",
            priceUsd = 300.0,
            previousPriceUsd = null,
            updatedAtMillis = 1000L,
        )
        fakeRepository.emitStocks(listOf(stockNoPrevious))
        advanceUntilIdle()

        val uiStock = viewModel.uiState.value.stocks.first()
        assertEquals(false, uiStock.isIncrease)
    }

    @Test
    fun `connection error is formatted with reconnect message`() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(testStocks)
        fakeRepository.emitError("WebSocket closed")
        advanceUntilIdle()

        assertEquals("WebSocket closed\nTrying to reconnect...", viewModel.uiState.value.error)
    }

    @Test
    fun `null error produces null in state`() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(testStocks)
        fakeRepository.emitError(null)
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `state updates when stocks change`() = runTest {
        val viewModel = createViewModel()
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(testStocks)
        advanceUntilIdle()
        assertEquals(3, viewModel.uiState.value.stocks.size)

        fakeRepository.emitStocks(listOf(testStocks[0].copy(priceUsd = 999.0)))
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.stocks.size)
        assertEquals(999.0, viewModel.uiState.value.stocks[0].priceUsd, 0.001)
    }

    companion object {
        val testStocks = listOf(
            Stock(
                symbol = "AAPL",
                name = "Apple Inc.",
                description = "Consumer electronics",
                priceUsd = 150.0,
                previousPriceUsd = 140.0,
                updatedAtMillis = 1000L,
            ),
            Stock(
                symbol = "GOOG",
                name = "Alphabet Inc.",
                description = "Search engine",
                priceUsd = 2800.0,
                previousPriceUsd = 2850.0,
                updatedAtMillis = 1000L,
            ),
            Stock(
                symbol = "TSLA",
                name = "Tesla Inc.",
                description = "Electric vehicles",
                priceUsd = 120.0,
                previousPriceUsd = 110.0,
                updatedAtMillis = 1000L,
            ),
        )
    }
}
