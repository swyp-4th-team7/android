package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.BoldStyle
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.MediumStyle

data class DayInfo(
    val weekDay: String,
    val day: Int,
    val isToday: Boolean,
    val hasSticker: Boolean,
)

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
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.heightIn(64.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_left),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onPrevClick)
                    .padding(horizontal = 12.dp, vertical = 20.dp),
                tint = Color.Unspecified,
            )

            Text(
                text = "${month}월 ${week}주차",
                color = HaebomTheme.colors.black,
                style = HaebomTheme.typo.week,
            )

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                modifier = Modifier
                    .noRippleClickable(onNextClick)
                    .padding(horizontal = 12.dp, vertical = 20.dp),
                tint = Color.Unspecified,
            )
        }

        when (dayInfos) {
            is Async.Success -> Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                dayInfos.data.forEach { day ->
                    MarkedDate(
                        weekDay = day.weekDay,
                        day = day.day,
                        isToday = day.isToday,
                        hasSticker = day.hasSticker,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            else -> Spacer(Modifier.height(72.dp))
        }
    }
}

@Composable
private fun MarkedDate(
    weekDay: String,
    day: Int,
    isToday: Boolean,
    hasSticker: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = weekDay,
            color = HaebomTheme.colors.black,
            style = MediumStyle.copy(fontSize = 12.sp),
        )

        when {
            isToday -> Text(
                text = day.toString(),
                modifier = Modifier
                    .sizeIn(40.dp, 40.dp)
                    .background(
                        color = HaebomTheme.colors.orange400,
                        shape = CircleShape,
                    )
                    .wrapContentSize(Alignment.Center),
                color = HaebomTheme.colors.white,
                style = BoldStyle.copy(fontSize = 20.sp),
            )

            hasSticker -> Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = HaebomTheme.colors.yellow300,
                        shape = CircleShape,
                    ),
            )

            else -> Text(
                text = day.toString(),
                modifier = Modifier
                    .sizeIn(40.dp, 40.dp)
                    .dashedCircleBorder(
                        color = HaebomTheme.colors.gray200,
                        strokeWidth = 2.dp,
                    )
                    .wrapContentSize(Alignment.Center),
                color = HaebomTheme.colors.gray200,
                style = BoldStyle.copy(fontSize = 20.sp),
            )
        }
    }
}

private fun Modifier.dashedCircleBorder(
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

@Preview(showBackground = true)
@Composable
private fun WeeklyCalendarPreview() {
    val previewDayInfos = listOf(
        DayInfo(weekDay = "월", day = 10, isToday = false, hasSticker = false),
        DayInfo(weekDay = "화", day = 11, isToday = false, hasSticker = true),
        DayInfo(weekDay = "수", day = 12, isToday = false, hasSticker = false),
        DayInfo(weekDay = "목", day = 13, isToday = true, hasSticker = false),
        DayInfo(weekDay = "금", day = 14, isToday = false, hasSticker = true),
        DayInfo(weekDay = "토", day = 15, isToday = false, hasSticker = false),
        DayInfo(weekDay = "일", day = 16, isToday = false, hasSticker = false),
    )

    HaebomTheme {
        WeeklyCalendar(
            onPrevClick = {},
            onNextClick = {},
            month = 3,
            week = 2,
            dayInfos = Async.Success(previewDayInfos),
        )
    }
}
