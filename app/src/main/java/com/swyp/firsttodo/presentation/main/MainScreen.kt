package com.swyp.firsttodo.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.swyp.firsttodo.core.navigation.Route
import com.swyp.firsttodo.presentation.main.bottombar.MainBottomBar
import com.swyp.firsttodo.presentation.main.navigation.MainNavHost
import com.swyp.firsttodo.presentation.main.navigation.MainNavigator

@Composable
fun MainScreen(
    startDestination: Route,
    navigator: MainNavigator,
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
        MainNavHost(
            navigator = navigator,
            paddingValues = innerPadding,
            startDestination = startDestination,
        )
    }
}
