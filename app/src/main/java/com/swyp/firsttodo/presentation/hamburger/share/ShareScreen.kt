package com.swyp.firsttodo.presentation.hamburger.share

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.family.ConnectedFamilyModel
import com.swyp.firsttodo.presentation.common.component.DeleteDialogType
import com.swyp.firsttodo.presentation.common.component.HaebomDeleteDialog
import com.swyp.firsttodo.presentation.common.component.HaebomTopBar
import com.swyp.firsttodo.presentation.hamburger.share.component.CodeInputSection
import com.swyp.firsttodo.presentation.hamburger.share.component.ConnectedFamilySection
import com.swyp.firsttodo.presentation.hamburger.share.component.MyCodeSection
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar

@Composable
fun ShareRoute(
    popBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShareViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHostState.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is ShareSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
        }
    }

    if (uiState.showDialog) {
        HaebomDeleteDialog(
            dialogType = DeleteDialogType.Disconnect(uiState.dialogNickname),
            onConfirm = viewModel::disconnectFamily,
            onCancel = viewModel::closeDialog,
            onDismiss = viewModel::closeDialog,
            confirmBtnLabel = "네",
            cancelBtnLabel = "아니요",
        )
    }

    ShareScreen(
        uiState = uiState,
        onPopBackStack = popBackStack,
        codeFieldState = viewModel.codeFieldState,
        onDoneAction = viewModel::onInviteCodeInputDone,
        onCopyBtnClick = viewModel::onInviteCodeCopy,
        onDisconnectClick = viewModel::onDisconnectClick,
        modifier = modifier,
    )
}

@Composable
fun ShareScreen(
    uiState: ShareUiState,
    codeFieldState: TextFieldState,
    onPopBackStack: () -> Unit,
    onDoneAction: () -> Unit,
    onCopyBtnClick: () -> Unit,
    onDisconnectClick: (ConnectedFamilyModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        topBar = {
            HaebomTopBar(
                title = "가족보기",
                onBackClick = onPopBackStack,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 36.dp),
        ) {
            CodeInputSection(
                fieldState = codeFieldState,
                modifier = Modifier.padding(bottom = 28.dp),
                onDoneAction = onDoneAction,
                errorText = uiState.codeErrorText,
            )

            MyCodeSection(
                inviteCode = uiState.inviteCode,
                onCopyBtnClick = onCopyBtnClick,
                modifier = Modifier.padding(bottom = 52.dp),
            )

            ConnectedFamilySection(
                families = uiState.families,
                onDisconnectBtnClick = onDisconnectClick,
                modifier = Modifier.padding(bottom = 28.dp),
            )
        }
    }
}

private class ShareScreenPreviewProvider : PreviewParameterProvider<ShareUiState> {
    override val values = sequenceOf(
        ShareUiState(),
        ShareUiState(
            inviteCode = Async.Success("ABC123XY"),
            families = Async.Empty,
        ),
        ShareUiState(
            inviteCode = Async.Success("ABC123XY"),
            families = Async.Success(
                listOf(
                    ConnectedFamilyModel(userId = 1L, nickname = "엄마는외계인"),
                    ConnectedFamilyModel(userId = 2L, nickname = "아빠"),
                ),
            ),
        ),
        ShareUiState(
            inviteCode = Async.Success("ABC123XY"),
            families = Async.Success(
                listOf(ConnectedFamilyModel(userId = 1L, nickname = "엄마")),
            ),
            codeErrorText = "올바르지 않은 초대 코드예요. 다시 확인해 주세요.",
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun ShareScreenPreview(
    @PreviewParameter(ShareScreenPreviewProvider::class) uiState: ShareUiState,
) {
    HaebomTheme {
        ShareScreen(
            uiState = uiState,
            codeFieldState = rememberTextFieldState(),
            onPopBackStack = {},
            onDoneAction = {},
            onCopyBtnClick = {},
            onDisconnectClick = {},
        )
    }
}
