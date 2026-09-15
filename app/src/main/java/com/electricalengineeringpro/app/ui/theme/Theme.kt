package com.electricalengineeringpro.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = EngineeringBlue,
    secondary = EngineeringAccent,
    background = EngineeringSurface,
    surface = EngineeringSurface,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = EngineeringText,
    onSurface = EngineeringText
)

private val DarkColors = darkColorScheme(
    primary = EngineeringBlue,
    secondary = EngineeringAccent
)

@Composable
fun ElectricalEngineeringTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
