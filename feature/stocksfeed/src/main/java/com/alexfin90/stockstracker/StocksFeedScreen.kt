package com.alexfin90.stockstracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexfin90.StocksFeedViewModelContract
import com.alexfin90.stockstracker.uimodels.UiStock

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


//TODO move to design system and improve it
@Composable
private fun StockRow(
    stock: UiStock,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = stock.symbol, style = MaterialTheme.typography.titleMedium)
            Text(text = stock.name, style = MaterialTheme.typography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$%.2f".format(stock.priceUsd),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.width(4.dp))
            stock.isIncrease.let { isUp ->
                Text(
                    text = if (isUp) "\u2191" else "\u2193",
                    color = if (isUp) Color.Green else Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
