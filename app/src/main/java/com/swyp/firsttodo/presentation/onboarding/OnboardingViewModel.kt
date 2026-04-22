package com.swyp.firsttodo.presentation.onboarding

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.error.ProfileError
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.usecase.user.SaveOnboardingProfile
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val saveOnboardingProfile: SaveOnboardingProfile,
    ) : BaseViewModel<OnboardingUiState, OnboardingSideEffect>(OnboardingUiState()) {
        val nickNameFieldState = TextFieldState()

        private var lastBackPressTime = 0L

        private val validNicknameRegex = Regex("^[가-힣a-zA-Z]{1,12}$")

        init {
            snapshotFlow { nickNameFieldState.text.toString() }
                .onEach { nickname ->
                    val errorText = if (nickname.isNotEmpty() && !isValidNickname(nickname)) {
                        "닉네임은 1~12자의 한글 또는 영문만 사용해 주세요."
                    } else {
                        null
                    }
                    updateState { copy(nicknameErrorText = errorText) }
                }
                .launchIn(viewModelScope)
        }

        private fun isValidNickname(nickname: String) = validNicknameRegex.matches(nickname)

        val bottomBtnEnabled: StateFlow<Boolean> = combine(
            snapshotFlow { nickNameFieldState.text.toString() },
            uiState,
        ) { nickname, state ->
            when (state.currentStep) {
                OnboardingStep.ROLE_SELECT -> state.selectedRole != null
                OnboardingStep.PROFILE -> isValidNickname(nickname)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

        fun onBack() {
            when (currentState.currentStep) {
                OnboardingStep.ROLE_SELECT -> handleDoubleBackPressToExit()
                OnboardingStep.PROFILE -> updateState { copy(currentStep = OnboardingStep.ROLE_SELECT) }
            }
        }

        fun onBottomBtnClick() {
            when (currentState.currentStep) {
                OnboardingStep.ROLE_SELECT -> updateState { copy(currentStep = OnboardingStep.PROFILE) }
                OnboardingStep.PROFILE -> saveProfile()
            }
        }

        fun onRoleClick(role: Role) {
            updateState { copy(selectedRole = role) }
        }

        private fun saveProfile() {
            val role = currentState.selectedRole ?: return
            val nickname = nickNameFieldState.text.toString()
            if (!isValidNickname(nickname)) return

            viewModelScope.launch {
                saveOnboardingProfile(nickname, role)
                    .onSuccess { sendThrottledEffect(OnboardingSideEffect.NavigateToTodo) }
                    .onFailure { throwable ->
                        Timber.e(throwable, "save onboarding profile error")

                        val message = when (throwable) {
                            is ProfileError.NicknameEmpty -> "닉네임은 필수로 입력해주세요."
                            is ProfileError.NicknameLength -> "닉네임은 1자 이상 12자 이하로 입력해주세요."
                            is ProfileError.NicknameSymbols -> "닉네임은 한글로만 입력해주세요."
                            is ProfileError.RoleEmpty -> "역할은 필수로 선택해주세요."
                            is ApiError -> throwable.snackbarMsg()
                            else -> "프로필 저장에 실패했어요."
                        }
                        sendEffect(OnboardingSideEffect.ShowSnackbar(message))
                    }
            }
        }

        private fun handleDoubleBackPressToExit() {
            val now = System.currentTimeMillis()
            if (now - lastBackPressTime < 2000L) {
                sendEffect(OnboardingSideEffect.FinishApp)
            } else {
                lastBackPressTime = now
                sendEffect(OnboardingSideEffect.ShowSnackbar("한 번 더 '뒤로가기' 누르면 앱이 종료돼요."))
            }
        }
    }
