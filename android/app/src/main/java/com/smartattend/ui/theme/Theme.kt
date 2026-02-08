package com.smartattend.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = SmartAttendPrimary,
    onPrimary = SmartAttendPrimaryForeground,
    secondary = SmartAttendSecondary,
    onSecondary = SmartAttendForeground,
    background = SmartAttendBackground,
    onBackground = SmartAttendForeground,
    surface = SmartAttendCard,
    onSurface = SmartAttendForeground,
    error = SmartAttendDestructive,
    onError = SmartAttendPrimaryForeground,
)

@Composable
fun SmartAttendTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = SmartAttendTypography,
        content = content,
    )
}
