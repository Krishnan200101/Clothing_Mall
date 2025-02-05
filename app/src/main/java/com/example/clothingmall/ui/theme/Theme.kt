package com.example.clothingmall.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF74D4B8),
    onPrimary = Color(0xFF00382A),
    primaryContainer = Color(0xFF00513D),
    onPrimaryContainer = Color(0xFF90F1D3),
    secondary = Color(0xFFB3CCBF),
    onSecondary = Color(0xFF1E352C),
    secondaryContainer = Color(0xFF344B42),
    onSecondaryContainer = Color(0xFFCEE9DB),
    tertiary = Color(0xFFA6CCE0),
    onTertiary = Color(0xFF0B3445),
    tertiaryContainer = Color(0xFF274A5C),
    onTertiaryContainer = Color(0xFFC2E8FD),
    background = Color(0xFF191C1A),
    onBackground = Color(0xFFE1E3DF),
    surface = Color(0xFF191C1A),
    onSurface = Color(0xFFE1E3DF)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006C51),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF90F1D3),
    onPrimaryContainer = Color(0xFF002117),
    secondary = Color(0xFF4C6359),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCEE9DB),
    onSecondaryContainer = Color(0xFF082017),
    tertiary = Color(0xFF3F6374),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFC2E8FD),
    onTertiaryContainer = Color(0xFF001F2A),
    background = Color(0xFFFBFDF9),
    onBackground = Color(0xFF191C1A),
    surface = Color(0xFFFBFDF9),
    onSurface = Color(0xFF191C1A)
)

private val LightColors = lightColorScheme(
    background = LightBackgroundColor,
    surface = LightNavigationBarColor,
    onBackground = LightTextColor,
    primary = LightButtonBackground,
    onPrimary = LightTextColor
)

private val DarkColors = darkColorScheme(
    background = DarkBackgroundColor,
    surface = DarkNavigationBarColor,
    onBackground = DarkTextColor,
    primary = DarkButtonBackground,
    onPrimary = DarkTextColor,
)

@Composable
fun ClothingMallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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