package com.alexfin90.stockstracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexfin90.StocksFeedViewModelContract
import com.alexfin90.stockstracker.designsystem.atomic.molecules.StockRow

@Composable
fun StocksFeedScreen(
    modifier: Modifier = Modifier,
    viewModel: StocksFeedViewModelContract = hiltViewModel<StocksFeedViewModel>(),
    onStockClick: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    StocksFeedContent(
        modifier = modifier,
        state = state,
        onStockClick = onStockClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StocksFeedContent(
    modifier: Modifier = Modifier,
    state: StocksFeedScreenState,
    onStockClick: (String) -> Unit = {},
) {
    when {
        state.isLoading && state.stocks.isEmpty() -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center,
            ) {
                Text(text = state.error, textAlign = TextAlign.Center)
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier
            ) {
                items(state.stocks, key = { it.symbol }) { stock ->
                    StockRow(
                        stock = stock,
                        onClick = { onStockClick(stock.symbol) },
                    )
                }
            }
        }
    }
}
