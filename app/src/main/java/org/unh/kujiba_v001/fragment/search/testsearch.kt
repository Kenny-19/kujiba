package org.unh.kujiba_v001.fragment.search
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

data class Recipe(val title: String, val imageUrl: String)

data class Hit(val recipe: Recipe)

data class RecipeSearchResponse(val hits: List<Hit>)

@Composable
fun RecipeSearchScreen() {
    var query by remember { mutableStateOf("") }
    var recipes by remember { mutableStateOf(emptyList<Recipe>()) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            label = { Text(text = "Search for recipes") }
        )

        Button(
            onClick = {
                if (query.isNotEmpty()) {
                    scope.launch {
                        isLoading = true
                        recipes = searchRecipes(query)
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(recipes) { recipe ->
                    RecipeItem(recipe = recipe)
                }
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = rememberImagePainter(recipe.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}



suspend fun searchRecipes(query: String): List<Recipe> = withContext(Dispatchers.IO) {
    val appId = "8101fbeb"
    val appKey = "\n" +
            "3057c99cf2f9b7d1411b0da32d221199\t"

    val client = OkHttpClient()
    val url = "https://api.edamam.com/api/recipes/v2".toHttpUrlOrNull()?.newBuilder()
        ?.addQueryParameter("q", query)
        ?.addQueryParameter("app_id", appId)
        ?.addQueryParameter("app_key", appKey)
        ?.build()

    val request = url?.let {
        Request.Builder()
            .url(it)
            .build()
    }

    val response: Response? = request?.let { client.newCall(it).execute() }

    if (response != null && response.isSuccessful) {
        val gson = Gson()
        val responseBody = response.body?.string()
        val recipeSearchResponse = gson.fromJson(responseBody, RecipeSearchResponse::class.java)
        return@withContext recipeSearchResponse?.hits?.map { hit -> hit.recipe } ?: emptyList()
    }
   print(response)
    print(url)
    return@withContext emptyList()
}










