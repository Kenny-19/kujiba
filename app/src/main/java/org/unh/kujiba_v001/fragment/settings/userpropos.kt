package org.unh.kujiba_v001.fragment.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager

@Composable
fun DisplayInfoPage() {
    val context = LocalContext.current
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    val username = sharedPreferences.getString("username", "N/A") ?: "N/A"
    val allergies = sharedPreferences.getStringSet("selectedAllergies", emptySet()) ?: emptySet()
    val intolerances = sharedPreferences.getStringSet("selectedIntolerances", emptySet()) ?: emptySet()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Informations de l'utilisateur",
            style = MaterialTheme.typography.headlineMedium
        )
        Divider()

        Text("Nom d'utilisateur: $username", style = MaterialTheme.typography.bodyLarge)

        if (allergies.isNotEmpty()) {
            Text("Allergies sélectionnées:", style = MaterialTheme.typography.bodyLarge)
            allergies.forEach { allergy ->
                Text("- $allergy", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            Text("Aucune allergie sélectionnée", style = MaterialTheme.typography.bodyMedium)
        }
        Divider()
        if (intolerances.isNotEmpty()) {
            Text("Intolérances sélectionnées:", style = MaterialTheme.typography.bodyLarge)
            intolerances.forEach { intolerance ->
                Text("- $intolerance", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            Text("Aucune intolérance sélectionnée", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
