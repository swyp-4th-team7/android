package com.swyp.firsttodo.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.swyp.firsttodo.core.auth.manager.AuthSideEffect
import com.swyp.firsttodo.core.common.extension.toast
import com.swyp.firsttodo.core.common.util.HandleSideEffects
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.auth.navigation.navigateToLogin
import com.swyp.firsttodo.presentation.main.bottombar.MainBottomBar
import com.swyp.firsttodo.presentation.main.navigation.MainNavHost
import com.swyp.firsttodo.presentation.main.navigation.MainNavigator

@Composable
fun MainScreen(
    startDestination: Route,
    navigator: MainNavigator,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    HandleSideEffects(viewModel.sideEffect) { effect ->
        when (effect) {
            AuthSideEffect.NavigateToLogin -> {
                context.toast("로그인이 만료되었어요. 다시 로그인 해주세요.")
                navigator.navController.navigateToLogin()
            }
        }
    }

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
        MainNavHost(
            navigator = navigator,
            paddingValues = innerPadding,
            startDestination = startDestination,
        )
    }
}
