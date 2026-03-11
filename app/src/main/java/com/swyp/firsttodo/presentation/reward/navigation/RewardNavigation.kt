package com.swyp.firsttodo.presentation.reward.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.presentation.reward.RewardRoute
import kotlinx.serialization.Serializable

sealed interface RewardRoute {
    @Serializable
    data object Reward : RewardRoute
}

fun NavGraphBuilder.rewardNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<RewardRoute.Reward> {
        RewardRoute(
            modifier = Modifier.padding(paddingValues),
        )
    }
}
