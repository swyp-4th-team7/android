package com.swyp.firsttodo.presentation.reward.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.swyp.firsttodo.presentation.habit.component.HabitDetailTopBar
import com.swyp.firsttodo.presentation.habit.extension.displayName
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar
import com.swyp.firsttodo.presentation.reward.component.RewardDetailTextBox
import com.swyp.firsttodo.presentation.reward.component.RewardHeader

@Composable
fun RewardDetailRoute(
    popBackStack: (String?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RewardDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHostState.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is RewardDetailSideEffect.PopBackStack -> popBackStack(effect.resultMessage)
            is RewardDetailSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
        }
    }

    RewardDetailScreen(
        uiState = uiState,
        rewardFieldState = viewModel.rewardFieldState,
        onPopBackStack = viewModel::onPopBackStack,
        onBtnClick = viewModel::onBtnClick,
        modifier = modifier,
    )
}

@Composable
fun RewardDetailScreen(
    uiState: RewardDetailUiState,
    rewardFieldState: TextFieldState,
    onPopBackStack: () -> Unit,
    onBtnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

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
                enabled = uiState.isBtnEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 20.dp),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp),
        ) {
            RewardHeader(
                title = uiState.title,
                description = uiState.description,
                modifier = Modifier.padding(bottom = 40.dp),
            )

            TaskInputSection(
                title = "자녀 습관",
                modifier = Modifier.padding(bottom = 28.dp),
            ) {
                RewardDetailTextBox(
                    text = uiState.habit,
                )
            }

            TaskInputSection(
                title = "습관 기간",
                modifier = Modifier.padding(bottom = 28.dp),
                description = "자녀의 습관 기간을 확인하세요.",
            ) {
                TaskCategoryList(
                    categories = HabitDuration.entries,
                    selectedCategory = uiState.duration,
                    onCategoryClick = {},
                    getDisplayName = { it.displayName },
                )
            }

            TaskInputSection(
                title = "보상 정하기",
                modifier = Modifier.padding(bottom = 28.dp),
            ) {
                HaebomMultiLineTextField(
                    fieldState = rewardFieldState,
                    placeholder = uiState.initialReward,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                    ),
                    enabled = uiState.rewardFieldEnabled,
                )
            }
        }
    }
}

private class RewardDetailScreenPreviewProvider : PreviewParameterProvider<RewardDetailUiState> {
    override val values: Sequence<RewardDetailUiState> = sequenceOf(
        RewardDetailUiState(
            screenType = RewardDetailScreenType.ACCEPT,
            habit = "매일 수학 문제 풀기",
            duration = HabitDuration.SEVEN_DAYS,
            initialReward = "치킨 사주세요",
        ),
        RewardDetailUiState(
            screenType = RewardDetailScreenType.DELIVER,
            habit = "매일 수학 문제 풀기 매일 수학 문제 풀기 매일 수학 문제 풀기 매일 수학 문제 풀기",
            duration = HabitDuration.SEVEN_DAYS,
            initialReward = "치킨 사주세요",
        ),
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun RewardDetailScreenPreview(
    @PreviewParameter(RewardDetailScreenPreviewProvider::class) uiState: RewardDetailUiState,
) {
    HaebomTheme {
        RewardDetailScreen(
            uiState = uiState,
            onPopBackStack = {},
            onBtnClick = {},
            rewardFieldState = rememberTextFieldState(),
        )
    }
}
