package org.unh.kujiba_v001.navigation_a_ne_plus_utiliser
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.unh.kujiba_v001.fragment.FavoritesScreen
import org.unh.kujiba_v001.fragment.LogoutScreen
import org.unh.kujiba_v001.fragment.RecipeCardsScreen
import org.unh.kujiba_v001.fragment.SettingsScreen
import org.unh.kujiba_v001.R


@Composable
fun NavigationDrawer() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(
        DrawerItem(icon = Icons.Default.Favorite, label = "Recettes Favorites", secondaryLabel = " "),
        DrawerItem(icon = Icons.Default.Done, label = "Fiche de Recettes", secondaryLabel = " "),
        DrawerItem(icon = Icons.Default.Settings, label = "Paramètre", secondaryLabel = " "),
        DrawerItem(icon = Icons.Default.ExitToApp, label = "Déconnexion", secondaryLabel = ""),
    )
    var selectedItem by remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.kujiba_app),
                        contentDescription = "kujiba_app",
                        modifier = Modifier.size(100.dp)
                    )
                }
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.label) },
                        selected = item == selectedItem,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem = item
                        },
                        icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                        badge = { Text(text = item.secondaryLabel) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            Content2(
                onMenuIconClick = { scope.launch { drawerState.open() } }
            )
        }
    )
}

data class DrawerItem(
    val icon: ImageVector,
    val label: String,
    val secondaryLabel: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content2(
    onMenuIconClick: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onMenuIconClick) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                title = { Text(text = "Menu") }
            )
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                NavHost(navController, startDestination = Screen.Settings.route) {
                    composable(Screen.Settings.route) {
                        SettingsScreen()
                    }
                    composable(Screen.Favorites.route) {
                        FavoritesScreen()
                    }
                    composable(Screen.RecipeCards.route) {
                        RecipeCardsScreen()
                    }
                    composable(Screen.Logout.route) {
                        LogoutScreen()
                    }
                }
            }
        }
    )
}













