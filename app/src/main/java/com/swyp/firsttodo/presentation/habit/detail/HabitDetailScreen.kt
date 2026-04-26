package com.swyp.firsttodo.presentation.habit.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.common.component.HaebomLargeButton
import com.swyp.firsttodo.core.common.component.HaebomMultiLineTextField
import com.swyp.firsttodo.core.common.component.TaskCategoryList
import com.swyp.firsttodo.core.common.component.TaskInputSection
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.presentation.habit.component.HabitDetailHeader
import com.swyp.firsttodo.presentation.habit.component.HabitDetailTopBar
import com.swyp.firsttodo.presentation.habit.extension.displayName
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar
import kotlinx.collections.immutable.toImmutableList

private val habitDurations = HabitDuration.entries.toImmutableList()

@Composable
fun HabitDetailRoute(
    popBackStack: (resultMessage: String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHostState.current
    val isBtnEnabled by viewModel.isBtnEnabled.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler {
        viewModel.onBackClick()
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is HabitDetailSideEffect.PopBackStack -> {
                keyboardController?.hide()
                popBackStack(effect.resultMessage)
            }

            is HabitDetailSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
        }
    }

    HabitDetailScreen(
        uiState = uiState,
        screenType = viewModel.screenType,
        btnEnabled = isBtnEnabled,
        titleFieldState = viewModel.titleState,
        rewardFieldState = viewModel.rewardState,
        onPopBackStack = viewModel::onBackClick,
        onBtnClick = viewModel::onBtnClick,
        onDurationClick = viewModel::onDurationClick,
        modifier = modifier,
    )
}

@Composable
fun HabitDetailScreen(
    uiState: HabitDetailUiState,
    screenType: HabitDetailScreenType,
    btnEnabled: Boolean,
    titleFieldState: TextFieldState,
    rewardFieldState: TextFieldState,
    onPopBackStack: () -> Unit,
    onBtnClick: () -> Unit,
    onDurationClick: (HabitDuration) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier,
        topBar = {
            HabitDetailTopBar(
                onBackClick = onPopBackStack,
            )
        },
        bottomBar = {
            HaebomLargeButton(
                text = uiState.btnText,
                onClick = onBtnClick,
                enabled = btnEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .background(HaebomTheme.colors.white)
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 20.dp),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            HabitDetailHeader(
                title = uiState.title,
                description = uiState.description,
                modifier = Modifier.padding(vertical = 40.dp),
            )

            TaskInputSection(
                title = "습관 작성",
                modifier = Modifier.padding(bottom = 3.dp),
                description = uiState.habitDescription,
            ) {
                HaebomMultiLineTextField(
                    fieldState = titleFieldState,
                    placeholder = "습관을 작성해 주세요.",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (screenType == HabitDetailScreenType.CHILD) ImeAction.Next else ImeAction.Done,
                    ),
                    onKeyboardAction = if (screenType == HabitDetailScreenType.CHILD) {
                        KeyboardActionHandler {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    } else {
                        null
                    },
                    enabled = uiState.titleEnabled,
                )
            }

            TaskInputSection(
                title = "기간 정하기",
                modifier = Modifier.padding(bottom = 28.dp),
                description = "10일에 1번 실패시 경고 알림을 보냅니다.",
            ) {
                TaskCategoryList(
                    categories = habitDurations,
                    selectedCategory = uiState.duration,
                    onCategoryClick = onDurationClick,
                    getDisplayName = { it.displayName },
                )
            }

            if (screenType == HabitDetailScreenType.CHILD) {
                TaskInputSection(
                    title = "보상 정하기",
                    modifier = Modifier.padding(bottom = 28.dp),
                ) {
                    HaebomMultiLineTextField(
                        fieldState = rewardFieldState,
                        placeholder = "받고 싶은 보상을 적어주세요.",
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                    )
                }
            }
        }
    }
}

private class HabitDetailScreenPreviewProvider :
    PreviewParameterProvider<Pair<HabitDetailScreenType, HabitDetailScreenState>> {
    override val values = sequenceOf(
        Pair(HabitDetailScreenType.IDLE, HabitDetailScreenState.IDLE),
        Pair(HabitDetailScreenType.CHILD, HabitDetailScreenState.CREATE),
        Pair(HabitDetailScreenType.PARENT, HabitDetailScreenState.CREATE),
        Pair(HabitDetailScreenType.CHILD, HabitDetailScreenState.EDIT),
        Pair(HabitDetailScreenType.PARENT, HabitDetailScreenState.EDIT),
        Pair(HabitDetailScreenType.CHILD, HabitDetailScreenState.RETRY),
        Pair(HabitDetailScreenType.PARENT, HabitDetailScreenState.RETRY),
    )
}

@Preview
@Composable
private fun HabitDetailScreenPreview(
    @PreviewParameter(HabitDetailScreenPreviewProvider::class) params:
        Pair<HabitDetailScreenType, HabitDetailScreenState>,
) {
    val (screenType, screenState) = params
    HaebomTheme {
        HabitDetailScreen(
            uiState = HabitDetailUiState(screenType = screenType, screenState = screenState),
            screenType = screenType,
            btnEnabled = false,
            titleFieldState = rememberTextFieldState(),
            rewardFieldState = rememberTextFieldState(),
            onPopBackStack = {},
            onBtnClick = {},
            onDurationClick = {},
        )
    }
}
