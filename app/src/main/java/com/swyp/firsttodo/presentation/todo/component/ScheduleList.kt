package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.ScheduleCategory
import com.swyp.firsttodo.presentation.common.component.HaebomLabel
import com.swyp.firsttodo.presentation.common.component.task.TaskEditPopup

data class ScheduleUiModel(
    val scheduleId: Long,
    val dDay: Long,
    val title: String,
    val date: String,
    val rawDate: String,
    val category: ScheduleCategory,
    val isUrgent: Boolean,
)

@Composable
fun ScheduleList(
    schedules: Async<List<ScheduleUiModel>>,
    onPlusClick: () -> Unit,
    onEditClick: (ScheduleUiModel) -> Unit,
    onDeleteClick: (ScheduleUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    TodoCard(
        title = "다가오는 일정",
        onPlusClick = onPlusClick,
        // TODO: Focus 구현
        onHelpClick = {},
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            when (schedules) {
                is Async.Success -> schedules.data.forEach { schedule ->
                    key(schedule.scheduleId) {
                        ScheduleItem(
                            schedule = schedule,
                            onEditClick = { onEditClick(schedule) },
                            onDeleteClick = { onDeleteClick(schedule) },
                        )
                    }
                }

                is Async.Empty -> TodoCardEmptyContent(
                    text = "+를 눌러 다가올 일을 적어 주세요!",
                )

                else -> Spacer(Modifier.height(56.dp))
            }
        }
    }
}

@Composable
private fun ScheduleItem(
    schedule: ScheduleUiModel,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPopup by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = modifier
                .noRippleClickable({ showPopup = true })
                .fillMaxWidth()
                .heightIn(56.dp)
                .height(IntrinsicSize.Max)
                .background(
                    color = HaebomTheme.colors.white,
                    shape = RoundedCornerShape(4.dp),
                )
                .padding(all = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .widthIn(52.dp)
                    .padding(end = 8.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = "D-",
                    color = HaebomTheme.colors.gray500,
                    style = HaebomTheme.typo.dDay,
                )

                Text(
                    text = schedule.dDay.toString(),
                    color = if (schedule.isUrgent) HaebomTheme.colors.semanticRed else HaebomTheme.colors.semanticGreen,
                    style = HaebomTheme.typo.dDay,
                )
            }

            VerticalDivider(
                modifier = Modifier.padding(end = 10.dp),
                thickness = 1.dp,
                color = HaebomTheme.colors.gray200,
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = schedule.title,
                    color = HaebomTheme.colors.black,
                    style = HaebomTheme.typo.description,
                )

                Text(
                    text = schedule.date,
                    color = HaebomTheme.colors.gray500,
                    style = HaebomTheme.typo.helperText,
                )
            }

            HaebomLabel(
                textColor = HaebomTheme.colors.labelRedText,
                backgroundColor = HaebomTheme.colors.labelRedBackground,
                text = schedule.category.displayName,
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

private class ScheduleListPreviewProvider : PreviewParameterProvider<Async<List<ScheduleUiModel>>> {
    override val values = sequenceOf(
        Async.Success(
            listOf(
                ScheduleUiModel(
                    scheduleId = 1L,
                    dDay = 5,
                    title = "수학 수행평가",
                    date = "2026.03.22.일요일",
                    category = ScheduleCategory.SCHOOL_EXAM,
                    rawDate = "",
                    isUrgent = true,
                ),
                ScheduleUiModel(
                    scheduleId = 2L,
                    dDay = 20,
                    title = "1학기 기말고사",
                    date = "2026.04.06.월요일",
                    category = ScheduleCategory.SCHOOL_EXAM,
                    rawDate = "",
                    isUrgent = false,
                ),
                ScheduleUiModel(
                    scheduleId = 3L,
                    dDay = 28,
                    title = "과학 탐구 과학 탐구 대회 과학 탐구 대회 대회 과학 탐구 대회",
                    date = "2026.04.14.화요일",
                    category = ScheduleCategory.SCHOOL_EXAM,
                    rawDate = "",
                    isUrgent = false,
                ),
            ),
        ),
        Async.Empty,
    )
}

@Preview(showBackground = true)
@Composable
private fun ScheduleListPreview(
    @PreviewParameter(ScheduleListPreviewProvider::class) schedules: Async<List<ScheduleUiModel>>,
) {
    HaebomTheme {
        ScheduleList(
            schedules = schedules,
            onPlusClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
