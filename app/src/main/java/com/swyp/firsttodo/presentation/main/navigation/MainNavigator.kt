package com.swyp.firsttodo.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.swyp.firsttodo.presentation.main.bottombar.MainTab

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTab: MainTab?
        @Composable get() = MainTab.entries.find { tab ->
            currentDestination?.route == tab.route::class.qualifiedName
        }

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            popUpTo(MainTab.TODO.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        navController.navigate(tab.route, navOptions)
    }

    fun navigateToTodo() {
        navController.navigate(MainTab.TODO.route) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    @Composable
    fun shouldShowBottomBar() = currentTab != null
}

@Composable
fun rememberHeabomNavigator(navController: NavHostController = rememberNavController()): MainNavigator =
    remember(navController) {
        MainNavigator(navController)
    }
