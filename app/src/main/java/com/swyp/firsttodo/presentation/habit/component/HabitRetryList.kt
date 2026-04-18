package com.swyp.firsttodo.presentation.habit.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.presentation.common.component.task.TaskItemPopup
import com.swyp.firsttodo.presentation.common.component.task.TaskItemPopupType
import kotlinx.coroutines.launch

private const val HELPER_SCROLL_AMOUNT_DP = 76

@Composable
fun HabitRetryList(
    habits: Async<List<HabitModel>>,
    onRetry: (HabitModel) -> Unit,
    onDelete: (HabitModel) -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    var showHelper by remember { mutableStateOf(false) }
    var firstItemRect by remember { mutableStateOf(Rect.Zero) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val view = LocalView.current
    val navBarHeightPx = WindowInsets.navigationBars.getBottom(density).toFloat()
    val bottomBarHeightPx = with(density) { 64.dp.toPx() } + navBarHeightPx

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = HaebomTheme.colors.gray50,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
    ) {
        Header(
            onHelperClick = {
                coroutineScope.launch {
                    val visibleBottom = view.height.toFloat() - bottomBarHeightPx
                    val isFirstItemVisible = firstItemRect != Rect.Zero && firstItemRect.top in 0f..visibleBottom
                    if (!isFirstItemVisible) {
                        scrollState.animateScrollTo(
                            scrollState.value + with(density) { HELPER_SCROLL_AMOUNT_DP.dp.roundToPx() },
                        )
                    }
                    showHelper = true
                }
            },
        )

        if (habits == Async.Empty) {
            EmptyItem(
                showTutorial = showHelper,
                onTutorialDismiss = { showHelper = false },
                onRectChanged = { firstItemRect = it },
            )
        } else {
            habits.getDataOrNull()?.let { datas ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    datas.forEachIndexed { index, habit ->
                        RetryItem(
                            title = habit.title,
                            reward = habit.reward,
                            duration = habit.duration,
                            onRetry = { onRetry(habit) },
                            onDelete = { onDelete(habit) },
                            showTutorial = index == 0 && showHelper,
                            onTutorialDismiss = { showHelper = false },
                            onRectChanged = if (index == 0) ({ firstItemRect = it }) else null,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    onHelperClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "재도전할 습관",
            color = HaebomTheme.colors.black,
            style = HaebomTheme.typo.section,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_help_24),
            contentDescription = null,
            modifier = Modifier
                .noRippleClickable(onHelperClick)
                .padding(vertical = 12.dp)
                .padding(start = 4.dp, end = 20.dp),
            tint = Color.Unspecified,
        )
    }
}

@Composable
private fun EmptyItem(
    showTutorial: Boolean,
    onTutorialDismiss: () -> Unit,
    onRectChanged: ((Rect) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var itemRect by remember { mutableStateOf(Rect.Zero) }

    Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            itemRect = coordinates.boundsInRoot()
            onRectChanged?.invoke(itemRect)
        },
    ) {
        Text(
            text = "실패한 습관이 없어요.",
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = HaebomTheme.colors.white,
                    shape = RoundedCornerShape(4.dp),
                )
                .heightIn(min = 56.dp)
                .padding(all = 16.dp),
            color = HaebomTheme.colors.gray400,
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.subtitle,
        )

        if (showTutorial) {
            RetryTutorialOverlay(
                targetRect = itemRect,
                onDismiss = onTutorialDismiss,
            )
        }
    }
}

