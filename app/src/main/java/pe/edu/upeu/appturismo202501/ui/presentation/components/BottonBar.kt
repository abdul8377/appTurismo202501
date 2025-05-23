package pe.edu.upeu.appturismo202501.ui.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations

@Composable
fun BottomNavigationBar(items: List<Destinations>, navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoutex = navBackStackEntry?.destination?.route

    // Condición para no mostrar la barra en la ruta de login
    if (currentRoutex == null || currentRoutex == Destinations.Login.route) {
        return
    }

    // Variable para almacenar el índice del item seleccionado
    var selectedItem by remember { mutableStateOf(0) }

    // Variable para almacenar la ruta actual
    var currentRoute by remember { mutableStateOf(Destinations.Welcome.route) }

    // Iteración para encontrar el índice del item seleccionado
    items.forEachIndexed { index, navigationItem ->
        if (navigationItem.route == currentRoute) {
            selectedItem = index
        }
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                alwaysShowLabel = true,
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    currentRoute = item.route
                    navController.navigate(item.route) {
                        if (item.route == Destinations.Welcome.route) {
                            popUpTo(item.route)
                        } else {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
