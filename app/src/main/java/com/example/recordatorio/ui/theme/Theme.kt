// ui/theme/Theme.kt
package com.example.recordatorio.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color.Red,
    secondary = Color.Blue,
    background = Color.White,
    surface = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color.Red,
    secondary = Color.Blue,
    background = Color.Black,
    surface = Color.Black,
)

@Composable
fun RecordatorioTheme(
    isDarkTheme: Boolean = false, // Usar este nombre para el tema
    onThemeChange: (Boolean) -> Unit, // Añadir el parámetro para manejar el cambio de tema
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de definir la tipografía si es necesario
        content = content
    )
}
