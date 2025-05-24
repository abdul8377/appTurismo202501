package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.ProductionQuantityLimits
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.*
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito.CarritoScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.ExplorarScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil.PerfilScreen
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.TurismoNavigationBar
import androidx.compose.ui.graphics.vector.ImageVector
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.NavItem
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.productos.ProductosScreen

enum class BottomDestination(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    EXPLORAR("Explorar", "explorar", Icons.Filled.Place, Icons.Outlined.Place),
    PRODUCTOS("Productos", "productos", Icons.Filled.ProductionQuantityLimits, Icons.Outlined.ProductionQuantityLimits),
    CARRITO("Carrito", "carrito", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    RESERVAS("Reservas", "reservas", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
    PERFIL("Perfil", "perfil", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
}

@Composable
fun WelcomeMain() {
    val navController = rememberNavController()
    var selectedRoute by rememberSaveable { mutableStateOf(BottomDestination.EXPLORAR.route) }

    Scaffold(
        bottomBar = {
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
                selectedRoute = selectedRoute,  // Aquí pasas selectedRoute
                onItemSelected = { selectedItem ->
                    navController.navigate(selectedItem.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                    selectedRoute = selectedItem.route
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomDestination.EXPLORAR.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomDestination.EXPLORAR.route) { ExplorarScreen(navController) } // Pasar navController aquí
            composable(BottomDestination.PRODUCTOS.route) { ProductosScreen() }
            composable(BottomDestination.CARRITO.route) { CarritoScreen() }
            composable(BottomDestination.RESERVAS.route) { ReservasScreen() }
            composable(BottomDestination.PERFIL.route) { PerfilScreen(navController) }
        }
    }
}
