package com.alexfin90.stockstracker.designsystem.atomic.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ConnectionStatusIndicator(
    isConnected: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(if (isConnected) Color.Green else Color.Red)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = if (isConnected) "Connected" else "Disconnected",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}


@Preview
@Composable
private fun ConnectionStatusIndicatorPreview() {
    ConnectionStatusIndicator(isConnected = true)
}