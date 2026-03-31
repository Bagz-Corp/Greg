package com.gcorp.multirecherche3d.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SoftBlueGreen,
    secondary = SubtleSilver,
    tertiary = Pink
)

val LightColorScheme = lightColorScheme(
    primary = BoltBLue,
    primaryContainer = HawkesBlue,
    secondary = BahamaBlue,
    secondaryContainer = AndreaBlue,
    tertiary = TribalViolet,
    tertiaryContainer = CuteLavender,
    error = LuckyRed,
    errorContainer = PaleSalmonPink,
    surface = HawkesBlue,
    surfaceVariant = SubtleSilver,
    onSurface = Charcoal,
    outline = BusinessGrey,
)

@Composable
fun GregTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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