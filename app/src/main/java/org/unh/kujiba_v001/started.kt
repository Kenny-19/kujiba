package org.unh.kujiba_v001

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController


class StartActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartScreen(this)
        }
    }
}

@Composable
fun StartScreen(context: ComponentActivity) {
    val navController = rememberNavController() // Here, you might need to handle navigation differently depending on your app setup.
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.kujiba_app),  // Assurez-vous que R.drawable.kujiba_app est correct
                contentDescription = "Logo de l'application",  // Description pour l'accessibilité
                modifier = Modifier.size(180.dp)  // Définir une taille, ajustez selon vos besoins
            )

            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = {
                    val intent = Intent(context, MainActivity::class.java) // S'assurer que LoginActivity est bien l'activité à démarrer
                    context.startActivity(intent)
                }
            ) {
                Text("Commencer")
            }

        }
    }
}
