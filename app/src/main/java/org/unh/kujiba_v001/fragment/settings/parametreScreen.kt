package org.unh.kujiba_v001.fragment.settings


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun parametrescreen(navController: NavHostController) {
    Column {
        // Option 1 : Information Additionnelle
        ListItem(
            headlineContent = { Text("Information Additionnelle") },
            leadingContent = { Icon(imageVector = Icons.Filled.Info, contentDescription = "Information Additionnelle") },
            modifier = Modifier
                .clickable { navController.navigate("additionalInfo") }
                .padding(vertical = 8.dp)
        )
        Divider()

        // Option 2 : À propos de vous
        ListItem(
            headlineContent = { Text("À propos de vous") },
            leadingContent = { Icon(imageVector = Icons.Filled.Person, contentDescription = "À propos de vous") },
            modifier = Modifier
                .clickable { navController.navigate("infouser") }
                .padding(vertical = 8.dp)
        )
        Divider()
        ListItem(headlineContent = { Text("Deconnexion")},
            leadingContent = { Icon(imageVector = Icons.Filled.ExitToApp, contentDescription ="Deconnexion" )},
            modifier = Modifier
                .clickable { navController.navigate("logout") }
                .padding(vertical = 8.dp)
        )
    }
}


