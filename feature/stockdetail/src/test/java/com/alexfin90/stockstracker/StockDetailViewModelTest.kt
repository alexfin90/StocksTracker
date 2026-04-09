package com.alexfin90.stockstracker

import androidx.lifecycle.SavedStateHandle
import com.alexfin90.stockstracker.entities.Stock
import com.alexfin90.stockstracker.usecases.ObserveConnectionErrorUseCase
import com.alexfin90.stockstracker.usecases.ObserveStockUseCase
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class StockDetailViewModelTest {

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

    private fun createViewModel(symbol: String = "AAPL"): StockDetailViewModel {
        val savedStateHandle = SavedStateHandle().apply {
            set("symbol", symbol)
        }
        val observeStock = ObserveStockUseCase(fakeRepository)
        val observeConnectionError = ObserveConnectionErrorUseCase(fakeRepository)
        return StockDetailViewModel(
            savedStateHandle = savedStateHandle,
            observeStockUseCase = observeStock,
            observeConnectionErrorUseCase = observeConnectionError,
        )
    }

    @Test
    fun `initial state has null stock and null error`() = runTest {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value

        assertNull(state.stock)
        assertNull(state.error)
    }

    @Test
    fun `emitting matching stock produces UiStockDetail`() = runTest {
        val viewModel = createViewModel(symbol = "AAPL")
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(listOf(appleStock))
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.stock)
        assertEquals("AAPL", state.stock!!.symbol)
        assertEquals("Apple Inc.", state.stock.name)
        assertEquals("Consumer electronics", state.stock.description)
        assertEquals(150.0, state.stock.priceUsd, 0.001)
        assertTrue(state.stock.isIncrease)
        assertNull(state.error)
    }

    @Test
    fun `emitting non-matching stock results in null stock`() = runTest {
        val viewModel = createViewModel(symbol = "MSFT")
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(listOf(appleStock))
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.stock)
    }

    @Test
    fun `error shown only when stock is null`() = runTest {
        val viewModel = createViewModel(symbol = "MSFT")
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(listOf(appleStock))
        fakeRepository.emitError("Connection lost")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.stock)
        assertEquals("Connection lost\nTrying to reconnect...", state.error)
    }

    @Test
    fun `error suppressed when stock is available`() = runTest {
        val viewModel = createViewModel(symbol = "AAPL")
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(listOf(appleStock))
        fakeRepository.emitError("Connection lost")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.stock)
        assertNull(state.error)
    }

    @Test
    fun `stock with decreasing price maps isIncrease to false`() = runTest {
        val decreasingStock = appleStock.copy(priceUsd = 130.0, previousPriceUsd = 150.0)
        val viewModel = createViewModel(symbol = "AAPL")
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(listOf(decreasingStock))
        advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.stock!!.isIncrease)
    }

    @Test
    fun `stock with null previousPrice maps isIncrease to false`() = runTest {
        val noPrevStock = appleStock.copy(previousPriceUsd = null)
        val viewModel = createViewModel(symbol = "AAPL")
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(listOf(noPrevStock))
        advanceUntilIdle()

        assertEquals(false, viewModel.uiState.value.stock!!.isIncrease)
    }

    @Test
    fun `state updates when stock price changes`() = runTest {
        val viewModel = createViewModel(symbol = "AAPL")
        backgroundScope.launch { viewModel.uiState.collect {} }

        fakeRepository.emitStocks(listOf(appleStock))
        advanceUntilIdle()
        assertEquals(150.0, viewModel.uiState.value.stock!!.priceUsd, 0.001)

        fakeRepository.emitStocks(listOf(appleStock.copy(priceUsd = 175.0)))
        advanceUntilIdle()
        assertEquals(175.0, viewModel.uiState.value.stock!!.priceUsd, 0.001)
    }

    companion object {
        val appleStock = Stock(
            symbol = "AAPL",
            name = "Apple Inc.",
            description = "Consumer electronics",
            priceUsd = 150.0,
            previousPriceUsd = 140.0,
            updatedAtMillis = 1000L,
        )
    }
}
