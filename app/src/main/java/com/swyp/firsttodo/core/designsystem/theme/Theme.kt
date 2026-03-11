package com.swyp.firsttodo.core.designsystem.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private fun heabomLightColorScheme(colors: HeabomColors) =
    lightColorScheme(
        primary = colors.yellow500,
        onPrimary = colors.white,
        primaryContainer = colors.yellow100,
        onPrimaryContainer = colors.yellow600,
        secondary = colors.gray500,
        onSecondary = colors.white,
        secondaryContainer = colors.gray100,
        onSecondaryContainer = colors.gray700,
        tertiary = colors.green500,
        onTertiary = colors.white,
        tertiaryContainer = colors.green100,
        onTertiaryContainer = colors.green800,
        error = colors.semanticRed,
        onError = colors.white,
        background = colors.white,
        onBackground = colors.gray800,
        surface = colors.white,
        onSurface = colors.gray800,
        surfaceVariant = colors.gray50,
        onSurfaceVariant = colors.gray600,
        outline = colors.gray300,
        outlineVariant = colors.gray100,
    )

object HeabomTheme {
    val colors: HeabomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalHeabomColorsProvider.current

    val typo: HeabomTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalHeabomTypographyProvider.current
}

@Composable
private fun ProvideHeabomColorsAndTypography(
    colors: HeabomColors,
    typo: HeabomTypography,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalHeabomColorsProvider provides colors,
        LocalHeabomTypographyProvider provides typo,
        content = content,
    )
}

@Composable
fun HeabomTheme(content: @Composable () -> Unit) {
    val colors = defaultHeabomColors
    val typo = defaultHeabomTypography

    ProvideHeabomColorsAndTypography(
        colors = colors,
        typo = typo,
    ) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                (view.context as Activity).window.run {
                    WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
                }
            }
        }
        MaterialTheme(
            colorScheme = heabomLightColorScheme(colors),
            content = content,
        )
    }
}
