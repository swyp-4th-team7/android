package com.swyp.firsttodo.presentation.onboarding

import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.domain.model.Role

enum class OnboardingStep(
    val title: String,
) {
    ROLE_SELECT("역할 선택"),
    PROFILE("프로필 설정"),
    DONE("프로필 설정"),
}

data class OnboardingUiState(
    val currentStep: OnboardingStep = OnboardingStep.ROLE_SELECT,
    val selectedRole: Role? = null,
) : UiState

sealed interface OnboardingSideEffect : UiEffect {
    data object NavigateToHome : OnboardingSideEffect
}
