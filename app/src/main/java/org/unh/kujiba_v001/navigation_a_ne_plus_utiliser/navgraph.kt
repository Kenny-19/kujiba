package org.unh.kujiba_v001.navigation_a_ne_plus_utiliser

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.unh.kujiba_v001.fragment.FavoritesScreen
import org.unh.kujiba_v001.fragment.LogoutScreen
import org.unh.kujiba_v001.fragment.RecipeCardsScreen
import org.unh.kujiba_v001.fragment.SettingsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(navController, startDestination = Screen.Settings.route) {
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen()
        }
        composable(Screen.RecipeCards.route) {
            RecipeCardsScreen()
        }
        composable(Screen.Logout.route) {
            LogoutScreen()
        }
    }
}