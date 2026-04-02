package com.swyp.firsttodo.presentation.growth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.base.Async
import com.swyp.firsttodo.core.common.extension.getDataOrNull
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.designsystem.theme.HaebomTheme
import com.swyp.firsttodo.domain.model.Role
import com.swyp.firsttodo.domain.model.growth.ChildGrowthModel
import com.swyp.firsttodo.domain.model.growth.GrowthModel
import com.swyp.firsttodo.presentation.common.component.HaebomHeaderTab
import com.swyp.firsttodo.presentation.common.component.TopBarArea
import com.swyp.firsttodo.presentation.growth.component.CharacterBubble
import com.swyp.firsttodo.presentation.growth.component.ChildrenNicknameChipList
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.snackbar.showHaebomSnackbar

@Composable
fun GrowthRoute(
    modifier: Modifier = Modifier,
    viewModel: GrowthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHost = LocalSnackbarHostState.current

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            is GrowthSideEffect.ShowSnackbar -> snackbarHost.showHaebomSnackbar(effect.message)
        }
    }

    GrowthScreen(
        uiState = uiState,
        onTabClick = viewModel::onTabClick,
        onChildChipClick = viewModel::onChildChipClick,
        modifier = modifier,
    )
}

@Composable
fun GrowthScreen(
    uiState: GrowthUiState,
    onTabClick: (GrowthHeaderTabType) -> Unit,
    onChildChipClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopBarArea()

        HaebomHeaderTab(
            currentTab = uiState.currentTab,
            tabs = GrowthHeaderTabType.entries,
            onTabClick = onTabClick,
        )

        uiState.childrenGrowth.getDataOrNull()?.let {
            ChildrenNicknameChipList(
                childrenGrowth = it,
                selectedChildIdx = uiState.selectedChildIdx,
                onChildChipClick = onChildChipClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 12.dp),
            )
        } ?: Spacer(Modifier.weight(74f))

        uiState.currentStarCount?.let { count ->
            uiState.bubbleImageRes?.let { imageRes ->
                CharacterBubble(
                    starCount = count,
                    bubbleImageRes = imageRes,
                    text = uiState.bubbleText,
                    modifier = Modifier.height(157.dp),
                )
            }
        }

        uiState.characterImageRes?.let {
            Image(
                painter = painterResource(it),
                contentDescription = null,
                modifier = Modifier.weight(154f),
            )
        }

        Spacer(Modifier.weight(8f))

        Text(
            text = uiState.description,
            color = uiState.growthTextType.color(),
            textAlign = TextAlign.Center,
            style = HaebomTheme.typo.section,
        )

        Spacer(Modifier.weight(16f))

        uiState.weekRange?.let {
            Text(
                text = it,
                color = Color(0xFF929292),
                style = HaebomTheme.typo.section,
            )

            Spacer(Modifier.weight(41f))
        } ?: Spacer(Modifier.weight(76f))
    }
}

private const val PREVIEW_WEEK_RANGE = "2025.03.24 ~ 2025.03.30"

private fun previewGrowthModel(starCount: Int) = GrowthModel(starCount = starCount, weekRange = PREVIEW_WEEK_RANGE)

private fun previewChildModel(
    id: Long,
    nickname: String,
    todoStarCount: Int = 0,
    habitStarCount: Int = 0,
) = ChildGrowthModel(
    childId = id,
    nickname = nickname,
    todoStarCount = todoStarCount,
    habitStarCount = habitStarCount,
    weekRange = PREVIEW_WEEK_RANGE,
)

private val previewThreeChildren = listOf(
    previewChildModel(1, "이하나", todoStarCount = 1, habitStarCount = 3),
    previewChildModel(2, "김현우", todoStarCount = 2, habitStarCount = 0),
    previewChildModel(3, "박서연이야", todoStarCount = 3, habitStarCount = 2),
)

