package com.swyp.firsttodo.presentation.reward.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor
import com.swyp.firsttodo.domain.model.reward.RewardStatus

val RewardStatus.displayName: String
    get() = when (this) {
        RewardStatus.IN_PROGRESS -> "진행중"
        RewardStatus.REWARD_CHECKING -> "보상 확인중"
        RewardStatus.REWARD_WAITING -> "보상 대기중"
        RewardStatus.COMPLETE -> "완료"
    }

val RewardStatus.textColor: Color
    @Composable get() = when (this) {
        RewardStatus.REWARD_CHECKING -> HaebomTheme.colors.labelRedText
        RewardStatus.IN_PROGRESS -> LabelColor.SKY_BLUE.text
        RewardStatus.REWARD_WAITING -> LabelColor.MINT.text
        RewardStatus.COMPLETE -> HaebomTheme.colors.gray300
    }

val RewardStatus.backgroundColor: Color
    @Composable get() = when (this) {
        RewardStatus.REWARD_CHECKING -> HaebomTheme.colors.labelRedBackground
        RewardStatus.IN_PROGRESS -> LabelColor.SKY_BLUE.background
        RewardStatus.REWARD_WAITING -> LabelColor.LIME.background
        RewardStatus.COMPLETE -> HaebomTheme.colors.gray100
    }
