package com.alexfin90.stockstracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun StockDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: StockDetailViewModel = hiltViewModel(),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.TopCenter,
    ) {
        Text(
            text = viewModel.symbol,
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}