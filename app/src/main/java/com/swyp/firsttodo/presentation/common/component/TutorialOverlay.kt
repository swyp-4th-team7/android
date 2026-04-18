package com.swyp.firsttodo.presentation.common.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

internal const val TUTORIAL_SCROLL_AMOUNT_DP = 76
internal const val TUTORIAL_MIN_SPACE_DP = 40

enum class TutorialType(
    @param:DrawableRes val tooltipIconRes: Int,
    val tooltipEndMarginDp: Int,
) {
    RETRY(
        tooltipIconRes = R.drawable.ic_retry_tooltip,
        tooltipEndMarginDp = 16,
    ),
    SCHEDULE(
        tooltipIconRes = R.drawable.ic_schedule_tooltip,
        tooltipEndMarginDp = 28,
    ),
}

@Composable
fun TutorialOverlay(
    targetRect: Rect,
    onDismiss: () -> Unit,
    type: TutorialType,
) {
    val density = LocalDensity.current
    val statusBarHeightPx = WindowInsets.statusBars.getTop(density).toFloat()

    Popup(
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true),
    ) {
        val dimColor = HaebomTheme.colors.black.copy(alpha = 0.5f)

        val correctedRect = Rect(
            left = targetRect.left,
            top = targetRect.top - statusBarHeightPx,
            right = targetRect.right,
            bottom = targetRect.bottom - statusBarHeightPx,
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { onDismiss() }
                },
        ) {
            val cornerRadiusPx = 4.dp.toPx()

            val path = Path().apply {
                addRect(Rect(0f, 0f, size.width, size.height))
                addRoundRect(
                    RoundRect(
                        rect = correctedRect,
                        radiusX = cornerRadiusPx,
                        radiusY = cornerRadiusPx,
                    ),
                )
                fillType = PathFillType.EvenOdd
            }

            drawPath(path = path, color = dimColor)
        }

        Icon(
            imageVector = ImageVector.vectorResource(type.tooltipIconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)

                val xPosition = constraints.maxWidth - placeable.width - type.tooltipEndMarginDp.dp.roundToPx()
                val yPosition = correctedRect.top.toInt() - placeable.height - 8.dp.roundToPx()

                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(xPosition, yPosition)
                }
            },
        )
    }
}
