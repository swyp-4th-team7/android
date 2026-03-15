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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.habit.Habit
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.presentation.common.component.task.TaskEditPopup

@Composable
fun HabitList(
    onCheckClick: (Habit) -> Unit,
    onEditClick: (Habit) -> Unit,
    onDeleteClick: (Habit) -> Unit,
    habits: List<Habit>,
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
    onCheckClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    habit: Habit,
    modifier: Modifier = Modifier,
) {
    var showPopup by remember { mutableStateOf(false) }

    val checkIconRes = remember(habit.isCompleted) {
        when (habit.isCompleted) {
            true -> R.drawable.ic_check_filled
            false -> R.drawable.ic_check_unfilled
        }
    }

    val durationIconRes = remember(habit.duration) {
        when (habit.duration) {
            HabitDuration.THREE_DAYS -> R.drawable.ic_habit_day_3
            HabitDuration.SEVEN_DAYS -> R.drawable.ic_habit_day_7
            HabitDuration.FOURTEEN_DAYS -> R.drawable.ic_habbit_day_14
            HabitDuration.TWENTYONE_DAYS -> R.drawable.ic_habbit_day_21
            HabitDuration.SIXTYSIX_DAYS -> R.drawable.ic_habbit_day_66
            HabitDuration.NINETYNINE_DAYS -> R.drawable.ic_habbit_day_99
        }
    }

    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .noRippleClickable(
                    onClick = onCheckClick,
                    enabled = !habit.isCompleted,
                )
                .background(
                    color = HaebomTheme.colors.white,
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(horizontal = screenWidthDp(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(checkIconRes),
                contentDescription = null,
                tint = Color.Unspecified,
            )
        }

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
                    .padding(all = 8.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = habit.title,
                    color = HaebomTheme.colors.black,
                    style = HaebomTheme.typo.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "보상 :",
                        color = HaebomTheme.colors.gray400,
                        style = HaebomTheme.typo.helperText,
                    )

                    Text(
                        text = habit.reward,
                        color = HaebomTheme.colors.gray400,
                        style = HaebomTheme.typo.helperText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Box {
                Icon(
                    imageVector = ImageVector.vectorResource(durationIconRes),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                    tint = Color.Unspecified,
                )

                if (showPopup) {
                    TaskEditPopup(
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick,
                        onDismiss = { showPopup = false },
                        offset = DpOffset(4.dp, 0.dp),
                    )
                }
            }
        }
    }
}

private class HabitListPreviewProvider : PreviewParameterProvider<List<Habit>> {
    override val values = sequenceOf(
        HabitDuration.entries.mapIndexed { index, duration ->
            Habit(
                habitId = index.toLong(),
                duration = duration,
                isCompleted = index % 2 == 0,
                title = if (index % 2 == 0) "하루에 책 10장 읽기" else "하루에 책 10장 읽기 하루에 책 10장 읽기 하루에 책 10장 읽기",
                reward = if (index % 2 == 0) "가족이랑 놀이공원 가기" else "가족이랑 놀이공원 가기 가족이랑 놀이공원 가기 가족이랑 놀이공원 가기",
            )
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun HabitListPreview(
    @PreviewParameter(HabitListPreviewProvider::class) habits: List<Habit>,
) {
    HaebomTheme {
        HabitList(
            habits = habits,
            onCheckClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
