package com.swyp.firsttodo.presentation.growth.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.presentation.growth.GrowthRoute
import kotlinx.serialization.Serializable

sealed interface GrowthRoute {
    @Serializable
    data object Growth : GrowthRoute
}

fun NavGraphBuilder.growthNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<GrowthRoute.Growth> {
        GrowthRoute(
            modifier = Modifier.padding(paddingValues),
        )
    }
}
