package com.swyp.firsttodo.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.auth.manager.AuthSideEffect
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.common.util.screenHeightDp
import com.swyp.firsttodo.core.common.util.screenWidthDp
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.auth.navigation.navigateToLogin
import com.swyp.firsttodo.presentation.hamburger.navigation.navigateToFamily
import com.swyp.firsttodo.presentation.hamburger.navigation.navigateToShare
import com.swyp.firsttodo.presentation.main.bottombar.MainBottomBar
import com.swyp.firsttodo.presentation.main.drawer.MainDrawer
import com.swyp.firsttodo.presentation.main.navigation.MainNavHost
import com.swyp.firsttodo.presentation.main.navigation.MainNavigator
import com.swyp.firsttodo.presentation.main.snackbar.HaebomSnackbarHost
import com.swyp.firsttodo.presentation.main.snackbar.LocalSnackbarHostState
import com.swyp.firsttodo.presentation.main.topbar.MainTopBar

@Composable
fun MainScreen(
    startDestination: Route,
    navigator: MainNavigator,
    snackbarState: SnackbarHostState,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val showDrawer by viewModel.showDrawer.collectAsStateWithLifecycle()

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            AuthSideEffect.ForceNavigateToLogin -> navigator.navController.navigateToLogin(isSessionExpired = true)

            AuthSideEffect.NavigateToLogin -> navigator.navController.navigateToLogin(isSessionExpired = false)
        }
    }

    CompositionLocalProvider(LocalSnackbarHostState provides snackbarState) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    MainBottomBar(
                        visible = navigator.shouldShowBottomBar(),
                        currentTab = navigator.currentTab,
                        onTabClick = { navigator.navigate(it) },
                    )
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MainNavHost(
                        navigator = navigator,
                        paddingValues = innerPadding,
                        startDestination = startDestination,
                    )

                    MainTopBar(
                        visible = navigator.shouldShowBottomBar(),
                        onMenuClick = viewModel::onMenuClick,
                    )

                    MainDrawer(
                        visible = showDrawer,
                        onDismiss = viewModel::onDrawerDismiss,
                        onNavigateToFamily = navigator.navController::navigateToFamily,
                        onNavigateToShare = navigator.navController::navigateToShare,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }

            HaebomSnackbarHost(
                hostState = snackbarState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = screenHeightDp(40.dp))
                    .padding(horizontal = screenWidthDp(16.dp)),
            )
        }
    }
}
