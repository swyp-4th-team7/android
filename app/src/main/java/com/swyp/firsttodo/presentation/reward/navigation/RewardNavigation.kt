package com.swyp.firsttodo.presentation.reward.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.reward.detail.RewardDetailRoute
import com.swyp.firsttodo.presentation.reward.detail.RewardDetailScreenType
import com.swyp.firsttodo.presentation.reward.list.RewardListRoute
import kotlinx.serialization.Serializable

sealed interface RewardRoute : Route {
    @Serializable
    data object Reward : RewardRoute

    @Serializable
    data class RewardDetail(val screenType: RewardDetailScreenType) : RewardRoute
}

fun NavController.navigateToRewardDetail(screenType: RewardDetailScreenType) {
    navigate(RewardRoute.RewardDetail(screenType))
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
    ) {
        RewardListRoute(
            navigateToHabit = navigateToHabit,
            navigateToRewardDetail = navController::navigateToRewardDetail,
            modifier = Modifier.padding(paddingValues),
        )
    }

    composable<RewardRoute.RewardDetail> {
        RewardDetailRoute(
            popBackStack = navController::popBackStack,
        )
    }
}
