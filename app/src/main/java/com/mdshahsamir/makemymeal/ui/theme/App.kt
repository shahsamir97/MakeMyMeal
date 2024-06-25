package com.mdshahsamir.makemymeal.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mdshahsamir.makemymeal.navigation.NavigationScreen
import com.mdshahsamir.makemymeal.ui.createrecipe.CreateRecipeScreen
import com.mdshahsamir.makemymeal.ui.dashboard.DashboardScreen

@Composable
fun MakeMyMealApp(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = NavigationScreen.Dashboard.route
    ) {
        composable(NavigationScreen.Dashboard.route) {
            DashboardScreen(navHostController)
        }
        composable(NavigationScreen.CreateRecipe.route) {
           CreateRecipeScreen()
        }
        composable(NavigationScreen.MakeMeal.route) {

        }
    }
}

