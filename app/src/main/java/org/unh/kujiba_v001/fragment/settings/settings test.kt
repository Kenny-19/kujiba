package org.unh.kujiba_v001.fragment.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.unh.kujiba_v001.fragment.login.AllergiesIntolerancesList


@Composable
fun SettingsPage(
    allergies: List<String>,
    selectedAllergies: MutableState<Set<String>>,
    intolerances: List<String>,
    selectedIntolerances: MutableState<Set<String>>,
    onSaveClicked: () -> Unit
) {
    var username by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.h5,
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

        AllergiesIntolerancesList(
            allergies = allergies,
            selectedAllergies = selectedAllergies.value,
            onAllergySelected = { allergy ->
                if (allergy in selectedAllergies.value) {
                    selectedAllergies.value -= allergy
                } else {
                    selectedAllergies.value += allergy
                }
            },
            intolerances = intolerances,
            selectedIntolerances = selectedIntolerances.value,
            onIntoleranceSelected = { intolerance ->
                if (intolerance in selectedIntolerances.value) {
                    selectedIntolerances.value -= intolerance
                } else {
                    selectedIntolerances.value += intolerance
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSaveClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(text = "Save")
        }
    }
}