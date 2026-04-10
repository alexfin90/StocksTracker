package com.alexfin90.stockstracker.designsystem.atomic.atoms.icons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val DownPriceIcon: ImageVector
    get() {
        if (_down != null) return _down!!

        _down = ImageVector.Builder(
            name = "down",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            group {
                path(
                    fill = SolidColor(Color(0xFF0D0D0D))
                ) {
                    moveTo(4f, 18.9999f)
                    verticalLineTo(10.4999f)
                    curveTo(4f, 10.2237f, 4.22386f, 9.99985f, 4.5f, 9.99985f)
                    horizontalLineTo(5.5f)
                    curveTo(5.77614f, 9.99985f, 6f, 10.2237f, 6f, 10.4999f)
                    verticalLineTo(16.5799f)
                    lineTo(18.44f, 4.14985f)
                    curveTo(18.5339f, 4.0552f, 18.6617f, 4.00195f, 18.795f, 4.00195f)
                    curveTo(18.9283f, 4.00195f, 19.0561f, 4.0552f, 19.15f, 4.14985f)
                    lineTo(19.85f, 4.84985f)
                    curveTo(19.9447f, 4.94374f, 19.9979f, 5.07153f, 19.9979f, 5.20485f)
                    curveTo(19.9979f, 5.33817f, 19.9447f, 5.46597f, 19.85f, 5.55985f)
                    lineTo(7.41f, 17.9999f)
                    horizontalLineTo(13.5f)
                    curveTo(13.7761f, 17.9999f, 14f, 18.2237f, 14f, 18.4999f)
                    verticalLineTo(19.4999f)
                    curveTo(14f, 19.776f, 13.7761f, 19.9999f, 13.5f, 19.9999f)
                    horizontalLineTo(5f)
                    curveTo(4.80115f, 19.9997f, 4.61052f, 19.9205f, 4.47f, 19.7799f)
                    lineTo(4.2f, 19.5099f)
                    curveTo(4.07141f, 19.3712f, 3.99998f, 19.189f, 4f, 18.9999f)
                    close()
                }
            }
        }.build()

        return _down!!
    }

private var _down: ImageVector? = null

@Preview
@Composable
private fun IconPreview() {
    MaterialTheme {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = DownPriceIcon,
            contentDescription = null,
            tint = Color.Red
        )
    }
}