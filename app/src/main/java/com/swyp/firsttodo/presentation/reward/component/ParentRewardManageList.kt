package com.swyp.firsttodo.presentation.reward.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.swyp.firsttodo.R
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.presentation.reward.model.RewardState
import com.swyp.firsttodo.presentation.reward.model.backgroundColor
import com.swyp.firsttodo.presentation.reward.model.textColor

data class ParentRewardUiModel(
    val id: Long,
    val title: String,
    val habit: String,
    val reward: String,
    val rewardState: RewardState,
    @param:DrawableRes val habitIconRes: Int,
)

@Composable
fun ParentRewardManageList(
    rewards: Async<List<ParentRewardUiModel>>,
    onLabelClick: (ParentRewardUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (rewards) {
        Async.Empty -> ParentRewardEmptyView(
            title = "아직 관리할 보상이 없습니다.",
            description = "자녀의 습관을 만들어주세요.",
        )

        else -> {
            when (val data = rewards.getDataOrNull()) {
                null -> Unit

                else -> Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    data.forEach { reward ->
                        Column(
                            modifier = Modifier
                                .background(
                                    color = HaebomTheme.colors.gray50,
                                    shape = RoundedCornerShape(8.dp),
                                )
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = reward.title,
                                    modifier = Modifier.padding(top = 16.dp),
                                    color = HaebomTheme.colors.black,
                                    style = HaebomTheme.typo.buttonL,
                                )

                                Icon(
                                    imageVector = ImageVector.vectorResource(reward.habitIconRes),
                                    contentDescription = null,
                                    tint = Color.Unspecified,
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .background(
                                        color = HaebomTheme.colors.white,
                                        shape = RoundedCornerShape(4.dp),
                                    )
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Text(
                                        text = "습관 : ",
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                    )

                                    Text(
                                        text = "보상 : ",
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                    )
                                }

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    Text(
                                        text = reward.habit,
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                    Text(
                                        text = reward.reward,
                                        color = HaebomTheme.colors.gray300,
                                        style = HaebomTheme.typo.helperText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .noRippleClickable({ onLabelClick(reward) })
                                        .padding(vertical = 8.dp)
                                        .padding(start = 8.dp),
                                ) {
                                    Text(
                                        text = reward.rewardState.displayName,
                                        modifier = Modifier
                                            .sizeIn(96.dp, 36.dp)
                                            .background(
                                                color = reward.rewardState.backgroundColor,
                                                shape = RoundedCornerShape(4.dp),
                                            )
                                            .padding(all = 5.dp)
                                            .wrapContentSize(Alignment.Center),
                                        color = reward.rewardState.textColor,
                                        style = HaebomTheme.typo.buttonL,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private val sampleRewards = listOf(
    ParentRewardUiModel(
        id = 1L,
        title = "수학 공부하기",
        habit = "매일 수학 문제 풀기",
        reward = "치킨 사주기",
        rewardState = RewardState.ING,
        habitIconRes = R.drawable.ic_habit_day_7,
    ),
    ParentRewardUiModel(
        id = 2L,
        title = "영어 단어 외우기",
        habit = "매일 영어 단어 20개",
        reward = "게임 1시간",
        rewardState = RewardState.CONFIRMING,
        habitIconRes = R.drawable.ic_habit_day_3,
    ),
    ParentRewardUiModel(
        id = 3L,
        title = "독서하기",
        habit = "하루 30분 독서",
        reward = "문화상품권",
        rewardState = RewardState.WAITING,
        habitIconRes = R.drawable.ic_habit_day_7,
    ),
    ParentRewardUiModel(
        id = 4L,
        title = "운동하기",
        habit = "줄넘기 100개",
        reward = "아이스크림",
        rewardState = RewardState.DONE,
        habitIconRes = R.drawable.ic_habit_day_7,
    ),
)

private class ParentRewardManageListPreviewProvider : PreviewParameterProvider<Async<List<ParentRewardUiModel>>> {
    override val values: Sequence<Async<List<ParentRewardUiModel>>> = sequenceOf(
        Async.Success(sampleRewards),
        Async.Empty,
        Async.Loading(),
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun ParentRewardManageListPreview(
    @PreviewParameter(ParentRewardManageListPreviewProvider::class) rewards: Async<List<ParentRewardUiModel>>,
) {
    HaebomTheme {
        ParentRewardManageList(
            rewards = rewards,
            onLabelClick = {},
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp),
        )
    }
}
