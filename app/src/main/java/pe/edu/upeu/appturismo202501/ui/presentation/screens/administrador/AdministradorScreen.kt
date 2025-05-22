package pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.DrawerNavItem
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.SidebarDrawer
import pe.edu.upeu.appturismo202501.utils.TokenUtils

@Composable
fun AdministradorScreen(navController: NavHostController,onLogoutClicked: () -> Unit) {
    // 1) Define los ítems que quieras en el drawer
    val items = listOf(
        DrawerNavItem("Inicio", Icons.Default.Home, "inicio"),
        DrawerNavItem("Perfil", Icons.Default.Person, "perfil"),
        DrawerNavItem("Ajustes", Icons.Default.Settings, "ajustes")
    )

    // 2) Estado local de la opción seleccionada
    var selectedId by rememberSaveable { mutableStateOf("dashboard") }

    // 3) Llama al componente reutilizable
    SidebarDrawer(
        items = items,
        selectedItemId = "inicio",
        onItemClicked = { item ->
            when (item.id) {
                "inicio" -> navController.navigate(Destinations.Pantalla1.route)
                "perfil" -> navController.navigate(Destinations.PerfilWelcome.route)
                "ajustes" -> navController.navigate(Destinations.Welcome.route)
            }
        },
        onLogoutClicked = onLogoutClicked
    ) {
        // 4) Este content es tu área central: muéstrala según selectedId
        when(selectedId) {
            "dashboard" -> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Panel de Administración",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            "usuarios" -> {
                /* Composable UsuariosScreen(navController) */
            }
            "ajustes" -> {
                /* Composable AjustesScreen(navController) */
            }
            "salir" -> {
                /* quizá muestres un diálogo o regreses al login */
            }
        }
    }
}