package com.swyp.firsttodo.presentation.reward.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.LabelColor

enum class RewardState(val displayName: String) {
    CONFIRMING("보상 확인중"),
    ING("진행중"),
    WAITING("보상 대기중"),
    DONE("완료"),
}

val RewardState.textColor: Color
    @Composable get() = when (this) {
        RewardState.CONFIRMING -> HaebomTheme.colors.labelRedText
        RewardState.ING -> LabelColor.SKY_BLUE.text
        RewardState.WAITING -> LabelColor.MINT.text
        RewardState.DONE -> HaebomTheme.colors.gray300
    }

val RewardState.backgroundColor: Color
    @Composable get() = when (this) {
        RewardState.CONFIRMING -> HaebomTheme.colors.labelRedBackground
        RewardState.ING -> LabelColor.SKY_BLUE.background
        RewardState.WAITING -> LabelColor.LIME.background
        RewardState.DONE -> HaebomTheme.colors.gray100
    }
