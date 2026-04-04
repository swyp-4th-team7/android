package com.swyp.firsttodo.presentation.onboarding

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.presentation.common.component.HaebomLargeButton
import com.swyp.firsttodo.presentation.common.component.HaebomTopBar
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar
import com.swyp.firsttodo.presentation.onboarding.component.ProfileView
import com.swyp.firsttodo.presentation.onboarding.component.RoleSelectView

@Composable
fun OnboardingRoute(
    navigateToTodo: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val bottomBtnEnabled by viewModel.bottomBtnEnabled.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val snackbarHost = LocalSnackbarHostState.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            OnboardingSideEffect.NavigateToTodo -> navigateToTodo()
            OnboardingSideEffect.FinishApp -> activity.finish()
            is OnboardingSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
        }
    }

    BackHandler {
        viewModel.onBack()
    }

    OnboardingScreen(
        uiState = uiState,
        bottomBtnEnabled = bottomBtnEnabled,
        nickNameFieldState = viewModel.nickNameFieldState,
        onBackClick = viewModel::onBack,
        onBottomBtnClick = viewModel::onBottomBtnClick,
        onRoleClick = viewModel::onRoleClick,
        modifier = modifier,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    bottomBtnEnabled: Boolean,
    nickNameFieldState: TextFieldState,
    onBackClick: () -> Unit,
    onBottomBtnClick: () -> Unit,
    onRoleClick: (Role) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            HaebomTopBar(
                title = uiState.topBarTitle,
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            if (uiState.showButton) {
                HaebomLargeButton(
                    text = uiState.bottomBtnText,
                    onClick = onBottomBtnClick,
                    enabled = bottomBtnEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(32.dp))
                        .padding(bottom = screenHeightDp(32.dp)),
                )
            }
        },
    ) { innerPadding ->
        when (uiState.currentStep) {
            OnboardingStep.ROLE_SELECT -> RoleSelectView(
                selectedRole = uiState.selectedRole,
                onRoleClick = onRoleClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = screenWidthDp(32.dp))
                    .padding(top = screenHeightDp(80.dp)),
            )

            OnboardingStep.PROFILE -> ProfileView(
                fieldState = nickNameFieldState,
                errorText = uiState.nicknameErrorText,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = screenWidthDp(32.dp))
                    .padding(top = screenHeightDp(80.dp)),
            )
        }
    }
}

private class OnboardingScreenPreviewProvider : PreviewParameterProvider<OnboardingUiState> {
    override val values = sequenceOf(
        OnboardingUiState(currentStep = OnboardingStep.ROLE_SELECT),
        OnboardingUiState(currentStep = OnboardingStep.PROFILE),
    )
}

@Preview
@Composable
private fun OnboardingScreenPreview(
    @PreviewParameter(OnboardingScreenPreviewProvider::class) uiState: OnboardingUiState,
) {
    HaebomTheme {
        OnboardingScreen(
            uiState = uiState,
            bottomBtnEnabled = true,
            nickNameFieldState = rememberTextFieldState(),
            onBackClick = {},
            onBottomBtnClick = {},
            onRoleClick = {},
        )
    }
}
