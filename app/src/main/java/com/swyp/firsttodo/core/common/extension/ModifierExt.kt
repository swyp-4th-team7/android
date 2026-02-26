package com.swyp.firsttodo.core.common.extension

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val figmaScreenWidth = 360.dp
private val figmaScreenHeight = 800.dp

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit,
    enabled: Boolean = true,
): Modifier =
    composed {
        clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            enabled = enabled,
        ) {
            onClick()
        }
    }

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Modifier.heightForScreenPercentage(height: Dp): Modifier {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val ratio = screenHeight / figmaScreenHeight
    return this.height(height * ratio)
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Modifier.widthForScreenPercentage(width: Dp): Modifier {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val ratio = screenWidth / figmaScreenWidth
    return this.width(width * ratio)
}
