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
import com.alexfin90.stockstracker.designsystem.atomic.colors.Indigo10
import com.alexfin90.stockstracker.designsystem.atomic.colors.Indigo20
import com.alexfin90.stockstracker.designsystem.atomic.colors.Indigo40
import com.alexfin90.stockstracker.designsystem.atomic.colors.Indigo80
import com.alexfin90.stockstracker.designsystem.atomic.colors.Indigo90
import com.alexfin90.stockstracker.designsystem.atomic.colors.Red10
import com.alexfin90.stockstracker.designsystem.atomic.colors.Red20
import com.alexfin90.stockstracker.designsystem.atomic.colors.Red30
import com.alexfin90.stockstracker.designsystem.atomic.colors.Red40
import com.alexfin90.stockstracker.designsystem.atomic.colors.Red80
import com.alexfin90.stockstracker.designsystem.atomic.colors.Red90
import com.alexfin90.stockstracker.designsystem.atomic.colors.Sky10
import com.alexfin90.stockstracker.designsystem.atomic.colors.Sky20
import com.alexfin90.stockstracker.designsystem.atomic.colors.Sky40
import com.alexfin90.stockstracker.designsystem.atomic.colors.Sky80
import com.alexfin90.stockstracker.designsystem.atomic.colors.Sky90
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate10
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate20
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate30
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate40
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate50
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate60
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate80
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate90
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate95
import com.alexfin90.stockstracker.designsystem.atomic.colors.Slate99
import com.alexfin90.stockstracker.designsystem.atomic.colors.Teal10
import com.alexfin90.stockstracker.designsystem.atomic.colors.Teal20
import com.alexfin90.stockstracker.designsystem.atomic.colors.Teal40
import com.alexfin90.stockstracker.designsystem.atomic.colors.Teal80
import com.alexfin90.stockstracker.designsystem.atomic.colors.Teal90


private val DarkColorScheme = darkColorScheme(
    primary = Indigo80,
    onPrimary = Indigo10,
    primaryContainer = Indigo20,
    onPrimaryContainer = Indigo90,

    secondary = Teal80,
    onSecondary = Teal10,
    secondaryContainer = Teal20,
    onSecondaryContainer = Teal90,

    tertiary = Sky80,
    onTertiary = Sky10,
    tertiaryContainer = Sky20,
    onTertiaryContainer = Sky90,

    error = Red80,
    onError = Red20,
    errorContainer = Red30,
    onErrorContainer = Red90,

    background = Slate10,
    onBackground = Slate90,
    surface = Slate10,
    onSurface = Slate90,
    surfaceVariant = Slate30,
    onSurfaceVariant = Slate80,
    inverseSurface = Slate90,
    inverseOnSurface = Slate10,
    outline = Slate60,
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo40,
    onPrimary = Color.White,
    primaryContainer = Indigo90,
    onPrimaryContainer = Indigo10,

    secondary = Teal40,
    onSecondary = Color.White,
    secondaryContainer = Teal90,
    onSecondaryContainer = Teal10,

    tertiary = Sky40,
    onTertiary = Color.White,
    tertiaryContainer = Sky90,
    onTertiaryContainer = Sky10,

    error = Red40,
    onError = Color.White,
    errorContainer = Red90,
    onErrorContainer = Red10,

    background = Slate99,
    onBackground = Slate10,
    surface = Color.White,
    onSurface = Slate10,
    surfaceVariant = Slate90,
    onSurfaceVariant = Slate40,
    inverseSurface = Slate20,
    inverseOnSurface = Slate95,
    outline = Slate50,
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