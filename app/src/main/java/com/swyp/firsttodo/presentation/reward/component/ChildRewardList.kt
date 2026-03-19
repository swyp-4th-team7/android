package com.swyp.firsttodo.presentation.reward.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.presentation.reward.model.RewardState
import com.swyp.firsttodo.presentation.reward.model.backgroundColor
import com.swyp.firsttodo.presentation.reward.model.textColor

data class ChildRewardUiModel(
    val rewardId: Long,
    val title: String,
    val reward: String,
    @param:DrawableRes val defaultIconRes: Int,
    @param:DrawableRes val completedIconRes: Int,
    val rewardState: RewardState,
)

@Composable
fun ChildRewardList(
    rewards: Async<List<ChildRewardUiModel>>,
    onCreateHabitBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (rewards) {
        Async.Empty -> RewardMangeEmptyView(
            onCreateHabitBtnClick = onCreateHabitBtnClick,
        )

        else -> {
            when (val data = rewards.getDataOrNull()) {
                null -> Unit

                else -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    data.forEach { reward ->
                        val (backgroundColor, iconRes) = when (reward.rewardState) {
                            RewardState.DONE -> HaebomTheme.colors.gray50 to reward.completedIconRes
                            else -> HaebomTheme.colors.white to reward.defaultIconRes
                        }

                        Row(
                            modifier = Modifier
                                .height(IntrinsicSize.Max)
                                .background(
                                    color = backgroundColor,
                                    shape = RoundedCornerShape(4.dp),
                                )
                                .border(
                                    width = 1.5.dp,
                                    color = HaebomTheme.colors.gray100,
                                    shape = RoundedCornerShape(4.dp),
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(iconRes),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(Alignment.Top)
                                    .padding(horizontal = 12.dp),
                                tint = Color.Unspecified,
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 12.dp),
                            ) {
                                Text(
                                    text = reward.title,
                                    color = HaebomTheme.colors.black,
                                    style = HaebomTheme.typo.description,
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
                                        text = reward.reward,
                                        color = HaebomTheme.colors.gray400,
                                        style = HaebomTheme.typo.helperText,
                                    )
                                }
                            }

                            VerticalDivider(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .padding(vertical = 8.dp)
                                    .padding(start = 8.dp),
                                thickness = 0.8.dp,
                                color = HaebomTheme.colors.gray100,
                            )

                            Box(
                                modifier = Modifier.padding(all = 12.dp),
                            ) {
                                Text(
                                    text = reward.rewardState.displayName,
                                    modifier = Modifier
                                        .sizeIn(88.dp, 32.dp)
                                        .background(
                                            color = reward.rewardState.backgroundColor,
                                            shape = RoundedCornerShape(8.dp),
                                        )
                                        .padding(all = 4.dp)
                                        .wrapContentHeight(Alignment.CenterVertically),
                                    color = reward.rewardState.textColor,
                                    textAlign = TextAlign.Center,
                                    style = HaebomTheme.typo.section,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private val previewRewards = listOf(
    ChildRewardUiModel(
        rewardId = 1L,
        title = "하루 10분 명상하기",
        reward = "아이스크림 사주기",
        defaultIconRes = R.drawable.ic_habit_day_7,
        completedIconRes = R.drawable.ic_habit_day_7,
        rewardState = RewardState.ING,
    ),
    ChildRewardUiModel(
        rewardId = 2L,
        title = "하루에 책 10장 읽기 하루에 책 10장 읽기 하루에 책 10장 읽기",
        reward = "가족이랑 놀이공원 가기 가족이랑 놀이공원 가기 가족이랑 놀이공원",
        defaultIconRes = R.drawable.ic_habit_day_7,
        completedIconRes = R.drawable.ic_habit_day_7,
        rewardState = RewardState.ING,
    ),
    ChildRewardUiModel(
        rewardId = 3L,
        title = "매일 아침 스트레칭",
        reward = "영화 보러 가기",
        defaultIconRes = R.drawable.ic_habit_day_7,
        completedIconRes = R.drawable.ic_habit_day_7,
        rewardState = RewardState.WAITING,
    ),
    ChildRewardUiModel(
        rewardId = 4L,
        title = "매일 아침 스트레칭 10분 매일 아침 스트레칭 10분 매일 아침",
        reward = "가족과 외식하기 가족과 외식하기 가족과 외식하기 가족과 외식하기",
        defaultIconRes = R.drawable.ic_habit_day_7,
        completedIconRes = R.drawable.ic_habit_day_7,
        rewardState = RewardState.WAITING,
    ),
    ChildRewardUiModel(
        rewardId = 5L,
        title = "취침 전 일기 쓰기",
        reward = "좋아하는 카페 가기",
        defaultIconRes = R.drawable.ic_habit_day_7,
        completedIconRes = R.drawable.ic_habit_day_7,
        rewardState = RewardState.DONE,
    ),
    ChildRewardUiModel(
        rewardId = 6L,
        title = "취침 전 일기 쓰기 취침 전 일기 쓰기 취침 전 일기 쓰기 취침 전",
        reward = "좋아하는 카페 가기 좋아하는 카페 가기 좋아하는 카페 가기 좋아",
        defaultIconRes = R.drawable.ic_habit_day_7,
        completedIconRes = R.drawable.ic_habit_day_7,
        rewardState = RewardState.DONE,
    ),
)

private class ChildRewardListPreviewProvider : PreviewParameterProvider<Async<List<ChildRewardUiModel>>> {
    override val values: Sequence<Async<List<ChildRewardUiModel>>> = sequenceOf(
        Async.Success(previewRewards),
        Async.Empty,
        Async.Loading(),
    )
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun ChildRewardListPreview(
    @PreviewParameter(ChildRewardListPreviewProvider::class) rewards: Async<List<ChildRewardUiModel>>,
) {
    HaebomTheme {
        ChildRewardList(
            rewards = rewards,
            onCreateHabitBtnClick = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        )
    }
}
