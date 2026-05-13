package com.swyp.firsttodo.core.common.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.type.TaskItemPopupType
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

@Composable
fun TaskItemPopup(
    onFirstClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    popupType: TaskItemPopupType = TaskItemPopupType.EDIT,
    offset: DpOffset = DpOffset(8.dp, 8.dp),
) {
    val density = LocalDensity.current

    val popupPositionProvider = remember(offset, density) {
        val xOffset = with(density) { offset.x.roundToPx() }
        val yOffset = with(density) { offset.y.roundToPx() }

        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                return IntOffset(
                    x = anchorBounds.right - popupContentSize.width - xOffset,
                    y = anchorBounds.top + yOffset,
                )
            }
        }
    }

    val popupProperties = remember {
        PopupProperties(focusable = true)
    }

    Popup(
        popupPositionProvider = popupPositionProvider,
        properties = popupProperties,
        onDismissRequest = onDismiss,
    ) {
        PopupContent(
            popupType = popupType,
            onEditClick = {
                onDismiss()
                onFirstClick()
            },
            onDeleteClick = {
                onDismiss()
                onDeleteClick()
            },
            modifier = modifier,
        )
    }
}

@Composable
private fun PopupContent(
    popupType: TaskItemPopupType,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    Column(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .dropShadow(
                shape = RoundedCornerShape(8.dp),
                shadow = Shadow(
                    radius = 6.dp,
                    color = colors.black.copy(alpha = 0.4f),
                    offset = DpOffset(0.dp, 1.dp),
                    spread = 0.dp,
                ),
            )
            .background(
                color = colors.white,
                shape = RoundedCornerShape(8.dp),
            ),
    ) {
        val dividerColor = colors.black.copy(alpha = 0.09f)

        ButtonItem(
            text = popupType.text,
            color = colors.gray600,
            iconRes = R.drawable.ic_edit_24,
            onClick = onEditClick,
            modifier = Modifier.drawBehind {
                val y = size.height
                val horizontalPadding = 2.dp.toPx()
                drawLine(
                    color = dividerColor,
                    start = Offset(horizontalPadding, y),
                    end = Offset(size.width - horizontalPadding, y),
                    strokeWidth = 0.8.dp.toPx(),
                )
            },
        )

        ButtonItem(
            text = "삭제하기",
            color = colors.semanticRed,
            iconRes = R.drawable.ic_trash_24,
            onClick = onDeleteClick,
        )
    }
}

@Composable
private fun ButtonItem(
    text: String,
    color: Color,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable(onClick)
            .padding(horizontal = 12.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = color,
            style = HaebomTheme.typo.helperText,
        )

        Spacer(modifier = Modifier.width(15.dp))

        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = color,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PopupContentPreview() {
    HaebomTheme {
        PopupContent(
            onEditClick = {},
            onDeleteClick = {},
            popupType = TaskItemPopupType.RETRY,
            modifier = Modifier.padding(all = 20.dp),
        )
    }
}
