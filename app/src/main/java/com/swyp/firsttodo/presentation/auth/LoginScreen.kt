package com.swyp.firsttodo.presentation.auth

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.common.extension.toast
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.designsystem.theme.HeabomTheme
import com.swyp.firsttodo.presentation.auth.launcher.GoogleLauncher
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = context as Activity

    val googleLauncher = remember(context) {
        GoogleLauncher(activity)
    }

    val scope = rememberCoroutineScope()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            LoginSideEffect.NavigateToHome -> context.toast("로그인 성공!")

            LoginSideEffect.LaunchGoogleLogin -> {
                scope.launch {
                    val result = googleLauncher.startGoogleLogin()
                    viewModel.onGoogleLoginResult(result)
                }
            }

            is LoginSideEffect.ShowToast -> context.toast(effect.message)
        }
    }

    LoginScreen(
        uiState = uiState,
        onGoogleLoginClick = viewModel::onGoogleLoginClick,
        onLogoutClick = viewModel::onLogoutClick,
        modifier = modifier,
    )
}

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onGoogleLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "구글 로그인",
            modifier = Modifier
                .padding(100.dp)
                .clickable(onClick = onGoogleLoginClick),
            style = TextStyle(
                fontSize = 40.sp,
            ),
        )

        Text(
            text = "로그아웃",
            modifier = Modifier
                .padding(100.dp)
                .clickable(onClick = onLogoutClick),
            style = TextStyle(
                fontSize = 40.sp,
            ),
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    HeabomTheme {
        LoginScreen(
            uiState = LoginUiState(),
            onGoogleLoginClick = {},
            onLogoutClick = {},
        )
    }
}
