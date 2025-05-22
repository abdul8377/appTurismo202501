package pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.DrawerNavItem
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.SidebarDrawer
import pe.edu.upeu.appturismo202501.utils.SessionManager

@Composable
fun AdministradorScreen(
    navController: NavHostController,
    onLogoutClicked: () -> Unit,       // Parámetro para manejar logout desde NavigationHost
    viewModel: AdministradorViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val items = listOf(
        DrawerNavItem("Inicio", Icons.Default.Home, "inicio"),
        DrawerNavItem("Perfil", Icons.Default.Person, "perfil"),
        DrawerNavItem("Ajustes", Icons.Default.Settings, "ajustes")
    )

    var selectedId by rememberSaveable { mutableStateOf("inicio") }
    val isLoading by remember { viewModel::isLoading }

    SidebarDrawer(
        items = items,
        selectedItemId = selectedId,
        onItemClicked = { item ->
            selectedId = item.id
            when (item.id) {
                "inicio" -> navController.navigate(Destinations.Pantalla1.route)
                "perfil" -> navController.navigate(Destinations.PerfilWelcome.route)
                "ajustes" -> navController.navigate(Destinations.Welcome.route)
            }
        },
        onLogoutClicked = {
            val token = SessionManager.getToken()
            if (!token.isNullOrEmpty()) {
                viewModel.logout(token,
                    onLogoutSuccess = {
                        onLogoutClicked()  // Se ejecuta la función pasada desde NavigationHost
                    },
                    onLogoutFailed = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                onLogoutClicked() // Si no hay token, también ejecutar logout
            }
        }
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            when (selectedId) {
                "inicio" -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Panel de Administración", style = MaterialTheme.typography.headlineMedium)
                }
                "perfil" -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Perfil del Administrador", style = MaterialTheme.typography.headlineMedium)
                }
                "ajustes" -> Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Configuraciones", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}
