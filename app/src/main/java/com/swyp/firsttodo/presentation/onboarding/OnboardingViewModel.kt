package com.swyp.firsttodo.presentation.onboarding

import androidx.compose.foundation.text.input.TextFieldState
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : BaseViewModel<OnboardingUiState, OnboardingSideEffect>(OnboardingUiState()) {
        val nickNameFieldState = TextFieldState()

        private var lastBackPressTime = 0L

        fun onBack() {
            when (uiState.value.currentStep) {
                OnboardingStep.ROLE_SELECT -> {
                    val now = System.currentTimeMillis()

                    if (now - lastBackPressTime < 2000L) {
                        sendEffect(OnboardingSideEffect.FinishApp)
                    } else {
                        lastBackPressTime = now
                        sendEffect(OnboardingSideEffect.ShowToast("한 번 더 '뒤로가기' 누르면 앱이 종료돼요."))
                    }
                }

                OnboardingStep.PROFILE -> updateState { copy(currentStep = OnboardingStep.ROLE_SELECT) }

                OnboardingStep.DONE -> updateState { copy(currentStep = OnboardingStep.PROFILE) }
            }
        }

        fun onBottomBtnClick() {
            when (uiState.value.currentStep) {
                OnboardingStep.ROLE_SELECT -> updateState { copy(currentStep = OnboardingStep.PROFILE) }
                OnboardingStep.PROFILE -> updateState { copy(currentStep = OnboardingStep.DONE) }
                OnboardingStep.DONE -> Unit
            }
        }

        fun bottomBtnEnabled(): Boolean {
            return true
        }

        fun onRoleClick(role: Role) {
            updateState { copy(selectedRole = role) }
        }
    }
