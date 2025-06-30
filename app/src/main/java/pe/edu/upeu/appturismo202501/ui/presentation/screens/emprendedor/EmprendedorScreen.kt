package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.paquetes.CreateTourPackageScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.paquetes.EditTourPackageScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.paquetes.TourPackageListScreen

import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.productos.EmprendedorProductoFormScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.productos.ProductEditForm
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.productos.ProductosEmprendedorScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.servicios.CrearServicioScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.servicios.EmprendedorServicesListScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.servicios.ServiceEditScreen

@Composable
fun EmprendedorScreen(
    navController: NavHostController,
    onLogoutClicked: () -> Unit
) {
    val emprNavController = rememberNavController()
    val items = listOf(
        DrawerNavItem("Inicio", Icons.Default.Home, Destinations.Emprendedor.route),
        DrawerNavItem("Mi Perfil",    Icons.Default.Person,      "perfil_emprendedor"),
        DrawerNavItem("Mis Productos",Icons.Default.Settings,    "productos_emprendedor"),
        DrawerNavItem("Mis Servicios",Icons.Default.CleanHands,    "servicios_emprendedor"),
        DrawerNavItem("Mis Paquetes",Icons.Default.CheckBox,    "paquetes_turisticos"),
        DrawerNavItem("Disponibilidad" , Icons.Default.AddTask, "disponibilidad"),
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
            navController = emprNavController,
            startDestination = Destinations.Emprendedor.route,
            modifier = Modifier.fillMaxSize()
        ) {
            // Pantalla principal de Emprendedor
            composable(Destinations.Emprendedor.route) {
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
                ProductosEmprendedorScreen(
                    onCreate = { emprNavController.navigate("crear_producto") },
                    onEdit = { p -> emprNavController.navigate("editar_producto/${p.id}") },

                    )
            }


            // 4) Formulario para crear un producto
            composable("crear_producto") {
                // Aquí pasamos el mismo NavController
                EmprendedorProductoFormScreen(navController = emprNavController)
            }

            composable(
                route = "editar_producto/{productId}",
                arguments = listOf(navArgument("productId") {
                    type = NavType.LongType
                })
            ) { backStack ->
                val productId = backStack.arguments!!.getLong("productId")
                ProductEditForm(
                    navController = emprNavController,  // ← aquí el mismo controller
                    productId = productId
                )
            }

            composable("servicios_emprendedor") {
                EmprendedorServicesListScreen(
                    onAddService    = { emprNavController.navigate("crear_servicio") },
                    onEditService   = { id -> emprNavController.navigate("editar_servicio/$id") },
                    onDeleteService = { id ->
                        // ¡No necesitamos aquí el VM!
                        // El propio Composable llamará a loadPropios() tras eliminar
                        emprNavController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("deleteServiceId", id)
                    }
                )
            }

            composable("crear_servicio") {
                CrearServicioScreen(navController = emprNavController)
            }

            composable(

                route = "editar_servicio/{serviceId}",
                arguments = listOf(navArgument("serviceId") {
                    type = NavType.LongType
                })
            ) { backStack ->
                val serviceId = backStack.arguments?.getLong("serviceId") ?: return@composable
                ServiceEditScreen(
                    serviceId = serviceId,
                    navController = emprNavController
                )
            }

            composable("paquetes_turisticos") {
                TourPackageListScreen(
                    onAddPackage = { emprNavController.navigate("crear_paquete") },
                    onViewDetails = { id -> emprNavController.navigate("detalle_paquete/$id") },
                    onEditPackage = { id -> emprNavController.navigate("editar_paquete/$id") },
                    onDeletePackage = { /* la ruta delete la maneja internamente el ViewModel */ }
                )
            }
//            // 2) CREAR PAQUETE
            composable("crear_paquete") {
                CreateTourPackageScreen(
                    navController       = emprNavController,
                    paqueteViewModel    = hiltViewModel(),
                    serviciosViewModel  = hiltViewModel()
                )
            }
//
//            // 3) DETALLE DE PAQUETE
//            composable(
//                "detalle_paquete/{paqueteId}",
//                arguments = listOf(navArgument("paqueteId") { type = NavType.LongType })
//            ) { backStack ->
//                val paqueteId = backStack.arguments!!.getLong("paqueteId")
//                PaqueteDetailScreen(
//                    paqueteId = paqueteId,
//                    onBack = { emprNavController.popBackStack() }
//                )
//            }
//
//            // 4) EDITAR PAQUETE
          composable(
                "editar_paquete/{paqueteId}",
                arguments = listOf(navArgument("paqueteId") { type = NavType.LongType })
            ) { backStack ->
                val paqueteId = backStack.arguments!!.getLong("paqueteId")
              EditTourPackageScreen(
                    paqueteId = paqueteId,
                    navController = emprNavController
                  )
            }

            composable("disponibilidad") {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Pantalla disponibilidad")
                }
            }

        }
    }
}
