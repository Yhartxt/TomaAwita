package com.yharim.tomaawita.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Teal500,
    onPrimary = Color.White,
    primaryContainer = Teal100,
    onPrimaryContainer = Teal700,
    background = AppBackground,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Teal50,
    onSurfaceVariant = Muted,
    outline = AppOutline,
    // Sin estos, los diálogos, menús y el reloj salen con el tono lavanda por defecto de Material.
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Teal50
)

// El diseño solo tiene versión clara, por eso no hay tema oscuro todavía.
@Composable
fun TomaAwitaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content
    )
}