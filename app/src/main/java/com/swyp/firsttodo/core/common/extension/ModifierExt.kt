package com.swyp.firsttodo.core.common.extension

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp

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

fun Modifier.figmaDropShadow(
    shape: Shape,
    dpOffset: DpOffset,
    blur: Dp,
    spread: Dp,
    color: Color,
): Modifier {
    return this.dropShadow(
        shape = shape,
        shadow = Shadow(
            radius = blur,
            color = color,
            offset = dpOffset,
            spread = spread,
        ),
    )
}

fun Modifier.dashedCircleBorder(
    color: Color,
    strokeWidth: Dp = 2.dp,
    dashWidth: Dp = 4.dp,
    gapWidth: Dp = 4.dp,
): Modifier =
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                asFrameworkPaint().apply {
                    isAntiAlias = true
                    style = android.graphics.Paint.Style.STROKE
                    this.strokeWidth = strokeWidth.toPx()
                    this.color = android.graphics.Color.TRANSPARENT
                    setShadowLayer(0f, 0f, 0f, android.graphics.Color.TRANSPARENT)
                    this.color = color.toArgb()
                    pathEffect = android.graphics.DashPathEffect(
                        floatArrayOf(dashWidth.toPx(), gapWidth.toPx()),
                        0f,
                    )
                }
            }
            val radius = size.minDimension / 2f
            canvas.drawCircle(
                center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height / 2f),
                radius = radius - strokeWidth.toPx() / 2f,
                paint = paint,
            )
        }
    }

@Composable
fun Modifier.heightForScreenPercentage(height: Dp): Modifier = this.height(screenHeightDp(height))

@Composable
fun Modifier.widthForScreenPercentage(width: Dp): Modifier = this.width(screenWidthDp(width))
