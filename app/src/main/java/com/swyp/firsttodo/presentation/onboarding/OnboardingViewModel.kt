package com.swyp.firsttodo.presentation.onboarding

import com.swyp.firsttodo.core.base.BaseViewModel
import com.swyp.firsttodo.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : BaseViewModel<OnboardingUiState, OnboardingSideEffect>(OnboardingUiState())
