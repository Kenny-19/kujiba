package org.unh.kujiba_v001.screens


import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import org.unh.kujiba_v001.R
import org.unh.kujiba_v001.StartActivity
import java.io.File

@Composable
fun SignOutScreen(navController: NavController, firebaseAuth: FirebaseAuth) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.kujiba_app),
            contentDescription = "Logo de l'application",
            modifier = Modifier.size(120.dp).align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Avant de quitter, souvenez-vous que vous pouvez toujours vous rassasier sans problème chez nous!",
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                signOut(firebaseAuth, context)
                val intent = Intent(context, StartActivity::class.java) // S'assurer que LoginActivity est bien l'activité à démarrer
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Déconnexion")
        }
    }
}

fun signOut(firebaseAuth: FirebaseAuth, context: Context) {
    // Effacer SharedPreferences
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    sharedPreferences.edit().clear().apply()

    // Effacer les données de recettes favorites si elles sont stockées localement
    clearFavorites(context)

    // Déconnexion de Firebase
    firebaseAuth.signOut()
}

fun clearFavorites(context: Context) {
    val file = File(context.filesDir, "favorites.json")
    if (file.exists()) {
        file.delete()
    }
}