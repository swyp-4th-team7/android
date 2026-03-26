package com.swyp.firsttodo.presentation.hamburger.share

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.model.family.ConnectedFamilyModel
import com.swyp.firsttodo.domain.repository.FamilyRepository
import com.swyp.firsttodo.domain.throwable.FamilyError
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel
    @Inject
    constructor(
        private val familyRepository: FamilyRepository,
    ) : BaseViewModel<ShareUiState, ShareSideEffect>(ShareUiState()) {
        val codeFieldState = TextFieldState()

        init {
            getFamiles()
            getMyCode()
        }

        private fun getFamiles() {
            viewModelScope.launch {
                familyRepository.getConnectedFamily()
                    .onSuccess {
                        updateState { copy(families = if (it.isEmpty()) Async.Empty else Async.Success(it)) }
                    }
                    .onFailure {
                        if (it is ApiError) sendEffect(ShareSideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }

        private fun getMyCode() {
            viewModelScope.launch {
                familyRepository.getMyInviteCode()
                    .onSuccess {
                        updateState { copy(inviteCode = Async.Success(it)) }
                    }
                    .onFailure {
                        when (it) {
                            is FamilyError.OnboardingUncompleted -> {
                                sendEffect(ShareSideEffect.ShowSnackbar("프로필 설정이 필요합니다."))
                                sendEffect(ShareSideEffect.NavigateToOnboarding)
                            }

                            is ApiError -> sendEffect(ShareSideEffect.ShowSnackbar(it.snackbarMsg()))
                        }
                    }
            }
        }

        fun closeDialog() {
            updateState { copy(disconnectRequestMember = null) }
        }

        fun disconnectFamily() {
            val targetUserId = uiState.value.disconnectRequestMember?.userId ?: return

            viewModelScope.launch {
                updateState { copy(disconnectState = Async.Loading()) }

                familyRepository.disconnectFamily(targetUserId)
                    .onSuccess {
                        updateState { copy(disconnectState = Async.Success(Unit)) }
                        closeDialog()
                        getFamiles()
                        sendEffect(ShareSideEffect.ShowSnackbar("가족 연결이 끊겼습니다."))
                    }
                    .onFailure { throwable ->
                        updateState { copy(disconnectState = Async.Init) }
                        closeDialog()

                        val message = when (throwable) {
                            is FamilyError.ConnectInvalid -> {
                                getFamiles()
                                "이미 연결 해제된 가족입니다."
                            }

                            is ApiError -> throwable.snackbarMsg()

                            else -> return@onFailure
                        }
                        sendEffect(ShareSideEffect.ShowSnackbar(message))
                    }
            }
        }

        fun onInviteCodeInputDone() {
            val code = codeFieldState.text.toString()

            if (code.isBlank()) return

            viewModelScope.launch {
                familyRepository.connectFamily(code)
                    .onSuccess {
                        updateState { copy(codeErrorText = null) }
                        codeFieldState.clearText()
                        getFamiles()
                        sendEffect(ShareSideEffect.ShowSnackbar("가족과 연결되었습니다."))
                    }
                    .onFailure { throwable ->
                        when (throwable) {
                            is FamilyError.InviteAlreadyDone -> {
                                getFamiles()
                                codeFieldState.clearText()
                                sendEffect(ShareSideEffect.ShowSnackbar("이미 연결된 가족입니다."))
                            }

                            is FamilyError.InviteCodeEmpty, is FamilyError.InviteCodeInvalid,
                            is FamilyError.InviteCodeMySelf,
                            -> {
                                updateState { copy(codeErrorText = "올바르지 않은 초대 코드예요. 다시 확인해 주세요.") }
                            }

                            is ApiError -> {
                                sendEffect(ShareSideEffect.ShowSnackbar(throwable.snackbarMsg()))
                            }

                            else -> return@launch
                        }
                    }
            }
        }

        fun onInviteCodeCopy() {}

        fun onDisconnectClick(member: ConnectedFamilyModel) {
            updateState { copy(disconnectRequestMember = member, disconnectState = Async.Init) }
        }
    }
