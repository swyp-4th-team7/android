package com.swyp.firsttodo.presentation.hamburger.family

import androidx.lifecycle.viewModelScope
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.core.network.model.ApiError
import com.swyp.firsttodo.domain.repository.FamilyRepository
import com.swyp.firsttodo.presentation.common.extension.snackbarMsg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel
    @Inject
    constructor(
        private val familyRepository: FamilyRepository,
    ) : BaseViewModel<FamilyUiState, FamilySideEffect>(FamilyUiState()) {
        init {
            getFamilyInfos()
        }

        private fun getFamilyInfos() {
            viewModelScope.launch {
                familyRepository.getFamilyDashboard()
                    .onSuccess {
                        updateState { copy(familyInfos = if (it.isEmpty()) Async.Empty else Async.Success(it)) }
                    }
                    .onFailure {
                        if (it is ApiError) sendEffect(FamilySideEffect.ShowSnackbar(it.snackbarMsg()))
                    }
            }
        }
    }
