package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme

data class DayInfo(
    val day: Int,
    val isToday: Boolean,
)

private val days = listOf("월", "화", "수", "목", "금", "토", "일")

@Composable
fun WeeklyCalendar(
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    month: Int,
    week: Int,
    dayInfos: Async<List<DayInfo>>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        val colors = HaebomTheme.colors
        val typo = HaebomTheme.typo

        Row(
            modifier = Modifier.heightIn(64.dp),
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_cheveron_left),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onPrevClick)
                    .padding(all = 12.dp),
                tint = Color.Unspecified,
            )

            Text(
                text = "${month}월 ${week}주차",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 26.dp, bottom = 10.dp),
                color = colors.black,
                style = typo.week,
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_cheveron_right),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onNextClick)
                    .padding(all = 12.dp),
                tint = Color.Unspecified,
            )
        }

        Row(
            modifier = Modifier,
        ) { }
    }
}
