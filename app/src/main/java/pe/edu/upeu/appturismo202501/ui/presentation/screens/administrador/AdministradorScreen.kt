package pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.DrawerNavItem
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.SidebarDrawer
import pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador.CategoriasServicios.CategoriasScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador.blogs.BlogsScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador.emprendimientos.ListaEmprendimientosScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador.zonasTuriticas.TourismZonesScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador.zonasTuriticas.ZonasTuristicasList
import pe.edu.upeu.appturismo202501.ui.presentation.screens.tipodenegocio.TipoDeNegocioScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.tipodenegocio.VerTipoDeNegocioScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.user.UserScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.ajustes.AjustesScreen
import pe.edu.upeu.appturismo202501.utils.SessionManager

@Composable
fun AdministradorScreen(
    navController: NavHostController,
    onLogoutClicked: () -> Unit
) {
    val adminNavController = rememberNavController()

    val items = listOf(
        DrawerNavItem("Inicio",   Icons.Default.Home,     Destinations.Administrador.route),
        DrawerNavItem("Usuarios", Icons.Default.Person,   Destinations.User.route),
        DrawerNavItem("Negocios", Icons.Default.Person,   Destinations.Negocios.route),
        DrawerNavItem("Ajustes",  Icons.Default.Settings, Destinations.Ajustes.route),
        DrawerNavItem("Zonas Turisticas", Icons.Default.Place, Destinations.ZonasTuristicasAdministrador.route),
        DrawerNavItem("Emoprendimeintos", Icons.Default.Person, Destinations.EmprendimientosAdministrador.route),
        DrawerNavItem("Categorias Servicios", Icons.Default.HomeRepairService, Destinations.CatServiciosAdministrador.route),
        DrawerNavItem("Blogs", Icons.Default.HomeRepairService, Destinations.BlogsAdministrador.route),

        )

    SidebarDrawer(
        items = items,
        selectedRoute = adminNavController.currentBackStackEntryAsState().value
            ?.destination?.route
            ?: Destinations.Administrador.route,
        onItemClicked = { item ->
            adminNavController.navigate(item.route) {
                popUpTo(Destinations.Administrador.route) { inclusive = false }
                launchSingleTop = true
            }
        },
        onLogoutClicked = onLogoutClicked
    ) {
        NavHost(
            navController = adminNavController,
            startDestination = Destinations.Administrador.route,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(Destinations.Administrador.route) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                }
            }
            composable(Destinations.User.route) {
                UserScreen()
            }
            composable(Destinations.Negocios.route) {
                TipoDeNegocioScreen(navController = adminNavController)
            }
            composable(Destinations.Ajustes.route) {
                AjustesScreen()
            }
            composable(
                route = "ver_tipo_de_negocio_screen/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStack ->
                val id = backStack.arguments?.getLong("id") ?: 0L
                VerTipoDeNegocioScreen(id)
            }

            composable(Destinations.ZonasTuristicasAdministrador.route) {
                // Importa tu Composable:
                TourismZonesScreen()
            }

            composable(Destinations.EmprendimientosAdministrador.route) {
                // Importa tu Composable:
                ListaEmprendimientosScreen()
            }

            composable(Destinations.CatServiciosAdministrador.route) {
                // Importa tu Composable:
                CategoriasScreen()
            }
            composable(Destinations.BlogsAdministrador.route) {
                BlogsScreen()
            }

        }
    }
}
