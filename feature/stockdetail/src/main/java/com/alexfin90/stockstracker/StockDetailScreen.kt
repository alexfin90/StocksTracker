package com.alexfin90.stockstracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexfin90.stockstracker.uimodels.UiStockDetail

@Composable
fun StockDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: StocksDetailViewModelContract = hiltViewModel<StockDetailViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center,
    ) {
        when {
            uiState.error != null -> {
                Text(text = uiState.error!!)
            }

            else -> {
                uiState.stock?.let {
                    StockDetailContent(stock = it)
                }
            }
        }
    }

}

@Composable
private fun StockDetailContent(stock: UiStockDetail) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stock.symbol,
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$%.2f".format(stock.priceUsd),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.width(4.dp))
            stock.isIncrease.let { isUp ->
                Text(
                    text = if (isUp) "\u2191" else "\u2193",
                    color = if (isUp) Color.Green else Color.Red,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stock.description,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
