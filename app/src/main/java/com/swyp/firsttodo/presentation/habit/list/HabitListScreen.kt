package com.swyp.firsttodo.presentation.habit.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.habit.Habit
import com.swyp.firsttodo.domain.model.habit.HabitDuration
import com.swyp.firsttodo.presentation.common.component.DeleteDialogType
import com.swyp.firsttodo.presentation.common.component.HaebomDeleteDialog
import com.swyp.firsttodo.presentation.common.component.TopBarArea
import com.swyp.firsttodo.presentation.habit.component.HabitList
import com.swyp.firsttodo.presentation.habit.component.HabitListEmpty
import com.swyp.firsttodo.presentation.habit.component.HabitListType
import com.swyp.firsttodo.presentation.habit.component.HabitMainBanner
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar

@Composable
fun HabitListRoute(
    navigateToHabitDetail: (Habit?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HabitListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHostState.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is HabitListSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)

            is HabitListSideEffect.NavigateToHabitDetail -> navigateToHabitDetail(effect.habit)
        }
    }

    if (uiState.showDeleteDialog) {
        HaebomDeleteDialog(
            dialogType = DeleteDialogType.HABIT,
            onConfirm = viewModel::onDeleteConfirm,
            onCancel = viewModel::onDeleteCancel,
            onDismiss = viewModel::onDeleteCancel,
        )
    }

    HabitListScreen(
        uiState = uiState,
        habitListType = viewModel.listType,
        onCreateClick = viewModel::onCreateClick,
        onCheckClick = viewModel::onCheckClick,
        onEditClick = viewModel::onEditClick,
        onDeleteClick = viewModel::onDeleteClick,
        modifier = modifier,
    )
}

@Composable
fun HabitListScreen(
    uiState: HabitListUiState,
    habitListType: HabitListType,
    onCreateClick: () -> Unit,
    onCheckClick: (Habit) -> Unit,
    onEditClick: (Habit) -> Unit,
    onDeleteClick: (Habit) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        TopBarArea()

        HabitMainBanner(
            onButtonClick = onCreateClick,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        when {
            uiState.habits is Async.Empty -> HabitListEmpty(
                modifier = Modifier
                    .padding(
                        horizontal = screenWidthDp(16.dp),
                    ),
            )

            uiState.habitsData != null -> HabitList(
                habitListType = habitListType,
                onCheckClick = onCheckClick,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick,
                habits = uiState.habitsData,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(16.dp)),
            )

            else -> Unit
        }
    }
}

private class HabitListScreenPreviewProvider : PreviewParameterProvider<HabitListUiState> {
    private val sampleHabits = HabitDuration.entries.mapIndexed { index, duration ->
        Habit(
            habitId = index.toLong(),
            duration = duration,
            isCompleted = index % 2 == 0,
            title = if (index % 2 == 0) "하루에 책 10장 읽기" else "하루에 책 10장 읽기 하루에 책 10장 읽기",
            reward = if (index % 2 == 0) "가족이랑 놀이공원 가기" else "가족이랑 놀이공원 가기 가족이랑 놀이공원 가기",
        )
    }

    override val values = sequenceOf(
        HabitListUiState(habits = Async.Success(sampleHabits)),
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
            habitListType = HabitListType.CHILD,
            onCreateClick = {},
            onCheckClick = {},
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
