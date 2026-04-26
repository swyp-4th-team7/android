package com.swyp.firsttodo.presentation.hamburger.family.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.family.FamilyHabit
import com.swyp.firsttodo.domain.model.family.FamilyInfo
import com.swyp.firsttodo.domain.model.family.FamilyTodo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

private const val FIRST_COL_WEIGHT = 100f
private const val SECOND_COL_WEIGHT = 104f
private const val THIRD_COL_WEIGHT = 88f

@Composable
fun FamilyDashBoard(
    familyInfos: ImmutableList<FamilyInfo>,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    Column(
        modifier = modifier
            .background(
                color = HaebomTheme.colors.gray50,
                shape = RoundedCornerShape(4.dp),
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            Spacer(Modifier.weight(FIRST_COL_WEIGHT))

            Text(
                text = "할 일",
                modifier = Modifier.weight(SECOND_COL_WEIGHT),
                color = HaebomTheme.colors.gray400,
                textAlign = TextAlign.Center,
                style = HaebomTheme.typo.caption,
            )

            Text(
                text = "습관",
                modifier = Modifier.weight(THIRD_COL_WEIGHT),
                color = HaebomTheme.colors.gray400,
                textAlign = TextAlign.Center,
                style = HaebomTheme.typo.caption,
            )
        }

        familyInfos.forEachIndexed { index, info ->
            Row(
                modifier = Modifier
                    .heightIn(min = 61.dp)
                    .height(IntrinsicSize.Min)
                    .drawBehind {
                        if (index < familyInfos.lastIndex) {
                            drawLine(
                                color = colors.gray200,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 2.dp.toPx(),
                            )
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = info.nickname,
                    modifier = Modifier
                        .weight(FIRST_COL_WEIGHT)
                        .fillMaxHeight()
                        .drawBehind {
                            drawLine(
                                color = colors.gray200,
                                start = Offset(size.width, 0f),
                                end = Offset(size.width, size.height),
                                strokeWidth = 2.dp.toPx(),
                            )
                        }
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .wrapContentHeight(Alignment.CenterVertically),
                    color = HaebomTheme.colors.gray500,
                    style = HaebomTheme.typo.buttonM,
                )

                Text(
                    text = "${info.todo.completedCount}/${info.todo.totalCount}",
                    modifier = Modifier
                        .weight(SECOND_COL_WEIGHT)
                        .padding(horizontal = 5.dp, vertical = 16.dp)
                        .wrapContentSize(Alignment.Center),
                    color = colors.gray500,
                    style = HaebomTheme.typo.screen,
                )

                Icon(
                    imageVector = ImageVector.vectorResource(
                        if (info.habit.completed) {
                            R.drawable.ic_check_filled
                        } else {
                            R.drawable.ic_check_unfilled
                        },
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(THIRD_COL_WEIGHT)
                        .padding(horizontal = 5.dp, vertical = 16.dp)
                        .wrapContentSize(Alignment.Center),
                    tint = Color.Unspecified,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FamilyDashBoardPreview() {
    HaebomTheme {
        FamilyDashBoard(
            familyInfos = persistentListOf(
                FamilyInfo(1L, "엄마는외계인", FamilyTodo(10, 3), FamilyHabit(completed = true)),
                FamilyInfo(2L, "박영희영희영희영희영희", FamilyTodo(10, 10), FamilyHabit(completed = false)),
                FamilyInfo(3L, "이민준", FamilyTodo(5, 0), FamilyHabit(completed = true)),
                FamilyInfo(4L, "최서연서연서연서연서연서연서연서연서연서연서연서연", FamilyTodo(100, 99), FamilyHabit(completed = false)),
            ),
            modifier = Modifier.padding(16.dp),
        )
    }
}
