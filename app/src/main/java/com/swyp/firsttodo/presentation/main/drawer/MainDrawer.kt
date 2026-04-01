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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.noRippleClickable
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.core.designsystem.theme.RegularStyle
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar

@Composable
fun MainDrawer(
    visible: Boolean,
    onDismiss: () -> Unit,
    onNavigateToFamily: () -> Unit,
    onNavigateToShare: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainDrawerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHostState.current

    LaunchedEffect(visible) {
        if (visible) viewModel.getMyInfo()
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            MainDrawerSideEffect.Dismiss -> onDismiss()
            MainDrawerSideEffect.NavigateToLogin -> onDismiss()
            is MainDrawerSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
            MainDrawerSideEffect.NavigateToFamily -> {
                onDismiss()
                onNavigateToFamily()
            }

            MainDrawerSideEffect.NavigateToShare -> {
                onDismiss()
                onNavigateToShare()
            }
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

        if (uiState.showDialog) {
            MainDrawerDialog(
                dialogType = uiState.dialogType,
                onDismiss = viewModel::closeDialog,
                onCancel = viewModel::closeDialog,
                onConfirm = viewModel::onDialogConfirmBtnClick,
                loadingState = uiState.dialogLoadingState,
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
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val drawerWidth = if (screenWidth.value / screenHeight.value >= 0.75f) screenWidth / 2 else screenWidthDp(280.dp)

    Column(
        modifier = Modifier
            .width(drawerWidth)
            .fillMaxHeight()
            .background(HaebomTheme.colors.white),
    ) {
        DrawerNickname(
            nickname = uiState.nickname,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 23.dp),
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp, bottom = 8.dp),
            thickness = 1.dp,
            color = HaebomTheme.colors.gray200,
        )

        DrawerTextButton(
            drawerType = DrawerType.FAMILY,
            currentType = uiState.currentDrawer,
            onClick = onFamilyClick,
        )

        DrawerTextButton(
            drawerType = DrawerType.SHARE,
            currentType = uiState.currentDrawer,
            onClick = onShareClick,
        )

        DrawerTextButton(
            drawerType = DrawerType.LOGOUT,
            currentType = uiState.currentDrawer,
            onClick = onLogoutClick,
        )

        DrawerTextButton(
            drawerType = DrawerType.WITHDRAWAL,
            currentType = uiState.currentDrawer,
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
private fun DrawerTextButton(
    drawerType: DrawerType,
    currentType: DrawerType?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = HaebomTheme.colors

    val (backgroundColor, textColor) = remember(currentType, colors) {
        when (drawerType == currentType) {
            true -> colors.gray50 to colors.black
            false -> colors.white to colors.gray400
        }
    }

    Text(
        text = drawerType.displayName,
        modifier = modifier
            .noRippleClickable(onClick)
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .wrapContentHeight(Alignment.CenterVertically),
        color = textColor,
        style = HaebomTheme.typo.subtitle,
    )
}

@Preview
@Composable
private fun MainDrawerContentPreview() {
    HaebomTheme {
        MainDrawerContent(
            uiState = MainDrawerUiState(
                nickname = Async.Success("엄마는 외계인"),
                currentDrawer = DrawerType.FAMILY,
            ),
            onFamilyClick = {},
            onShareClick = {},
            onLogoutClick = {},
            onWithdrawClick = {},
        )
    }
}
