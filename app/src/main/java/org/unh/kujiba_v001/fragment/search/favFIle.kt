package org.unh.kujiba_v001.fragment.search

import Recipe
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class JsonFileHandler(private val context: Context) {
    private val gson = Gson()
    private fun getFavoritesFile() = File(context.filesDir, "favorites.json")

    fun saveFavorites(favorites: List<Recipe>) {
        val jsonString = gson.toJson(favorites)
        getFavoritesFile().writeText(jsonString)
    }

    fun loadFavorites(): MutableList<Recipe> {
        val file = getFavoritesFile()
        if (!file.exists()) return mutableListOf()
        val jsonString = file.readText()
        val type = object : TypeToken<List<Recipe>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    fun updateFavorites(newRecipe: Recipe) {
        val currentFavorites = loadFavorites().toMutableList()
        if (!currentFavorites.any { it.name == newRecipe.name && it.source == newRecipe.source }) {
            currentFavorites.add(newRecipe)
            saveFavorites(currentFavorites)
        }
    }
    fun countFavorites(): Int {
        val file = getFavoritesFile()
        if (!file.exists()) return 0
        val jsonString = file.readText()
        val type = object : TypeToken<List<Recipe>>() {}.type
        val recipes: List<Recipe> = gson.fromJson(jsonString, type)
        return recipes.size
    }
}

