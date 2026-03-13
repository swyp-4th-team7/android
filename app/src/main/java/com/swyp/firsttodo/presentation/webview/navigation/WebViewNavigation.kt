package com.swyp.firsttodo.presentation.webview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.webview.WebViewScreen
import kotlinx.serialization.Serializable

sealed interface WebViewRoute : Route {
    @Serializable
    data class WebView(val title: String, val url: String) : WebViewRoute
}

fun NavController.navigateToWebView(
    title: String,
    url: String,
) {
    navigate(WebViewRoute.WebView(title, url))
}

fun NavGraphBuilder.webViewNavGraph(
    paddingValues: PaddingValues,
    navController: NavController,
) {
    composable<WebViewRoute.WebView> { backStackEntry ->
        val route = backStackEntry.toRoute<WebViewRoute.WebView>()
        WebViewScreen(
            title = route.title,
            url = route.url,
            onPopBackStack = navController::popBackStack,
            modifier = Modifier.padding(paddingValues),
        )
    }
}
