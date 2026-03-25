package com.swyp.firsttodo.presentation.hamburger.family

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.family.FamilyHabit
import com.swyp.firsttodo.domain.model.family.FamilyInfo
import com.swyp.firsttodo.domain.model.family.FamilyTodo
import com.swyp.firsttodo.presentation.common.component.HaebomTopBar
import com.swyp.firsttodo.presentation.hamburger.family.component.FamilyDashBoard
import com.swyp.firsttodo.presentation.hamburger.family.component.FamilyDashBoardEmpty
import com.swyp.firsttodo.presentation.hamburger.family.component.FamilyHeader
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar

@Composable
fun FamilyRoute(
    modifier: Modifier = Modifier,
    popBackStack: () -> Unit,
    viewModel: FamilyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHostState.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is FamilySideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
        }
    }

    FamilyScreen(
        uiState = uiState,
        onPopBackStack = popBackStack,
        modifier = modifier,
    )
}

@Composable
fun FamilyScreen(
    uiState: FamilyUiState,
    onPopBackStack: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 40.dp, bottom = 22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            FamilyHeader()

            if (uiState.familyInfos is Async.Empty) {
                FamilyDashBoardEmpty(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            } else {
                uiState.familyData?.let { data ->
                    FamilyDashBoard(
                        familyInfos = data,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FamilyScreenPreview() {
    HaebomTheme {
        FamilyScreen(
            uiState = FamilyUiState(
                familyInfos = Async.Success(
                    listOf(
                        FamilyInfo(1L, "엄마는외계인", FamilyTodo(10, 3), FamilyHabit(completed = true)),
                        FamilyInfo(2L, "박영희영희영희영희영희", FamilyTodo(10, 10), FamilyHabit(completed = false)),
                        FamilyInfo(3L, "이민준", FamilyTodo(5, 0), FamilyHabit(completed = true)),
                    ),
                ),
            ),
            onPopBackStack = {},
        )
    }
}
