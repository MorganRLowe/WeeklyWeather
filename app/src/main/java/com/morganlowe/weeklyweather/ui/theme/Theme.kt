package com.morganlowe.weeklyweather.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColors = lightColorScheme(
    primary = Teal,
    onPrimary = White,
    primaryContainer = Teal,
    onPrimaryContainer = White,
    secondary = White,
    onSecondary = Teal,
    background = Teal,         // The whole app background — matches original
    onBackground = White,      // Text on the teal background
    surface = Teal,            // Cards blend in with the background — matches original
    onSurface = White,
    surfaceVariant = Teal,
    onSurfaceVariant = White,
    error = ErrorRed,
    onError = Teal
)

@Composable
fun WeeklyWeatherTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColors,
        typography = Typography,
        content = content
    )
}
