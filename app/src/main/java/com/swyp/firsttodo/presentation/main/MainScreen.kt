package com.swyp.firsttodo.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        MainNavHost(
            navController = navController,
            paddingValues = innerPadding,
        )
    }
}
