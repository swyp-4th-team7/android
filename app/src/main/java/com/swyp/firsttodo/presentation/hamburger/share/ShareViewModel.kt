package com.swyp.firsttodo.presentation.hamburger.share

import androidx.compose.foundation.text.input.TextFieldState
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.domain.model.family.ConnectedFamilyModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel
    @Inject
    constructor() : BaseViewModel<ShareUiState, ShareSideEffect>(ShareUiState()) {
        val codeFieldState = TextFieldState()

        fun closeDialog() {}

        fun disconnectFamily() {}

        fun onInviteCodeInputDone() {}

        fun onInviteCodeCopy() {}

        fun onDisconnectClick(member: ConnectedFamilyModel) {}
    }
