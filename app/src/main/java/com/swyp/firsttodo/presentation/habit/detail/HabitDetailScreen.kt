package com.swyp.firsttodo.presentation.habit.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.common.extension.toast
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.presentation.common.component.HaebomLargeButton
import com.swyp.firsttodo.presentation.common.component.task.TaskCategoryList
import com.swyp.firsttodo.presentation.common.component.task.TaskInputSection
import com.swyp.firsttodo.presentation.common.component.task.TaskTextField
import com.swyp.firsttodo.presentation.habit.component.HabitDetailHeader
import com.swyp.firsttodo.presentation.habit.component.HabitDetailTopBar
import com.swyp.firsttodo.presentation.habit.extension.displayName

@Composable
fun HabitDetailRoute(
    popBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isBtnEnabled by viewModel.isBtnEnabled.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is HabitDetailSideEffect.PopBackStack -> popBackStack()
            is HabitDetailSideEffect.ShowToast -> context.toast(effect.message)
        }
    }

    HabitDetailScreen(
        uiState = uiState,
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
    btnEnabled: Boolean,
    titleFieldState: TextFieldState,
    rewardFieldState: TextFieldState,
    onPopBackStack: () -> Unit,
    onBtnClick: () -> Unit,
    onDurationClick: (HabitDuration) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                    .padding(horizontal = screenWidthDp(16.dp))
                    .padding(vertical = screenHeightDp(20.dp)),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = screenWidthDp(16.dp)),
        ) {
            HabitDetailHeader(
                title = uiState.title,
                description = uiState.description,
                modifier = Modifier.padding(vertical = 40.dp),
            )

            TaskInputSection(
                title = "습관 작성",
                modifier = Modifier.padding(bottom = 28.dp),
            ) {
                TaskTextField(
                    fieldState = titleFieldState,
                    placeholder = "습관을 작성해주세요. (최대 12자)",
                    modifier = Modifier.fillMaxWidth(),
                    maxCount = 12,
                )
            }

            TaskInputSection(
                title = "기간 정하기",
                modifier = Modifier.padding(bottom = 28.dp),
                description = "10일에 1번 실패시 경고 알림을 보냅니다.",
            ) {
                TaskCategoryList(
                    categories = HabitDuration.entries,
                    selectedCategory = uiState.duration,
                    onCategoryClick = onDurationClick,
                    getDisplayName = { it.displayName },
                )
            }

            TaskInputSection(
                title = "보상 정하기",
                modifier = Modifier.padding(bottom = 28.dp),
            ) {
                TaskTextField(
                    fieldState = rewardFieldState,
                    placeholder = "",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun HabitDetailScreenPreview() {
    HaebomTheme {
        HabitDetailScreen(
            uiState = HabitDetailUiState(),
            btnEnabled = false,
            titleFieldState = rememberTextFieldState(),
            rewardFieldState = rememberTextFieldState(),
            onPopBackStack = {},
            onBtnClick = {},
            onDurationClick = {},
        )
    }
}
