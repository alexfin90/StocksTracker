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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexfin90.StocksFeedViewModelContract
import com.alexfin90.stockstracker.designsystem.atomic.atoms.icons.DownPriceIcon
import com.alexfin90.stockstracker.designsystem.atomic.atoms.icons.UpPriceIcon
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
            Icon(
                imageVector = if (stock.isUp) UpPriceIcon else DownPriceIcon,
                contentDescription = null,
                tint = if (stock.isUp) Color.Green else Color.Red
            )
        }
    }
}

private class StockRowPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}

@Preview
@Composable
private fun StockRowPreview(
    @PreviewParameter(StockRowPreviewProvider::class) isUp: Boolean,
) {
    StockRow(
        stock = UiStock(
            symbol = "AAPL",
            name = "Apple Inc.",
            priceUsd = 150.0,
            isUp = isUp,
        ),
        onClick = {},
    )
}