private val previewTenChildren = listOf(
    previewChildModel(1, "나", todoStarCount = 0, habitStarCount = 0),
    previewChildModel(2, "김민준", todoStarCount = 1, habitStarCount = 2),
    previewChildModel(3, "이서연", todoStarCount = 2, habitStarCount = 1),
    previewChildModel(4, "박지우", todoStarCount = 3, habitStarCount = 3),
    previewChildModel(5, "최예은이야", todoStarCount = 0, habitStarCount = 1),
    previewChildModel(6, "정하린", todoStarCount = 1, habitStarCount = 0),
    previewChildModel(7, "강도윤", todoStarCount = 2, habitStarCount = 3),
    previewChildModel(8, "윤서하", todoStarCount = 3, habitStarCount = 2),
    previewChildModel(9, "장민서입니다", todoStarCount = 0, habitStarCount = 0),
    previewChildModel(10, "열두글자닉네임입니다", todoStarCount = 1, habitStarCount = 1),
)

private class GrowthScreenPreviewProvider : PreviewParameterProvider<GrowthUiState> {
    override val values: Sequence<GrowthUiState> = sequenceOf(
        // Child / TODO — 0·1·2·3
        GrowthUiState(
            role = Role.CHILD,
            currentTab = GrowthHeaderTabType.TODO,
            todoGrowth = Async.Success(previewGrowthModel(0)),
        ),
        GrowthUiState(
            role = Role.CHILD,
            currentTab = GrowthHeaderTabType.TODO,
            todoGrowth = Async.Success(previewGrowthModel(1)),
        ),
        GrowthUiState(
            role = Role.CHILD,
            currentTab = GrowthHeaderTabType.TODO,
            todoGrowth = Async.Success(previewGrowthModel(2)),
        ),
        GrowthUiState(
            role = Role.CHILD,
            currentTab = GrowthHeaderTabType.TODO,
            todoGrowth = Async.Success(previewGrowthModel(3)),
        ),
        // Child / HABIT — 0·3
        GrowthUiState(
            role = Role.CHILD,
            currentTab = GrowthHeaderTabType.HABIT,
            habitGrowth = Async.Success(previewGrowthModel(0)),
        ),
        GrowthUiState(
            role = Role.CHILD,
            currentTab = GrowthHeaderTabType.HABIT,
            habitGrowth = Async.Success(previewGrowthModel(3)),
        ),
        // Parent / TODO — starCount 0·1·3
        GrowthUiState(
            role = Role.PARENT,
            currentTab = GrowthHeaderTabType.TODO,
            childrenGrowth = Async.Success(previewThreeChildren.map { it.copy(todoStarCount = 0) }),
            selectedChildIdx = 0,
        ),
        GrowthUiState(
            role = Role.PARENT,
            currentTab = GrowthHeaderTabType.TODO,
            childrenGrowth = Async.Success(previewThreeChildren),
            selectedChildIdx = 0,
        ),
        GrowthUiState(
            role = Role.PARENT,
            currentTab = GrowthHeaderTabType.TODO,
            childrenGrowth = Async.Success(previewThreeChildren),
            selectedChildIdx = 2,
        ),
        // Parent / HABIT
        GrowthUiState(
            role = Role.PARENT,
            currentTab = GrowthHeaderTabType.HABIT,
            childrenGrowth = Async.Success(previewThreeChildren),
            selectedChildIdx = 0,
        ),
        // Parent / 자녀 10명
        GrowthUiState(
            role = Role.PARENT,
            currentTab = GrowthHeaderTabType.TODO,
            childrenGrowth = Async.Success(previewTenChildren),
            selectedChildIdx = 3,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun GrowthScreenPreview(
    @PreviewParameter(GrowthScreenPreviewProvider::class) uiState: GrowthUiState,
) {
    HaebomTheme {
        GrowthScreen(
            uiState = uiState,
            onTabClick = {},
            onChildChipClick = {},
        )
    }
}
