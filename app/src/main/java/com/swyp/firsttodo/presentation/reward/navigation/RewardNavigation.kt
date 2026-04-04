package com.swyp.firsttodo.presentation.reward.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.reward.detail.RewardDetailRoute
import com.swyp.firsttodo.presentation.reward.detail.RewardDetailScreenType
import com.swyp.firsttodo.presentation.reward.list.RewardListRoute
import kotlinx.serialization.Serializable

private const val REWARD_DETAIL_RESULT_KEY = "reward_detail_result"

sealed interface RewardRoute : Route {
    @Serializable
    data object Reward : RewardRoute

    @Serializable
    data class RewardDetail(
        val screenType: RewardDetailScreenType,
        val habitId: Long,
        val habit: String,
        val duration: String,
        val reward: String,
    ) : RewardRoute
}

fun NavController.navigateToRewardDetail(
    screenType: RewardDetailScreenType,
    habitId: Long,
    habit: String,
    duration: String,
    reward: String,
) {
    navigate(
        RewardRoute.RewardDetail(
            screenType = screenType,
            habitId = habitId,
            habit = habit,
            duration = duration,
            reward = reward,
        ),
    )
}

fun NavGraphBuilder.rewardNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
    navigateToHabit: () -> Unit,
) {
    composable<RewardRoute.Reward>(
        enterTransition = { fadeIn(tween(160)) },
        exitTransition = { fadeOut(tween(160)) },
        popEnterTransition = { fadeIn(tween(160)) },
        popExitTransition = { fadeOut(tween(160)) },
    ) { backStackEntry ->
        val rewardDetailResult by backStackEntry.savedStateHandle
            .getStateFlow<String?>(REWARD_DETAIL_RESULT_KEY, null)
            .collectAsStateWithLifecycle()

        RewardListRoute(
            navigateToHabit = navigateToHabit,
            navigateToRewardDetail = { screenType, habitId, habit, duration, reward ->
                navController.navigateToRewardDetail(screenType, habitId, habit, duration, reward)
            },
            rewardDetailResult = rewardDetailResult,
            onDetailResultConsumed = {
                backStackEntry.savedStateHandle[REWARD_DETAIL_RESULT_KEY] = null
            },
            modifier = Modifier.padding(paddingValues),
        )
    }

    composable<RewardRoute.RewardDetail> {
        RewardDetailRoute(
            popBackStack = { resultMessage ->
                resultMessage?.let {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        REWARD_DETAIL_RESULT_KEY,
                        it,
                    )
                }
                navController.popBackStack()
            },
        )
    }
}
