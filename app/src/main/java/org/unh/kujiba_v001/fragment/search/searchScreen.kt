package org.unh.kujiba_v001.fragment.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController



@Composable
fun searchScreen(navController: NavHostController) {

    val searchQuery = remember { mutableStateOf("") }
    val allergies = listOf(
        "Arachides",
        "Lait",
        "Œufs",
        "Soja",
        "Fruits à coque",
        "Blé",
        "Poisson",
        "Crustacés",
        "Mollusques"
    )


    val filteredAllergies = allergies.filter { it.contains(searchQuery.value, ignoreCase = true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = { newValue ->
                searchQuery.value = newValue
            },
            label = { Text(text = "Recherche d'allergies") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (filteredAllergies.isNotEmpty()) {
            LazyColumn {
                items(filteredAllergies) { allergy ->
                    Text(
                        text = allergy,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        } else {
            Text(
                text = "Aucune allergie trouvée",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    }