package org.unh.kujiba_v001.fragment.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.preference.PreferenceManager

@Composable
fun AdditionalInfoPage(navController: NavController) {
    
    val intolerancesList = listOf(
        "Alcohol-free",
        "Balanced",
        "High-Fiber",
        "High-Protein",
        "Keto",
        "Kidney friendly",
        "Kosher",
        "Low-Carb",
        "Low-Fat",
        "Low potassium",
        "Low-Sodium",
        "No oil added",
        "No-sugar",
        "Paleo",
        "Pescatarian",
        "Pork-free",
        "Red meat-free",
        "Sugar-conscious",
        "Vegan",
        "Vegetarian"
    )
    val allergiesList = listOf(
        "Celery-free",
        "Crustacean-free",
        "Dairy-free",
        "Egg-free",
        "Fish-free",
        "Gluten-free",
        "Lupine-free",
        "Mustard-free",
        "Peanut-free",
        "Sesame-free",
        "Shellfish-free",
        "Soy-free",
        "Tree-Nut-free",
        "Wheat-free"
    )

    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)

    var username by remember { mutableStateOf(sharedPreferences.getString("username", "") ?: "") }
    var selectedAllergies by remember { mutableStateOf(sharedPreferences.getStringSet("selectedAllergies", emptySet<String>()) ?: emptySet<String>()) }
    var selectedIntolerances by remember { mutableStateOf(sharedPreferences.getStringSet("selectedIntolerances", emptySet<String>()) ?: emptySet<String>()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Additional Information",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        item {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = "Allergies",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                allergiesList.forEach { allergy ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Checkbox(
                            checked = allergy in selectedAllergies,
                            onCheckedChange = { isChecked ->
                                selectedAllergies = if (isChecked) {
                                    selectedAllergies + allergy
                                } else {
                                    selectedAllergies - allergy
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = allergy, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                    }
                }
            }
        }

        item {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = "Intolerances",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                intolerancesList.forEach { intolerance ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Checkbox(
                            checked = intolerance in selectedIntolerances,
                            onCheckedChange = { isChecked ->
                                selectedIntolerances = if (isChecked) {
                                    selectedIntolerances + intolerance
                                } else {
                                    selectedIntolerances - intolerance
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = intolerance, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    val editor = sharedPreferences.edit()
                    editor.putString("username", username)
                    editor.putStringSet("selectedAllergies", selectedAllergies)
                    editor.putStringSet("selectedIntolerances", selectedIntolerances)
                    editor.apply()

                    // Redirection vers une autre page, par exemple la page d'accueil
                    navController.navigate("home")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun AllergiesIntolerancesList(
    allergies: List<String>,
    selectedAllergies: Set<String>,
    onAllergySelected: (String) -> Unit,
    intolerances: List<String>,
    selectedIntolerances: Set<String>,
    onIntoleranceSelected: (String) -> Unit
){
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = "Allergies",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        allergies.forEach { allergy ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Checkbox(
                    checked = allergy in selectedAllergies,
                    onCheckedChange = { isChecked ->
                        onAllergySelected(allergy)
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = allergy, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Intolerances",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        intolerances.forEach { intolerance ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Checkbox(
                    checked = intolerance in selectedIntolerances,
                    onCheckedChange = { isChecked ->
                        onIntoleranceSelected(intolerance)
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = intolerance, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
            }
        }
    }
}