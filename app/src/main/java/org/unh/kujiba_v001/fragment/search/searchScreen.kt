
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import org.unh.kujiba_v001.fragment.search.JsonFileHandler
import org.unh.kujiba_v001.fragment.search.SearchState
import java.io.IOException

@Composable
fun SearchScreen(navController: NavHostController, jsonFileHandler: JsonFileHandler, searchState: SearchState) {
    val baseUrl = "https://api.edamam.com/api/recipes/v2"
    val appId = "8101fbeb"
    val appKey = "3057c99cf2f9b7d1411b0da32d221199"
    val searchState = remember { SearchState() }
    val queryState = remember { mutableStateOf("") }
    val recipeType = "public"


    val recipesState = remember { mutableStateListOf<Recipe>() }
    val favoritesState = remember { mutableStateListOf<Recipe>() }

    val client = remember { OkHttpClient() }

// État pour le suivi de la recette sélectionnée
    val selectedRecipe = remember { mutableStateOf<Recipe?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Search for Recipes",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = searchState.queryState.value,
            onValueChange = { searchState.queryState.value = it },
            label = { Text("Search query") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                performRecipeSearch(
                    searchState.queryState.value,
                    recipeType,
                    appId,
                    appKey,
                    client,
                    recipesState
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Search")
        }

        Text(
            text = "Recettes trouvées :",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(recipesState) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    favoritesState = favoritesState,
                    onClick = {
                        selectedRecipe.value = recipe
                    },
                    jsonFileHandler = jsonFileHandler  // Passez l'instance ici
                )
            }
        }
    }

    selectedRecipe.value?.let { recipe ->
        Dialog(
            onDismissRequest = { selectedRecipe.value = null },
            content = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    color = Color.White // White background for dialog
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = recipe.name,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Image(
                            painter = rememberImagePainter(data = recipe.imageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .padding(10.dp)
                                .clip(shape = RoundedCornerShape(15.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Ingredients:",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        LazyColumn(modifier = Modifier.weight(1f)) { // Enable scrolling
                            items(recipe.ingredients) { ingredient ->
                                Text(
                                    text = "- $ingredient",
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                        }
                        Text(
                            text = "Additional Data:",
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        LazyColumn(modifier = Modifier.weight(1f)) { // Enable scrolling
                            item {
                                Text(
                                    text = "Source: ${recipe.source ?: ""}",
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "Calories: ${recipe.calories ?: ""} kcal",
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "Yield: ${recipe.yield}",
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "Diet Labels: ${recipe.dietLabels.joinToString()}",
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "Health Labels: ${recipe.healthLabels.joinToString()}",
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                // Display other important data properties here
                            }
                        }
                        Button(
                            onClick = { selectedRecipe.value = null },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Close")
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    favoritesState: MutableList<Recipe>,
    jsonFileHandler: JsonFileHandler,
    onClick: () -> Unit
) {
    val isLiked = remember { mutableStateOf(favoritesState.any { it.name == recipe.name && it.source == recipe.source }) }

    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Image(
                painter = rememberImagePainter(data = recipe.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(10.dp)
                    .clip(shape = RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "Source: ${recipe.source}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Yield: ${recipe.yield}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            // Display other desired information

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        isLiked.value = !isLiked.value
                        if (isLiked.value) {
                            jsonFileHandler.updateFavorites(recipe)
                        } else {
                            favoritesState.removeAll { it.name == recipe.name && it.source == recipe.source }
                            jsonFileHandler.saveFavorites(favoritesState)
                        }
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite",
                        tint = if (isLiked.value) Color.Magenta else Color.Gray
                    )
                }
            }
        }
    }
}



data class Recipe(
    val name: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val source: String,
    val url: String,
    val yield: Int,
    val dietLabels: List<String>,
    val healthLabels: List<String>,
    val cautions: List<String>,
    val totalWeight: Int,
    val cuisineType: List<String>,
    val mealType: List<String>,
    val dishType: List<String>,
    val calories: Int,
    val tags: List<String>
)


fun performRecipeSearch(
    query: String,
    recipeType: String,
    appId: String,
    appKey: String,
    client: OkHttpClient,
    recipesState: MutableList<Recipe>
) {
    val baseUrl = "https://api.edamam.com/api/recipes/v2"
    val url = "$baseUrl?type=$recipeType&q=$query&app_id=$appId&app_key=$appKey"

    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("La requête a échoué : ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseBody = response.body?.string()
            println("Réponse : $responseBody")

            // Analyser la réponse JSON pour extraire les noms et les images des recettes
            val recipes = parseRecipes(responseBody)

            // Mettre à jour la variable d'état recipesState avec les recettes
            recipesState.clear()
            recipesState.addAll(recipes)
        }
    })
}
fun parseRecipes(responseBody: String?): List<Recipe> {
    val recipes = mutableListOf<Recipe>()

    val jsonObject = JSONObject(responseBody)
    val hitsArray = jsonObject.getJSONArray("hits")

    for (i in 0 until hitsArray.length()) {
        val hitObject = hitsArray.getJSONObject(i)
        val recipe = hitObject.getJSONObject("recipe")
        val name = recipe.getString("label")
        val imageUrl = recipe.getString("image")
        val ingredientsArray = recipe.getJSONArray("ingredients")

        val ingredients = mutableListOf<String>()
        for (j in 0 until ingredientsArray.length()) {
            val ingredientObject = ingredientsArray.getJSONObject(j)
            val ingredientName = ingredientObject.getString("text")
            val ingredient = "${ingredientObject.getInt("weight")}g - $ingredientName"
            ingredients.add(ingredient)
        }

        val instructions = mutableListOf<String>()
        if (recipe.has("instructions")) {
            val instructionsArray = recipe.getJSONArray("instructions")
            for (j in 0 until instructionsArray.length()) {
                val instruction = instructionsArray.getString(j)
                instructions.add(instruction)
            }
        }

        val recipeData = Recipe(
            name = name,
            imageUrl = imageUrl,
            ingredients = ingredients,
            instructions = instructions,
            // Additional properties
            source = recipe.optString("source"),
            url = recipe.optString("url"),
            yield = recipe.optInt("yield"),
            dietLabels = parseStringArray(recipe.optJSONArray("dietLabels")),
            healthLabels = parseStringArray(recipe.optJSONArray("healthLabels")),
            cautions = parseStringArray(recipe.optJSONArray("cautions")),
            totalWeight = recipe.optInt("totalWeight"),
            cuisineType = parseStringArray(recipe.optJSONArray("cuisineType")),
            mealType = parseStringArray(recipe.optJSONArray("mealType")),
            dishType = parseStringArray(recipe.optJSONArray("dishType")),
            calories = recipe.optInt("calories"),
            tags = parseStringArray(recipe.optJSONArray("tags"))
        )

        recipes.add(recipeData)
    }

    return recipes
}

fun parseStringArray(jsonArray: JSONArray?): List<String> {
    val result = mutableListOf<String>()
    if (jsonArray != null) {
        for (i in 0 until jsonArray.length()) {
            result.add(jsonArray.getString(i))
        }
    }
    return result
}

