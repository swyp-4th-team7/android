package com.swyp.firsttodo.presentation.hamburger.family

import com.swyp.firsttodo.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel
    @Inject
    constructor() : BaseViewModel<FamilyUiState, FamilySideEffect>(FamilyUiState())
