
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.unh.kujiba_v001.fragment.FavoritesScreen
import org.unh.kujiba_v001.fragment.LogoutScreen
import org.unh.kujiba_v001.fragment.RecipeCardsScreen
import org.unh.kujiba_v001.fragment.SettingsScreen
import org.unh.kujiba_v001.fragment.search.searchScreen

@Composable
fun NavigationBarWithScaffold() {
    val navController = rememberNavController()
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
            route = "settings"
        ),
    )

    Scaffold(
        bottomBar = {
            NavigationBar(navController = navController, barItems = barItems)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("search")  }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Add")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { RecipeCardsScreen() }
                composable("favorites") { FavoritesScreen() }
                composable("quota") { LogoutScreen() }
                composable("settings") { SettingsScreen() }
                composable("search"){ searchScreen(navController) }
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

    BottomNavigation {
        barItems.forEach { barItem ->
            val selected = barItem.route == currentRoute
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    navController.navigate(barItem.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
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
                alwaysShowLabel = false // Change to true if you want to always show the label
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
