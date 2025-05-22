package pe.edu.upeu.appturismo202501.ui.navigation

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check

import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String,
    val icon: ImageVector) {


    object Welcome : Destinations("Welcome", "Welcome", Icons.Filled.Home)
    object Login : Destinations("login", "Login", Icons.Filled.Settings)
    object Register : Destinations("register", "register", Icons.Filled.Check)
    object Pantalla1 : Destinations("pantalla1", "Pantalla1", Icons.Filled.Home)
    object Search : Destinations("search", "Search", Icons.Filled.Search)
    object PerfilWelcome: Destinations("perfilWelcome", "PerfilWelcome", Icons.Filled.Person)

    // Aquí añades los nuevos destinos que usas para los roles:
    object Emprendedor : Destinations("emprendedor", "Emprendedor", Icons.Filled.Person)
    object Usuario : Destinations("usuario", "Usuario", Icons.Filled.Person)
    object Administrador : Destinations("administrador", "Administrador", Icons.Filled.Person)
    // Nueva ruta ForgotPassword
    object ForgotPassword : Destinations("forgot_password", "Forgot Password", Icons.Filled.Settings)
}

