package com.example.loomcraftadmin.ui.theme

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BrandAccentDark,
    onPrimary = Color.White,
    primaryContainer = BrandStrongDark,
    onPrimaryContainer = SurfaceBaseDark,
    secondary = BrandStrongDark,
    onSecondary = Color.White,
    secondaryContainer = SurfaceTintDark,
    tertiary = BrandAccentDark,
    surface = SurfaceBaseDark,
    onSurface = Color.White,
    surfaceVariant = SurfaceRaisedDark,
    onSurfaceVariant = BrandBodyTextDark,
    outline = BorderDefaultDark,
    outlineVariant = BorderSoftDark,
    error = BrandDangerDark,
    background = SurfaceBaseDark,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = BrandAccent,
    onPrimary = Color.White,
    primaryContainer = SurfaceHeaderPeach,
    onPrimaryContainer = BrandStrong,
    secondary = BrandStrong,
    onSecondary = Color.White,
    secondaryContainer = SurfaceTint,
    tertiary = BrandAccent,
    surface = SurfaceBase,
    onSurface = BrandStrong,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = BrandBodyText,
    outline = BorderDefault,
    outlineVariant = BorderSoft,
    error = BrandDanger,
    background = SurfaceBase,
    onBackground = BrandStrong

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun LoomCraftAdminTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to false to maintain LoomCraft brand consistency
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
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
