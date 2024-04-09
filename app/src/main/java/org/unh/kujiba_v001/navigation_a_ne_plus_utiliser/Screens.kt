package org.unh.kujiba_v001.navigation_a_ne_plus_utiliser

sealed class Screen(val route: String, val title: String) {
     object Favorites : Screen("favorites", "Recettes Favorites")
     object RecipeCards : Screen("recipe_cards", "Fiches de recettes")
     object Settings : Screen("settings", "Paramètres")
     object Logout : Screen("logout", "Déconnexion")
}