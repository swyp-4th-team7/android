package com.swyp.firsttodo.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp.firsttodo.core.auth.manager.AuthSideEffect
import com.swyp.firsttodo.core.common.extension.toast
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.auth.navigation.navigateToLogin
import com.swyp.firsttodo.presentation.main.bottombar.MainBottomBar
import com.swyp.firsttodo.presentation.main.drawer.MainDrawer
import com.swyp.firsttodo.presentation.main.navigation.MainNavHost
import com.swyp.firsttodo.presentation.main.navigation.MainNavigator
import com.swyp.firsttodo.presentation.main.topbar.MainTopBar

@Composable
fun MainScreen(
    startDestination: Route,
    navigator: MainNavigator,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val showDrawer by viewModel.showDrawer.collectAsStateWithLifecycle()
    val context = LocalContext.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            AuthSideEffect.NavigateToLogin -> {
                context.toast("로그인이 만료되었어요. 다시 로그인 해주세요.")
                navigator.navController.navigateToLogin(isSessionExpired = true)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainTopBar(
                visible = navigator.shouldShowBottomBar(),
                onMenuClick = viewModel::onMenuClick,
                onAlarmClick = viewModel::onAlarmClick,
            )
        },
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

            MainDrawer(
                visible = showDrawer,
                onDismiss = viewModel::onDrawerDismiss,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
