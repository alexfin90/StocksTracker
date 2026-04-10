package com.alexfin90.stockstracker.designsystem.atomic.atoms.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexfin90.stockstracker.designsystem.theme.StocksTrackerTheme

@Composable
fun StocksTrackerToggleButton(
    isConnected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.large,
    ) {
        Text(text = if (isConnected) "Stop" else "Start")
    }
}

@Preview
@Composable
internal fun StocksTrackerToggleButtonPreview() {
    StocksTrackerTheme {
        var isConnected by remember { mutableStateOf(false) }
        StocksTrackerToggleButton(
            isConnected = isConnected,
            onClick = { isConnected = !isConnected },
            modifier = Modifier.padding(16.dp)
        )
    }
}