package com.swyp.firsttodo.presentation.habit.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.component.HaebomDeleteDialog
import com.swyp.firsttodo.core.common.component.TopBarArea
import com.swyp.firsttodo.core.common.type.DeleteDialogType
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.domain.model.habit.HabitModel
import com.swyp.firsttodo.presentation.habit.component.HabitList
import com.swyp.firsttodo.presentation.habit.component.HabitListEmpty
import com.swyp.firsttodo.presentation.habit.component.HabitMainBanner
import com.swyp.firsttodo.presentation.habit.component.HabitRetryList
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar

@Composable
fun HabitListRoute(
    navigateToHabitDetail: (HabitModel?) -> Unit,
    navigateToHabitRetry: (HabitModel) -> Unit,
    habitDetailResult: String?,
    onDetailResultConsumed: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHostState.current

    LaunchedEffect(habitDetailResult) {
        viewModel.onDetailResult(habitDetailResult)
        onDetailResultConsumed()
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is HabitListSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
            is HabitListSideEffect.NavigateToHabitDetail -> navigateToHabitDetail(effect.habit)
            is HabitListSideEffect.NavigateToHabitRetry -> navigateToHabitRetry(effect.habit)
        }
    }

    if (uiState.showDeleteDialog) {
        HaebomDeleteDialog(
            dialogType = if (uiState.isFailedHabitDelete) DeleteDialogType.FailedHabit else DeleteDialogType.Habit,
            onConfirm = viewModel::onDeleteConfirm,
            onCancel = viewModel::onDeleteCancel,
            onDismiss = viewModel::onDeleteCancel,
            isLoading = uiState.isDialogLoading,
        )
    }

    HabitListScreen(
        uiState = uiState,
        onCreateClick = viewModel::onCreateClick,
        onCheckClick = viewModel::onCheckClick,
        onEditClick = viewModel::onEditClick,
        onDeleteClick = viewModel::onDeleteClick,
        onFailedHabitDeleteClick = viewModel::onFailedHabitDeleteClick,
        onRetryClick = viewModel::onRetryClick,
        modifier = modifier,
    )
}

@Composable
fun HabitListScreen(
    uiState: HabitListUiState,
    onCreateClick: () -> Unit,
    onCheckClick: (HabitModel) -> Unit,
    onEditClick: (HabitModel) -> Unit,
    onDeleteClick: (HabitModel) -> Unit,
    onFailedHabitDeleteClick: (HabitModel) -> Unit,
    onRetryClick: (HabitModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
    ) {
        TopBarArea()

        HabitMainBanner(
            description = uiState.description,
            onButtonClick = onCreateClick,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        when {
            uiState.habits is Async.Empty -> HabitListEmpty(
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            uiState.habitsData != null -> HabitList(
                habitListType = uiState.listType,
                onCheckClick = onCheckClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                habits = uiState.habitsData,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )

            else -> Unit
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            thickness = 0.8.dp,
            color = HaebomTheme.colors.gray50,
        )

        HabitRetryList(
            habits = uiState.failedHabits,
            onRetry = onRetryClick,
            onDelete = onFailedHabitDeleteClick,
            scrollState = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(80.dp))
    }
}

private class HabitListScreenPreviewProvider : PreviewParameterProvider<HabitListUiState> {
    private val sampleHabits = HabitDuration.entries.mapIndexed { index, duration ->
        HabitModel(
            habitId = index.toLong(),
            duration = duration,
            isCompleted = index % 2 == 0,
            title = if (index % 2 == 0) "하루에 책 10장 읽기" else "하루에 책 10장 읽기 하루에 책 10장 읽기",
            reward = if (index % 2 == 0) "가족이랑 놀이공원 가기" else "가족이랑 놀이공원 가기 가족이랑 놀이공원 가기",
        )
    }

    override val values = sequenceOf(
        HabitListUiState(habits = Async.Success(sampleHabits), failedHabits = Async.Empty),
        HabitListUiState(habits = Async.Success(emptyList())),
    )
}

@Preview(showBackground = true)
@Composable
private fun HabitListScreenPreview(
    @PreviewParameter(HabitListScreenPreviewProvider::class) uiState: HabitListUiState,
) {
    HaebomTheme {
        HabitListScreen(
            uiState = uiState,
            onCreateClick = {},
            onCheckClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onFailedHabitDeleteClick = {},
            onRetryClick = {},
        )
    }
}
