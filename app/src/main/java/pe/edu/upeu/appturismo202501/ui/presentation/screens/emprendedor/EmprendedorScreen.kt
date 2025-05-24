package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.DrawerNavItem
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.SidebarDrawer

@Composable
fun EmprendedorScreen(
    navController: NavHostController,
    onLogoutClicked: () -> Unit
) {
    val emprNavController = rememberNavController()
    val items = listOf(
        DrawerNavItem("Inicio", Icons.Default.Home, Destinations.Emprendedor.route),
        DrawerNavItem("Mi Perfil",    Icons.Default.Person,      "perfil_emprendedor"),
        DrawerNavItem("Mis Productos",Icons.Default.Settings,    "productos_emprendedor")
    )

    SidebarDrawer (
        items          = items,
        selectedRoute  = emprNavController.currentBackStackEntryAsState().value
            ?.destination?.route
            ?: Destinations.Emprendedor.route,
        onItemClicked  = { item ->
            emprNavController.navigate(item.route) {
                popUpTo(Destinations.Emprendedor.route) { inclusive = false }
                launchSingleTop = true
            }
        },
        onLogoutClicked = {
            // limpias sesión y vuelves al login o welcome
            onLogoutClicked()
        }
    ) {
        // 4) Aquí anidamos el NavHost que carga cada Composable según la ruta interna
        NavHost(
            navController    = emprNavController,
            startDestination = Destinations.Emprendedor.route,
            modifier         = Modifier.fillMaxSize()
        ) {
            // Pantalla principal de Emprendedor
            composable (Destinations.Emprendedor.route) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Bienvenido Emprendedor")
                }
            }
            // Perfil
            composable("perfil_emprendedor") {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Mi Perfil de Emprendedor")
                }
            }
            // Productos
            composable("productos_emprendedor") {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Listado de mis Productos")
                }
            }
        }
    }

}
