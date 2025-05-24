package pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
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
import pe.edu.upeu.appturismo202501.ui.presentation.screens.tipodenegocio.TipoDeNegocioScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.user.UserScreen
import pe.edu.upeu.appturismo202501.utils.SessionManager

@Composable
fun AdministradorScreen(
    navController: NavHostController,
    onLogoutClicked: () -> Unit,
    viewModel: AdministradorViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Definir los ítems para el menú lateral
    val items = listOf(
        DrawerNavItem("Inicio", Icons.Default.Home, "inicio"),
        DrawerNavItem("Usuarios", Icons.Default.Person, "usuarios"),
        DrawerNavItem("Tipos de Negocio", Icons.Default.Person, "negocios"),
        DrawerNavItem("Ajustes", Icons.Default.Settings, "ajustes")
    )

    // Estado para manejar el item seleccionado en el menú
    var selectedId by rememberSaveable { mutableStateOf("inicio") }
    val isLoading by remember { viewModel::isLoading }

    // DrawerSidebar: Menú lateral con navegación
    SidebarDrawer(
        items = items,
        selectedItemId = selectedId,
        onItemClicked = { item ->
            selectedId = item.id
            when (item.id) {
                "inicio" -> {
                    // Aquí puedes agregar lógica para "Inicio" si es necesario
                }
                "usuarios" -> {
                    // Aquí iría la lógica para los usuarios
                }
                "negocios" -> {
                    // Aquí es donde se debe pasar el navController a la pantalla de TipoDeNegocioScreen
                }
                "ajustes" -> {
                    navController.navigate(Destinations.Welcome.route)
                }
            }
        },
        onLogoutClicked = {
            val token = SessionManager.getToken()
            if (!token.isNullOrEmpty()) {
                viewModel.logout(
                    token,
                    onLogoutSuccess = {
                        onLogoutClicked()
                    },
                    onLogoutFailed = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                onLogoutClicked()
            }
        }
    ) {
        // Mostrar contenido basado en la selección del menú
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
                    UserScreen()
                }
                "usuarios" -> UserScreen()
                "negocios" -> TipoDeNegocioScreen(navController) // Asegúrate de pasar el navController

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
