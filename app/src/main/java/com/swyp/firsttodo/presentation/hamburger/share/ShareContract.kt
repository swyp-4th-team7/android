package com.swyp.firsttodo.presentation.hamburger.share

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.domain.model.family.ConnectedFamilyModel

@Immutable
data class ShareUiState(
    val codeErrorText: String? = null,
    val inviteCode: Async<String> = Async.Init,
    val families: Async<List<ConnectedFamilyModel>> = Async.Init,
    val disconnectRequestMember: ConnectedFamilyModel? = null,
) : UiState {
    val showDialog = disconnectRequestMember != null
    val dialogNickname = disconnectRequestMember?.nickname ?: ""
}

sealed interface ShareSideEffect : UiEffect {
    data class ShowSnackbar(val message: String) : ShareSideEffect
}
