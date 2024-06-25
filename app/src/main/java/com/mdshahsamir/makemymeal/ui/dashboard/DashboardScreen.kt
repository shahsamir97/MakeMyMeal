package com.mdshahsamir.makemymeal.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mdshahsamir.makemymeal.navigation.NavigationScreen

@Composable
internal fun DashboardScreen(navController: NavController) {
    Scaffold { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Button(onClick = { navController.navigate(NavigationScreen.CreateRecipe.route) }) {
                Text(text = "Create Recipe")
            }
            Button(onClick = { /*TODO*/ }) {

            }
        }
    }
}

