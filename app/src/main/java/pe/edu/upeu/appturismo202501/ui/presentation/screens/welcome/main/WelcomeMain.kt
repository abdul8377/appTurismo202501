package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.NavItem
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.TurismoNavigationBar
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedorcreate.EmprendedorCreateScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito.CarritoScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.ExplorarScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito.FavoritosScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil.PerfilScreen

enum class BottomDestination(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    EXPLORAR("Explorar", "explorar", Icons.Filled.Place, Icons.Outlined.Place),
    FAVORITO("Favoritos", "Favoritos", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    CARRITO("Carrito", "carrito", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    RESERVAS("Reservas", "reservas", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    PERFIL("Perfil", "perfil", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeMain(
    navControllerGlobal: NavHostController,
    initialRoute: String = BottomDestination.EXPLORAR.route
) {
    val navControllerLocal = rememberNavController()
    var selectedRoute by rememberSaveable { mutableStateOf(initialRoute) }

    // Eliminamos padding innecesario en el Scaffold
    Scaffold(
        bottomBar = {
            // Aquí también aseguramos que la barra de navegación esté bien alineada
            val navItems = BottomDestination.entries.map { destination ->
                NavItem(
                    label = destination.label,
                    route = destination.route,
                    selectedIcon = destination.selectedIcon,
                    unselectedIcon = destination.unselectedIcon
                )
            }

            TurismoNavigationBar(
                items = navItems,
                selectedRoute = selectedRoute,
                onItemSelected = { selectedItem ->
                    navControllerLocal.navigate(selectedItem.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navControllerLocal.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                    selectedRoute = selectedItem.route
                }
            )
        }
    ) { innerPadding ->

        // Usamos Modifier.fillMaxSize() en lugar de Modifier.padding(innerPadding) para evitar problemas con el padding
        NavHost(
            navController = navControllerLocal,
            startDestination = initialRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(BottomDestination.EXPLORAR.route) {
                ExplorarScreen(navControllerGlobal)
            }
            composable(BottomDestination.FAVORITO.route) {
                FavoritosScreen(navController = navControllerGlobal)
            }
            composable(BottomDestination.CARRITO.route) {
                CarritoScreen()
            }
            composable(BottomDestination.RESERVAS.route) {
                // ReservasScreen()
            }
            composable(BottomDestination.PERFIL.route) {
                PerfilScreen(
                    navControllerGlobal = navControllerGlobal,
                    navControllerLocal = navControllerLocal
                )
            }
            composable("emprendimiento_create") {
                EmprendedorCreateScreen(navController = navControllerLocal)
            }
        }

    }
}
