package com.alexfin90.stockstracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun StocksFeedScreen(
    modifier: Modifier = Modifier,
    viewModel: StocksFeedViewModel = hiltViewModel(),
    onStockClick: (String) -> Unit,
) {
    StocksFeedContent(
        modifier = modifier,
        state = StocksFeedScreenState(),
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
    Row(
        modifier = modifier.clickable { onStockClick("AAPL") },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Hello, Stocks Tracker!",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
