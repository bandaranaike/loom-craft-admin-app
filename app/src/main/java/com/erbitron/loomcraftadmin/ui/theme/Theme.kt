package com.erbitron.loomcraftadmin.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = BrandAccentDark,
    onPrimary = OnAccentDark,
    background = SurfaceBaseDark,
    surface = SurfaceRaisedDark,
    surfaceVariant = SurfaceTintDark,
    onBackground = BrandStrongDark,
    onSurface = BrandStrongDark,
    onSurfaceVariant = BrandMutedTextDark,
    outline = BorderDefaultDark,
    outlineVariant = BorderSoftDark,
    secondary = BrandStrongDark,
    onSecondary = OnAccentDark,
    error = BrandDanger
)

private val LightColorScheme = lightColorScheme(
    primary = BrandAccent,
    onPrimary = Color.White,
    background = SurfaceBase,
    surface = SurfaceRaised,
    surfaceVariant = SurfaceTint,
    onBackground = BrandStrong,
    onSurface = BrandStrong,
    onSurfaceVariant = BrandMutedText,
    outline = BorderDefault,
    outlineVariant = BorderSoft,
    secondary = BrandStrong,
    onSecondary = Color.White,
    error = BrandDanger
)

@Composable
fun LoomCraftAdminTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Make status bar transparent or match background
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
