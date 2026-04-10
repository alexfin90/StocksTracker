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
import androidx.compose.material3.Icon
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
import com.alexfin90.stockstracker.designsystem.atomic.atoms.icons.DownPriceIcon
import com.alexfin90.stockstracker.designsystem.atomic.atoms.icons.UpPriceIcon
import com.alexfin90.stockstracker.designsystem.atomic.molecules.animatedFlashBackground
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
            text = stock.name + " (${stock.symbol})",
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .animatedFlashBackground(
                        triggerKey = stock.priceUsd,
                        flashColor = if (stock.isUp) {
                            Color.Green.copy(alpha = 0.5f)
                        } else {
                            Color.Red.copy(alpha = 0.5f)
                        }
                    )
                    .padding(8.dp),
                text = "$%.2f".format(stock.priceUsd),
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.width(4.dp))
            stock.isUp.let { isUp ->
                Icon(
                    imageVector = if (isUp) UpPriceIcon else DownPriceIcon,
                    contentDescription = if (isUp) "price up" else "price down",
                    tint = if (isUp) Color.Green else Color.Red
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stock.description,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}
