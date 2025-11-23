package com.app.integrathlete.ui.theme

import android.app.Activity
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
    primary = BrandBlue, // Usa il tuo blu anche in dark mode (o BrandBlueDark se preferisci meno contrasto)
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surface = Color(0xFF1E1E1E), // Sfondo scuro per le card in dark mode
    onSurface = Color.White // Testo bianco su sfondo scuro
)

private val LightColorScheme = lightColorScheme(
    primary = BrandBlue, // Il tuo blu diventa il colore primario!
    secondary = PurpleGrey40,
    tertiary = Pink40,

    // Mappiamo i tuoi colori custom agli standard Material
    surface = NeutralGray98, // 0xFFFAFAFA
    onSurface = NeutralGray10, // 0xFF212121 (Testo principale)
    surfaceVariant = NeutralGray90, // 0xFFE0E0E0 (Sfondo Chip)
    onSurfaceVariant = NeutralGray60 // 0xFF666666 (Testo secondario)
)

@Composable
fun IntegrathleteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // DISATTIVIAMO i colori dinamici per vedere i TUOI colori
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