@Composable
private fun RetryItem(
    title: String,
    reward: String?,
    duration: HabitDuration,
    onRetry: () -> Unit,
    onDelete: () -> Unit,
    showTutorial: Boolean,
    onTutorialDismiss: () -> Unit,
    onRectChanged: ((Rect) -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var showPopup by remember { mutableStateOf(false) }
    var itemRect by remember { mutableStateOf(Rect.Zero) }

    val durationIconRes = remember(duration) {
        when (duration) {
            HabitDuration.THREE_DAYS -> R.drawable.ic_habit_day_3_completed
            HabitDuration.SEVEN_DAYS -> R.drawable.ic_habit_day_7_completed
            HabitDuration.FOURTEEN_DAYS -> R.drawable.ic_habit_day_14_completed
            HabitDuration.TWENTYONE_DAYS -> R.drawable.ic_habit_day_21_completed
            HabitDuration.SIXTYSIX_DAYS -> R.drawable.ic_habit_day_66_completed
            HabitDuration.NINETYNINE_DAYS -> R.drawable.ic_habit_day_99_completed
        }
    }

    Box(
        modifier = modifier.onGloballyPositioned { coordinates ->
            itemRect = coordinates.boundsInRoot()
            onRectChanged?.invoke(itemRect)
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .background(
                    color = HaebomTheme.colors.white,
                    shape = RoundedCornerShape(4.dp),
                ),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = title,
                    color = HaebomTheme.colors.gray200,
                    style = HaebomTheme.typo.description,
                )

                reward?.let {
                    Text(
                        text = "보상 : $it",
                        color = HaebomTheme.colors.gray200,
                        style = HaebomTheme.typo.helperText,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .noRippleClickable({ showPopup = true })
                    .width(102.dp)
                    .fillMaxHeight()
                    .padding(end = 8.dp),
                contentAlignment = Alignment.TopEnd,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(durationIconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                )
            }
        }

        if (showPopup) {
            TaskItemPopup(
                onFirstClick = onRetry,
                onDeleteClick = onDelete,
                onDismiss = { showPopup = false },
                popupType = TaskItemPopupType.RETRY,
            )
        }

        if (showTutorial) {
            RetryTutorialOverlay(
                targetRect = itemRect,
                onDismiss = onTutorialDismiss,
            )
        }
    }
}

@Composable
private fun RetryTutorialOverlay(
    targetRect: Rect,
    onDismiss: () -> Unit,
) {
    val density = LocalDensity.current

    val statusBarHeightPx = WindowInsets.statusBars
        .getTop(density)
        .toFloat()

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
            imageVector = ImageVector.vectorResource(R.drawable.ic_retry_tooltip),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)

                val xPosition = constraints.maxWidth - placeable.width - 16.dp.roundToPx()

                val yPosition = correctedRect.top.toInt() - placeable.height - 8.dp.roundToPx()

                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(xPosition, yPosition)
                }
            },
        )
    }
}

private class HabitRetryListPreviewProvider : PreviewParameterProvider<Async<List<HabitModel>>> {
    override val values = sequenceOf(
        Async.Success(
            listOf(
                HabitModel(
                    habitId = 1L,
                    title = "매일 물 2L 마시기 매일 물 2L 마시기 매일 물 2L 마시기 매일 물 2L 마시기 매일 물 2L 마시기",
                    duration = HabitDuration.SEVEN_DAYS,
                    reward = "건강한 몸",
                    isCompleted = false,
                ),
                HabitModel(
                    habitId = 2L,
                    title = "독서 30분",
                    duration = HabitDuration.TWENTYONE_DAYS,
                    reward = "지식의 힘",
                    isCompleted = false,
                ),
                HabitModel(
                    habitId = 3L,
                    title = "매일 스트레칭하기",
                    duration = HabitDuration.SIXTYSIX_DAYS,
                    reward = "유연한 몸",
                    isCompleted = false,
                ),
            ),
        ),
        Async.Success(
            listOf(
                HabitModel(
                    habitId = 4L,
                    title = "매일 물 2L 마시기",
                    duration = HabitDuration.THREE_DAYS,
                    reward = null,
                    isCompleted = false,
                ),
                HabitModel(
                    habitId = 5L,
                    title = "독서 30분",
                    duration = HabitDuration.FOURTEEN_DAYS,
                    reward = null,
                    isCompleted = false,
                ),
            ),
        ),
        Async.Empty,
    )
}

@Preview
@Composable
private fun HabitRetryListPreview(
    @PreviewParameter(HabitRetryListPreviewProvider::class) habits: Async<List<HabitModel>>,
) {
    val scrollState = androidx.compose.foundation.rememberScrollState()
    HaebomTheme {
        HabitRetryList(
            habits = habits,
            onRetry = {},
            onDelete = {},
            scrollState = scrollState,
            modifier = Modifier.padding(top = 200.dp),
        )
    }
}
