package com.swyp.firsttodo.presentation.reward.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
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
import com.swyp.firsttodo.core.common.extension.figmaDropShadow
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.presentation.reward.list.ChildRewardFilterType

interface RewardFilterType {
    val displayName: String
    val request: String
}

@Composable
fun <T : RewardFilterType> RewardFilter(
    allFilters: List<T>,
    selectedFilterType: T,
    onFilterClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    val iconRes = when (expanded) {
        true -> R.drawable.ic_chevron_up_24
        false -> R.drawable.ic_chevron_down_24
    }

    Box(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .noRippleClickable(onClick = { expanded = true })
                .background(
                    color = HaebomTheme.colors.gray50,
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = selectedFilterType.displayName,
                color = HaebomTheme.colors.gray400,
                style = HaebomTheme.typo.subtitle,
            )

            Icon(
                imageVector = ImageVector.vectorResource(iconRes),
                contentDescription = null,
                tint = HaebomTheme.colors.gray400,
            )
        }

        if (expanded) {
            FilterPopup(
                allFilters = allFilters,
                selectedFilterType = selectedFilterType,
                onFilterClick = {
                    onFilterClick(it)
                    expanded = false
                },
                onDismiss = { expanded = false },
            )
        }
    }
}

@Composable
private fun <T : RewardFilterType> FilterPopup(
    allFilters: List<T>,
    selectedFilterType: T,
    onFilterClick: (T) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current

    val popupPositionProvider = remember(density) {
        val xOffset = with(density) { 4.dp.roundToPx() }
        val yOffset = with(density) { 8.dp.roundToPx() }

        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize,
            ): IntOffset {
                return IntOffset(
                    x = anchorBounds.right - popupContentSize.width - xOffset,
                    y = anchorBounds.bottom + yOffset,
                )
            }
        }
    }

    val popupProperties = remember {
        PopupProperties(
            focusable = true,
        )
    }

    Popup(
        popupPositionProvider = popupPositionProvider,
        properties = popupProperties,
        onDismissRequest = onDismiss,
    ) {
        PopupContent(
            allFilters = allFilters,
            selectedFilterType = selectedFilterType,
            onFilterClick = onFilterClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun <T : RewardFilterType> PopupContent(
    allFilters: List<T>,
    selectedFilterType: T,
    onFilterClick: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .widthIn(min = 108.dp)
            .width(IntrinsicSize.Max)
            .background(
                color = HaebomTheme.colors.white,
                shape = RoundedCornerShape(8.dp),
            )
            .figmaDropShadow(
                shape = RoundedCornerShape(8.dp),
                dpOffset = DpOffset(0.dp, 0.dp),
                blur = 4.dp,
                spread = 0.dp,
                color = HaebomTheme.colors.black.copy(alpha = 0.2f),
            ),
    ) {
        val dividerColor = HaebomTheme.colors.gray50

        allFilters.forEachIndexed { index, type ->
            val isFirst = index == 0
            val isLast = index == allFilters.lastIndex

            val shape = when {
                isFirst -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                isLast -> RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                else -> RoundedCornerShape(0.dp)
            }

            val (backgroundColor, textColor) = when (selectedFilterType == type) {
                true -> HaebomTheme.colors.gray50 to HaebomTheme.colors.black
                false -> HaebomTheme.colors.white to HaebomTheme.colors.gray600
            }

            Text(
                text = type.displayName,
                modifier = Modifier
                    .clip(shape)
                    .clickable { onFilterClick(type) }
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .drawBehind {
                        if (!isLast) {
                            val strokeWidth = 0.8.dp.toPx()
                            val horizontalInset = 4.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = dividerColor,
                                start = Offset(horizontalInset, y),
                                end = Offset(size.width - horizontalInset, y),
                                strokeWidth = strokeWidth,
                            )
                        }
                    }
                    .padding(all = 16.dp)
                    .wrapContentHeight(Alignment.CenterVertically),
                color = textColor,
                textAlign = TextAlign.Center,
                style = HaebomTheme.typo.bottomNavbar,
            )
        }
    }
}

@Preview
@Composable
private fun RewardFilterPreview() {
    HaebomTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            RewardFilter(
                allFilters = ChildRewardFilterType.entries,
                selectedFilterType = ChildRewardFilterType.IN_PROGRESS,
                onFilterClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun PopupContentPreview() {
    HaebomTheme {
        PopupContent(
            allFilters = ChildRewardFilterType.entries,
            selectedFilterType = ChildRewardFilterType.COMPLETE,
            onFilterClick = {},
        )
    }
}
