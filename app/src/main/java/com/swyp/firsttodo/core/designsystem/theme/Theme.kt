package com.swyp.firsttodo.core.designsystem.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private fun heabomLightColorScheme(colors: HaebomColors) =
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

object HaebomTheme {
    val colors: HaebomColors
        @Composable
        @ReadOnlyComposable
        get() = LocalHaebomColorsProvider.current

    val typo: HaebomTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalHaebomTypographyProvider.current
}

@Composable
private fun ProvideHaebomColorsAndTypography(
    colors: HaebomColors,
    typo: HaebomTypography,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalHaebomColorsProvider provides colors,
        LocalHaebomTypographyProvider provides typo,
        content = content,
    )
}

@Composable
fun HaebomTheme(content: @Composable () -> Unit) {
    val colors = defaultHaebomColors
    val typo = defaultHaebomTypography

    ProvideHaebomColorsAndTypography(
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
        ) {
            Box {
                content()

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                        .background(HaebomTheme.colors.yellow400),
                )
            }
        }
    }
}
