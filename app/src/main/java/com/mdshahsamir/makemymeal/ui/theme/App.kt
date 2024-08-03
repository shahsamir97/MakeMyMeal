package com.mdshahsamir.makemymeal.ui.theme

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mdshahsamir.makemymeal.navigation.NavigationScreen
import com.mdshahsamir.makemymeal.ui.createrecipe.CreateRecipeScreen
import com.mdshahsamir.makemymeal.ui.dashboard.DashboardScreen
import com.mdshahsamir.makemymeal.ui.makemeal.MakeMealScreen

@Composable
fun MakeMyMealApp(navHostController: NavHostController) {
    NavHost(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(800)
            )
        },
        exitTransition =  {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(800)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(800)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(800)
            )
        },
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
            MakeMealScreen()
        }
    }
}

