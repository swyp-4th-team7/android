package com.swyp.firsttodo.presentation.hamburger.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.hamburger.family.FamilyRoute
import com.swyp.firsttodo.presentation.hamburger.share.ShareRoute
import kotlinx.serialization.Serializable

sealed interface HamburgerRoute : Route {
    @Serializable
    data object Family : HamburgerRoute

    @Serializable
    data object Share : HamburgerRoute
}

fun NavController.navigateToFamily() = navigate(HamburgerRoute.Family)

fun NavController.navigateToShare() = navigate(HamburgerRoute.Share)

fun NavGraphBuilder.hamburgerNavGraph(
    paddingValues: PaddingValues,
    navigateToOnboarding: () -> Unit,
    navController: NavController,
) {
    composable<HamburgerRoute.Family> {
        FamilyRoute(
            popBackStack = navController::popBackStack,
            modifier = Modifier.padding(paddingValues),
        )
    }

    composable<HamburgerRoute.Share> {
        ShareRoute(
            popBackStack = navController::popBackStack,
            navigateToOnboarding = navigateToOnboarding,
            modifier = Modifier.padding(paddingValues),
        )
    }
}
