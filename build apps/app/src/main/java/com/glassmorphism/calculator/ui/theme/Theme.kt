package com.glassmorphism.calculator.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GlassmorphismColorScheme = darkColorScheme(
    primary = GlassBlue,
    secondary = GlassPurple,
    tertiary = GlassCyan,
    background = DarkNavy,
    surface = DarkerNavy,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onTertiary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

private val AutomotiveColorScheme = darkColorScheme(
    primary = AureumAmber,
    secondary = AureumSilver,
    tertiary = AureumPlatinum,
    background = AureumBackground,
    surface = AureumSurface,
    onPrimary = AureumTextPrimary,
    onSecondary = AureumTextPrimary,
    onTertiary = AureumTextPrimary,
    onBackground = AureumTextPrimary,
    onSurface = AureumTextPrimary,
)

@Composable
fun AureumCalculatorTheme(
    themeType: ThemeType = ThemeType.GLASSMORPHISM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        themeType == ThemeType.AUTOMOTIVE -> AutomotiveColorScheme
        else -> GlassmorphismColorScheme
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
