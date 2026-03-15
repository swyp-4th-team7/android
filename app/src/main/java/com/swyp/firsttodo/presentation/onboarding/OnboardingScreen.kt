package com.swyp.firsttodo.presentation.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.common.util.HandleSideEffects

@Composable
fun OnboardingRoute(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            OnboardingSideEffect.NavigateToHome -> onNavigateToHome()
        }
    }

    OnboardingScreen(
        uiState = uiState,
        modifier = modifier,
    )
}

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
//        when (uiState.currentStep) {
//            OnboardingStep.ROLE_SELECT -> TODO()
//            OnboardingStep.PROFILE -> TODO()
//            OnboardingStep.DONE -> TODO()
//        }
    }
}
