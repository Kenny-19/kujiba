package org.unh.kujiba_v001.screens

import Recipe
import RecipeCard
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.unh.kujiba_v001.fragment.search.JsonFileHandler


@Composable
fun FavoriteScreen(navController: NavHostController, jsonFileHandler: JsonFileHandler) {
    val favoritesState = remember { mutableStateOf(listOf<Recipe>()) }

    LaunchedEffect(key1 = true) {
        favoritesState.value = jsonFileHandler.loadFavorites()
    }
    Text(
        text = "Vos Favoris",
        style = MaterialTheme.typography.h6,
        modifier = Modifier.padding(bottom = 16.dp)
    )
    // UI pour afficher les recettes favorites
    LazyColumn {
        items(favoritesState.value) { recipe ->
            RecipeCard(
                recipe = recipe,
                favoritesState = favoritesState.value.toMutableList(),
                onClick = {}, // Lambda vide
                jsonFileHandler = jsonFileHandler
            )

        }
    }
}

