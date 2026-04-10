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

val up: ImageVector
    get() {
        if (_up != null) return _up!!

        _up = ImageVector.Builder(
            name = "up",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            group {
                path(
                    fill = SolidColor(Color(0xFF0D0D0D))
                ) {
                    moveTo(20f, 5f)
                    verticalLineTo(13.5f)
                    curveTo(20f, 13.7761f, 19.7761f, 14f, 19.5f, 14f)
                    horizontalLineTo(18.5f)
                    curveTo(18.2238f, 14f, 18f, 13.7761f, 18f, 13.5f)
                    verticalLineTo(7.42f)
                    lineTo(5.55997f, 19.85f)
                    curveTo(5.46609f, 19.9447f, 5.33829f, 19.9979f, 5.20497f, 19.9979f)
                    curveTo(5.07166f, 19.9979f, 4.94386f, 19.9447f, 4.84997f, 19.85f)
                    lineTo(4.14997f, 19.15f)
                    curveTo(4.05532f, 19.0561f, 4.00208f, 18.9283f, 4.00208f, 18.795f)
                    curveTo(4.00208f, 18.6617f, 4.05532f, 18.5339f, 4.14997f, 18.44f)
                    lineTo(16.59f, 6f)
                    horizontalLineTo(10.5f)
                    curveTo(10.2238f, 6f, 9.99997f, 5.77614f, 9.99997f, 5.5f)
                    verticalLineTo(4.5f)
                    curveTo(9.99997f, 4.22386f, 10.2238f, 4f, 10.5f, 4f)
                    horizontalLineTo(19f)
                    curveTo(19.1988f, 4.00018f, 19.3895f, 4.07931f, 19.53f, 4.22f)
                    lineTo(19.8f, 4.49f)
                    curveTo(19.9286f, 4.6287f, 20f, 4.81086f, 20f, 5f)
                    close()
                }
            }
        }.build()

        return _up!!
    }

private var _up: ImageVector? = null

@Preview
@Composable
private fun IconPreview() {
    MaterialTheme {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = up,
            contentDescription = null,
            tint = Color.Green
        )
    }
}