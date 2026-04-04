package com.swyp.firsttodo.presentation.habit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.presentation.common.component.task.TaskEditPopup

enum class HabitListType {
    CHILD,
    PARENT,
}

@Composable
fun HabitList(
    habitListType: HabitListType,
    onCheckClick: (HabitModel) -> Unit,
    onEditClick: (HabitModel) -> Unit,
    onDeleteClick: (HabitModel) -> Unit,
    habits: List<HabitModel>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .background(
                color = HaebomTheme.colors.gray50,
                shape = RoundedCornerShape(8.dp),
            ),
        contentPadding = PaddingValues(top = 20.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
    ) {
        item {
            Text(
                text = "내 습관 모음집",
                modifier = Modifier.padding(bottom = 12.dp),
                color = HaebomTheme.colors.black,
                style = HaebomTheme.typo.section,
            )
        }

        items(items = habits, key = { it.habitId }) {
            ListItem(
                habitListType = habitListType,
                onCheckClick = { onCheckClick(it) },
                onEditClick = { onEditClick(it) },
                onDeleteClick = { onDeleteClick(it) },
                habit = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
            )
        }
    }
}

@Composable
private fun ListItem(
    habitListType: HabitListType,
    onCheckClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    habit: HabitModel,
    modifier: Modifier = Modifier,
) {
    var showPopup by remember { mutableStateOf(false) }

    val colors = HaebomTheme.colors

    val (checkIconRes, titleColor, rewardColor) = remember(habit.isCompleted) {
        when (habit.isCompleted) {
            true -> Triple(R.drawable.ic_check_filled, colors.gray200, colors.gray200)
            false -> Triple(R.drawable.ic_check_unfilled, colors.black, colors.gray400)
        }
    }

    val durationIconRes = remember(habit.duration, habit.isCompleted) {
        when (habit.duration) {
            HabitDuration.THREE_DAYS -> R.drawable.ic_habit_day_3
            HabitDuration.SEVEN_DAYS -> R.drawable.ic_habit_day_7
            HabitDuration.FOURTEEN_DAYS -> R.drawable.ic_habit_day_14
            HabitDuration.TWENTYONE_DAYS -> R.drawable.ic_habit_day_21
            HabitDuration.SIXTYSIX_DAYS -> R.drawable.ic_habit_day_66
            HabitDuration.NINETYNINE_DAYS -> R.drawable.ic_habit_day_99
        }
    }

    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .noRippleClickable(onCheckClick)
                .background(
                    color = HaebomTheme.colors.white,
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(checkIconRes),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable({ showPopup = true })
                    .background(
                        color = HaebomTheme.colors.white,
                        shape = RoundedCornerShape(4.dp),
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = if (habitListType == HabitListType.PARENT) 18.dp else 8.dp)
                        .padding(start = if (habitListType == HabitListType.PARENT) 12.dp else 16.dp, end = 8.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = habit.title,
                        color = titleColor,
                        style = HaebomTheme.typo.description,
                    )

                    when (habitListType) {
                        HabitListType.CHILD -> Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(
                                text = "보상 :",
                                color = rewardColor,
                                style = HaebomTheme.typo.helperText,
                            )

                            Text(
                                text = habit.reward ?: "",
                                color = rewardColor,
                                style = HaebomTheme.typo.helperText,
                            )
                        }

                        HabitListType.PARENT -> Unit
                    }
                }

                Icon(
                    imageVector = ImageVector.vectorResource(durationIconRes),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color.Unspecified,
                )
            }

            if (showPopup) {
                TaskEditPopup(
                    onEditClick = {
                        onEditClick()
                        showPopup = false
                    },
                    onDeleteClick = {
                        onDeleteClick()
                        showPopup = false
                    },
                    onDismiss = { showPopup = false },
                )
            }
        }
    }
}

private val previewHabits = HabitDuration.entries.mapIndexed { index, duration ->
    HabitModel(
        habitId = index.toLong(),
        duration = duration,
        isCompleted = index % 2 == 0,
        title = if (index % 2 == 0) "하루에 책 10장 읽기" else "하루에 책 10장 읽기 하루에 책 10장 읽기 하루에 책 10장 읽기",
        reward = if (index % 2 == 0) "가족이랑 놀이공원 가기" else "가족이랑 놀이공원 가기 가족이랑 놀이공원 가기 가족이랑 놀이공원 가기",
    )
}

private class HabitListPreviewProvider : PreviewParameterProvider<HabitListType> {
    override val values = sequenceOf(*HabitListType.entries.toTypedArray())
}

@Preview(showBackground = true)
@Composable
private fun HabitListPreview(
    @PreviewParameter(HabitListPreviewProvider::class) habitListType: HabitListType,
) {
    HaebomTheme {
        HabitList(
            habitListType = habitListType,
            habits = previewHabits,
            onCheckClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
