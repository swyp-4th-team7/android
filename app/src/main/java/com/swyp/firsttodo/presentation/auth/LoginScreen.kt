@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.swyp.firsttodo.presentation.auth

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.swyp.firsttodo.core.common.extension.heightForScreenPercentage
import com.swyp.firsttodo.core.common.extension.widthForScreenPercentage
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.presentation.auth.component.GoogleLoginButton
import com.swyp.firsttodo.presentation.auth.component.IntroPager
import com.swyp.firsttodo.presentation.auth.component.LegalLinks
import com.swyp.firsttodo.presentation.auth.launcher.GoogleLauncher
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    popBackStack: () -> Unit,
    navigateToTodo: () -> Unit,
    navigateToOnboarding: () -> Unit,
    navigateToWebView: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val snackbarHost = LocalSnackbarHostState.current
    val context = LocalContext.current
    val activity = context as Activity

    val googleLauncher = remember(context) {
        GoogleLauncher(activity)
    }

    val scope = rememberCoroutineScope()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            LoginSideEffect.PopBackStack -> popBackStack()

            LoginSideEffect.NavigateToHome -> navigateToTodo()

            LoginSideEffect.LaunchGoogleLogin -> {
                scope.launch {
                    val result = googleLauncher.startGoogleLogin()
                    viewModel.onGoogleLoginResult(result)
                }
            }

            is LoginSideEffect.NavigateToWebView -> navigateToWebView(effect.title, effect.url)

            is LoginSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)

            LoginSideEffect.NavigateToOnboarding -> navigateToOnboarding()
        }
    }

    BackHandler {
        viewModel.onBack()
    }

    LoginScreen(
        onGoogleLoginClick = viewModel::onGoogleLoginClick,
        onTosClick = viewModel::onTosClick,
        onPrivacyClick = viewModel::onPrivacyClick,
        modifier = modifier,
    )
}

@Composable
fun LoginScreen(
    onGoogleLoginClick: () -> Unit,
    onTosClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IntroPager(
            modifier = Modifier
                .widthForScreenPercentage(296.dp)
                .weight(1f),
        )

        Column(
            modifier = Modifier.padding(top = screenHeightDp(59.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            GoogleLoginButton(
                onClick = onGoogleLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(32.dp)),
            )

            Spacer(Modifier.heightForScreenPercentage(12.dp))

            LegalLinks(
                onTosClick = onTosClick,
                onPrivacyClick = onPrivacyClick,
                modifier = Modifier.padding(horizontal = screenWidthDp(32.dp)),
            )

            Spacer(Modifier.heightForScreenPercentage(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    HaebomTheme {
        LoginScreen(
            onGoogleLoginClick = {},
            onTosClick = {},
            onPrivacyClick = {},
        )
    }
}
