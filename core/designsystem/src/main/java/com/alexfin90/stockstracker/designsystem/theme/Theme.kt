package com.alexfin90.stockstracker.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7C9BFF),
    onPrimary = Color(0xFF0E1A3A),
    primaryContainer = Color(0xFF1D2B57),
    onPrimaryContainer = Color(0xFFDCE3FF),

    secondary = Color(0xFF4FD1C5),
    onSecondary = Color(0xFF062B28),
    secondaryContainer = Color(0xFF124B46),
    onSecondaryContainer = Color(0xFFB8F3ED),

    tertiary = Color(0xFF7DD3FC),
    onTertiary = Color(0xFF0A2A3A),
    tertiaryContainer = Color(0xFF153F56),
    onTertiaryContainer = Color(0xFFD2F1FF),

    error = Color(0xFFFF7A7A),
    onError = Color(0xFF4A1010),
    errorContainer = Color(0xFF6A1E1E),
    onErrorContainer = Color(0xFFFFDADA),

    background = Color(0xFF0F172A),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF243041),
    onSurfaceVariant = Color(0xFFB6C2CF),

    inverseSurface = Color(0xFFE5E7EB),
    inverseOnSurface = Color(0xFF111827),
    outline = Color(0xFF607086),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2F5BFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE4FF),
    onPrimaryContainer = Color(0xFF0E1A3A),

    secondary = Color(0xFF0F9D8A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFC9F2EC),
    onSecondaryContainer = Color(0xFF062B28),

    tertiary = Color(0xFF0284C7),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD0F0FF),
    onTertiaryContainer = Color(0xFF0A2A3A),

    error = Color(0xFFC62828),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111827),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),

    inverseSurface = Color(0xFF1E293B),
    inverseOnSurface = Color(0xFFF8FAFC),
    outline = Color(0xFF94A3B8),
)

@Composable
fun StocksTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}