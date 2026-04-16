package com.swyp.firsttodo.presentation.habit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.presentation.common.component.task.TaskItemPopup
import com.swyp.firsttodo.presentation.common.component.task.TaskItemPopupType

@Composable
fun HabitRetryList(
    habits: Async<List<HabitModel>>,
    onRetry: (HabitModel) -> Unit,
    onDelete: (HabitModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showHelper by remember { mutableStateOf(false) }

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
            onHelperClick = { showHelper = true },
        )

        if (habits == Async.Empty) {
            EmptyItem()
        } else {
            habits.getDataOrNull()?.let { datas ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    datas.forEach { habit ->
                        RetryItem(
                            title = habit.title,
                            reward = habit.reward,
                            duration = habit.duration,
                            onRetry = { onRetry(habit) },
                            onDelete = { onDelete(habit) },
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
private fun EmptyItem(modifier: Modifier = Modifier) {
    Text(
        text = "실패한 습관이 없어요.",
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = HaebomTheme.colors.white,
                shape = RoundedCornerShape(4.dp),
            )
            .heightIn(56.dp)
            .padding(all = 16.dp),
        color = HaebomTheme.colors.gray400,
        textAlign = TextAlign.Center,
        style = HaebomTheme.typo.subtitle,
    )
}

@Composable
private fun RetryItem(
    title: String,
    reward: String?,
    duration: HabitDuration,
    onRetry: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPopup by remember { mutableStateOf(false) }

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

    Box(modifier = modifier) {
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
    HaebomTheme {
        HabitRetryList(
            habits = habits,
            onRetry = {},
            onDelete = {},
        )
    }
}
