package com.swyp.firsttodo.presentation.onboarding

import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.domain.model.Role

enum class OnboardingStep {
    ROLE_SELECT,
    PROFILE,
    DONE,
}

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.ROLE_SELECT,
    val selectedRole: Role? = null,
    val nicknameErrorText: String? = null,
) : UiState {
    val topBarTitle = when (currentStep) {
        OnboardingStep.ROLE_SELECT -> "역할 선택"
        OnboardingStep.PROFILE -> "닉네임 설정"
        OnboardingStep.DONE -> "회원가입 완료!"
    }

    val showButton = when (currentStep) {
        OnboardingStep.DONE -> false
        else -> true
    }

    val bottomBtnText = when (currentStep) {
        OnboardingStep.ROLE_SELECT -> "다음으로"
        OnboardingStep.PROFILE -> "시작하기"
        OnboardingStep.DONE -> ""
    }
}

sealed interface OnboardingSideEffect : UiEffect {
    data object NavigateToTodo : OnboardingSideEffect

    data object FinishApp : OnboardingSideEffect

    data class ShowSnackbar(val message: String) : OnboardingSideEffect
}
