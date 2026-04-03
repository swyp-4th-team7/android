package com.swyp.firsttodo.presentation.todo.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.dashedCircleBorder
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.BoldStyle
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.MediumStyle
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickerModel
import com.swyp.firsttodo.domain.model.sticker.WeeklyStickersModel
import java.time.DayOfWeek
import java.time.LocalDate

data class DayInfo(
    val weekDay: String,
    val day: Int,
    val isToday: Boolean,
    val stickerCode: String?,
)

private val DAY_OF_WEEK_LABELS = mapOf(
    DayOfWeek.MONDAY to "월",
    DayOfWeek.TUESDAY to "화",
    DayOfWeek.WEDNESDAY to "수",
    DayOfWeek.THURSDAY to "목",
    DayOfWeek.FRIDAY to "금",
    DayOfWeek.SATURDAY to "토",
    DayOfWeek.SUNDAY to "일",
)

@Composable
fun WeeklyCalendar(
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    weeklyStickers: Async<WeeklyStickersModel>,
    modifier: Modifier = Modifier,
) {
    val weekLabel = weeklyStickers.getDataOrNull()?.weekLabel ?: "       "

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
                text = weekLabel,
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

        when (val data = weeklyStickers.getDataOrNull()) {
            null -> Spacer(Modifier.height(72.dp))

            else -> {
                val today = LocalDate.now()
                val dayInfos = data.stickers.map { sticker ->
                    val date = LocalDate.parse(sticker.date)
                    DayInfo(
                        weekDay = DAY_OF_WEEK_LABELS[date.dayOfWeek] ?: "",
                        day = date.dayOfMonth,
                        isToday = date == today,
                        stickerCode = sticker.stickerCode,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    dayInfos.forEach { day ->
                        MarkedDate(
                            weekDay = day.weekDay,
                            day = day.day,
                            isToday = day.isToday,
                            stickerCode = day.stickerCode,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MarkedDate(
    weekDay: String,
    day: Int,
    isToday: Boolean,
    stickerCode: String?,
    modifier: Modifier = Modifier,
) {
    val weekDayFontSize = with(LocalDensity.current) { 12.dp.toSp() }
    val dayFontSize = with(LocalDensity.current) { 20.dp.toSp() }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = weekDay,
            color = HaebomTheme.colors.black,
            style = MediumStyle.copy(fontSize = weekDayFontSize),
        )

        when {
            stickerCode != null -> Image(
                painter = painterResource(R.drawable.img_sticker_clover),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .wrapContentSize(Alignment.Center),
            )

            isToday -> Text(
                text = day.toString(),
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = HaebomTheme.colors.orange400,
                        shape = CircleShape,
                    )
                    .wrapContentSize(Alignment.Center),
                color = HaebomTheme.colors.white,
                style = BoldStyle.copy(fontSize = dayFontSize),
            )

            else -> Text(
                text = day.toString(),
                modifier = Modifier
                    .size(40.dp)
                    .dashedCircleBorder(
                        color = HaebomTheme.colors.gray200,
                        strokeWidth = 2.dp,
                    )
                    .wrapContentSize(Alignment.Center),
                color = HaebomTheme.colors.gray200,
                style = BoldStyle.copy(fontSize = dayFontSize),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeeklyCalendarPreview() {
    val previewModel = WeeklyStickersModel(
        weekLabel = "3월 2주차",
        weekOffset = 0,
        startDate = "2026-03-10",
        endDate = "2026-03-16",
        stickers = listOf(
            WeeklyStickerModel("2026-03-10", null),
            WeeklyStickerModel("2026-03-11", "BASIC_STICKER"),
            WeeklyStickerModel("2026-03-12", null),
            WeeklyStickerModel("2026-03-13", null),
            WeeklyStickerModel("2026-03-14", "BASIC_STICKER"),
            WeeklyStickerModel("2026-03-15", null),
            WeeklyStickerModel("2026-03-16", null),
        ),
    )

    HaebomTheme {
        WeeklyCalendar(
            onPrevClick = {},
            onNextClick = {},
            weeklyStickers = Async.Success(previewModel),
        )
    }
}
