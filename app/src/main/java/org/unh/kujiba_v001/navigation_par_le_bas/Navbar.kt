package org.unh.kujiba_v001.navigation_par_le_bas

import Quota
import SearchScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import org.unh.kujiba_v001.fragment.login.AdditionalInfoPage
import org.unh.kujiba_v001.fragment.login.App
import org.unh.kujiba_v001.fragment.search.JsonFileHandler
import org.unh.kujiba_v001.fragment.search.SearchState
import org.unh.kujiba_v001.fragment.settings.DisplayInfoPage
import org.unh.kujiba_v001.fragment.settings.parametrescreen
import org.unh.kujiba_v001.screens.FavoriteScreen
import org.unh.kujiba_v001.screens.SignOutScreen


@Composable
fun NavigationBarWithScaffold() {
    val context = LocalContext.current
    val jsonFileHandler = remember { JsonFileHandler(context) }

    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val searchState = remember { SearchState() }
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    val selectedAllergies = remember { mutableStateOf(emptySet<String>()) }
    val selectedIntolerances = remember { mutableStateOf(emptySet<String>()) }
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


    val barItems = listOf(
        BarItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "home"
        ),
        BarItem(
            title = "Favorites",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder,
            route = "favorites"
        ),
        BarItem(
            title = "Quota",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            route = "quota"
        ),
        BarItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = "parametre"
        ),
    )

    Scaffold(
        bottomBar = {
            NavigationBar(navController = navController, barItems = barItems)
        },

    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) { // Use descriptive name for padding



            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("home") { SearchScreen(navController, jsonFileHandler, searchState)}
                composable("favorites") { FavoriteScreen(navController,jsonFileHandler) }
                composable("quota") { Quota(jsonFileHandler = jsonFileHandler) }
                composable("additionalInfo"){
                    AdditionalInfoPage(navController = navController)
                }
                composable("parametre"){ parametrescreen(navController = navController)}
                composable("infouser"){ DisplayInfoPage()}
                composable("login"){App()}
                composable("logout"){SignOutScreen(navController, FirebaseAuth.getInstance())}


                     }

            }
        }
    }


@Composable
fun NavigationBar(
    navController: NavHostController,
    barItems: List<BarItem>
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = Color.Gray, // Couleur de fond de la barre de navigation
        contentColor = Color.White, // Couleur du texte et des icônes
    ) {
        barItems.forEach { barItem ->
            val selected = barItem.route == currentRoute
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    navController.navigate(barItem.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected) barItem.selectedIcon else barItem.unselectedIcon,
                        contentDescription = barItem.title
                    )
                },
                label = { Text(text = barItem.title) },
                alwaysShowLabel = false
            )
        }
    }
}

data class BarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String
)