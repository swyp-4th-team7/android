package com.swyp.firsttodo.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.swyp.firsttodo.presentation.auth.navigation.AuthRoute
import com.swyp.firsttodo.presentation.auth.navigation.authNavGraph

@Composable
fun MainNavHost(
    navController: NavHostController,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoute.Login,
        modifier = modifier.fillMaxSize(),
    ) {
        authNavGraph(
            navController = navController,
        )
    }
}
