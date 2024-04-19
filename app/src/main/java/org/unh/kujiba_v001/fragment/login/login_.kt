package org.unh.kujiba_v001.fragment.login


import AuthViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.unh.kujiba_v001.R
import org.unh.kujiba_v001.navigation_par_le_bas.NavigationBarWithScaffold


@Composable
fun LoginPage(authViewModel: AuthViewModel = viewModel(), navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrorSnackbar by remember { mutableStateOf(false) }

    val currentUser by authViewModel.currentUser.collectAsState()

    // Observer for sign-in result
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            if (user != null) {
                // Connexion réussie, naviguer vers une autre page
                navController.navigate("home")
            } else {
                // Afficher un message d'erreur
                showErrorSnackbar = true
            }
        }
    }

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
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Login",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    authViewModel.signIn(email, password)
                } else {
                    // Afficher un message d'erreur ou empêcher l'action
                    showErrorSnackbar = true
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(text = "Sign In")
        }

        TextButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create an account")
        }
    }

    // Snackbar for showing error message
    if (showErrorSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                Button(onClick = { showErrorSnackbar = false }) {
                    Text(text = "Dismiss")
                }
            }
        ) {
            Text(text = "Invalid email or password")
        }
    }
}
@Composable
fun RegisterPage(authViewModel: AuthViewModel, navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var intolerances by remember { mutableStateOf("") }

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
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Register",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )



        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    authViewModel.signUp(email, password)
                    navController.navigate("additionalInfo")
                } else {
                    // Afficher un message d'erreur ou empêcher l'action
                    // Vous pouvez afficher un Snackbar ou une autre notification d'erreur ici
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(text = "Register")
        }

        TextButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Already have an account? Sign in")
        }
    }
}


@Composable
fun App(authViewModel: AuthViewModel = viewModel()) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginPage(authViewModel = authViewModel, navController = navController)
            }
            composable("register") {
                RegisterPage(authViewModel = authViewModel, navController = navController)
            }
            composable("home"){
                NavigationBarWithScaffold()
            }
            composable("additionalInfo"){
                AdditionalInfoPage(navController)
            }
        }
    }
}












