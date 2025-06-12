package com.muhammadfaishalrizqipratama0094.cookit_.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Orange_Primary,
    onPrimary = Color.Black,
    primaryContainer = Orange_Light,
    onPrimaryContainer = Color.Black,
    secondary = Orange_Secondary,
    onSecondary = Color.Black,
    secondaryContainer = Orange_Secondary_Light,
    onSecondaryContainer = Color.Black
)

private val DarkColorScheme = darkColorScheme(
    primary = Orange_Dark,
    onPrimary = Color.Black,
    primaryContainer = Orange_Primary,
    onPrimaryContainer = Color.Black,
    secondary = Orange_Secondary_Dark,
    onSecondary = Color.Black,
    secondaryContainer = Orange_Secondary,
    onSecondaryContainer = Color.Black
)

@Composable
fun CookIT_Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}