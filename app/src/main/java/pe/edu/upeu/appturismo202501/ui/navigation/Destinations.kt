package pe.edu.upeu.appturismo202501.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Welcome : Destinations("welcome", "Welcome", Icons.Filled.Home)
    object Login : Destinations("login", "Login", Icons.Filled.Settings)
    object Register : Destinations("register", "Register", Icons.Filled.Check)
    object Search : Destinations("search", "Search", Icons.Filled.Search)
    object PerfilWelcome : Destinations("perfilWelcome", "PerfilWelcome", Icons.Filled.Person)

    // Roles y pantallas
    object Emprendedor : Destinations("emprendedor", "Emprendedor", Icons.Filled.Person)
    object Usuario : Destinations("usuario", "Usuario", Icons.Filled.Person)
    object Administrador : Destinations("administrador", "Administrador", Icons.Filled.Person)
    object User : Destinations("user", "User", Icons.Filled.Person) // pantalla para administrar usuarios

    object ForgotPassword : Destinations("forgot_password", "Forgot Password", Icons.Filled.Settings)

    object Negocios : Destinations("negocios", "Negocios", Icons.Filled.Person)
    object Ajustes : Destinations("ajustes", "Ajustes", Icons.Filled.Settings)

    object VerTipoDeNegocio : Destinations(
        "ver_tipo_de_negocio_screen/{id}",
        "Detalle Negocio",
        Icons.Filled.Info // o el icono que prefieras
    )
}
