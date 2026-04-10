package com.alexfin90.stockstracker.designsystem.atomic.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.alexfin90.stockstracker.designsystem.atomic.atoms.icons.DownPriceIcon
import com.alexfin90.stockstracker.designsystem.atomic.atoms.icons.UpPriceIcon

@Composable
fun StockRow(
    stock: UiStockRow,
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
        stock = UiStockRow(
            symbol = "AAPL",
            name = "Apple Inc.",
            priceUsd = 150.0,
            isUp = isUp,
        ),
        onClick = {},
    )
}

