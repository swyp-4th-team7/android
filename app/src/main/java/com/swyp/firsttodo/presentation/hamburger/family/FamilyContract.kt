package com.swyp.firsttodo.presentation.hamburger.family

import androidx.compose.runtime.Immutable
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.UiEffect
import com.swyp.firsttodo.core.base.UiState
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.domain.model.family.FamilyInfo

@Immutable
data class FamilyUiState(
    val familyInfos: Async<List<FamilyInfo>> = Async.Init,
) : UiState {
    val familyData = familyInfos.getDataOrNull()
}

sealed interface FamilySideEffect : UiEffect {
    data class ShowSnackbar(val message: String) : FamilySideEffect
}
