package com.swyp.firsttodo.presentation.main.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.extension.toast
import com.swyp.firsttodo.core.common.extension.widthForScreenPercentage
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.RegularStyle

@Composable
fun MainDrawer(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainDrawerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            MainDrawerSideEffect.Dismiss -> onDismiss()
            MainDrawerSideEffect.NavigateToLogin -> onDismiss()
            is MainDrawerSideEffect.ShowToast -> context.toast(effect.message)
            MainDrawerSideEffect.NavigateToFamily -> {}
            MainDrawerSideEffect.NavigateToShare -> {}
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HaebomTheme.colors.black.copy(alpha = 0.5f))
                    .noRippleClickable(onDismiss),
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(tween(300)) { -it },
            exit = slideOutHorizontally(tween(300)) { -it },
        ) {
            MainDrawerContent(
                uiState = uiState,
                onFamilyClick = viewModel::onFamilyClick,
                onShareClick = viewModel::onShareClick,
                onLogoutClick = viewModel::onLogoutClick,
                onWithdrawClick = viewModel::onWithdrawalClick,
            )
        }
    }
}

@Composable
private fun MainDrawerContent(
    uiState: MainDrawerUiState,
    onFamilyClick: () -> Unit,
    onShareClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .widthForScreenPercentage(280.dp)
            .fillMaxHeight()
            .background(HaebomTheme.colors.white)
            .padding(horizontal = screenWidthDp(16.dp)),
    ) {
        DrawerNickname(
            nickname = uiState.nickname,
            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
        )

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 24.dp),
            thickness = 1.dp,
            color = HaebomTheme.colors.gray200,
        )

        DrawerTextButton(
            text = "가족보기",
            onClick = onFamilyClick,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        DrawerTextButton(
            text = "공유관리",
            onClick = onShareClick,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        DrawerTextButton(
            text = "로그아웃",
            onClick = onLogoutClick,
            modifier = Modifier.padding(bottom = 24.dp),
        )

        DrawerTextButton(
            text = "계정탈퇴",
            onClick = onWithdrawClick,
        )
    }
}

@Composable
private fun DrawerNickname(
    nickname: Async<String>,
    modifier: Modifier = Modifier,
) {
    when (nickname) {
        is Async.Success -> Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = nickname.data,
                color = HaebomTheme.colors.black,
                style = HaebomTheme.typo.card,
            )

            Text(
                text = "님",
                color = HaebomTheme.colors.gray300,
                style = RegularStyle.copy(fontSize = 18.sp),
            )
        }

        else -> Box(modifier = modifier.size(26.dp))
    }
}

@Composable
fun DrawerTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .noRippleClickable(onClick)
            .fillMaxWidth(),
        color = HaebomTheme.colors.gray400,
        style = HaebomTheme.typo.subtitle,
    )
}

@Preview
@Composable
private fun MainDrawerContentPreview() {
    HaebomTheme {
        MainDrawerContent(
            uiState = MainDrawerUiState(nickname = Async.Success("엄마는 외계인")),
            onFamilyClick = {},
            onShareClick = {},
            onLogoutClick = {},
            onWithdrawClick = {},
        )
    }
}
