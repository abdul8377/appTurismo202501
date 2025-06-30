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

    // Roles y pantallas
    object Emprendedor : Destinations("emprendedor", "Emprendedor", Icons.Filled.Person)
    object Usuario : Destinations("usuario", "Usuario", Icons.Filled.Person)
    object Administrador : Destinations("administrador", "Administrador", Icons.Filled.Person)
    object User :
        Destinations("user", "User", Icons.Filled.Person) // pantalla para administrar usuarios

    object ForgotPassword :
        Destinations("forgot_password", "Forgot Password", Icons.Filled.Settings)

    object Negocios : Destinations("negocios", "Negocios", Icons.Filled.Person)
    object Ajustes : Destinations("ajustes", "Ajustes", Icons.Filled.Settings)
    object ZonasTuristicasAdministrador : Destinations("ZonasTuristicasAdministrador", "ZonasTuristicasAdministrador", Icons.Filled.Place)


    object Notifications : Destinations("notifications", "Notificaciones", Icons.Filled.Notifications)


    object VerTipoDeNegocio : Destinations(
        "ver_tipo_de_negocio_screen/{id}",
        "Detalle Negocio",
        Icons.Filled.Info
    )

    object Create_Emprendimiento : Destinations(

        "emprendimiento_create",
        "Emprendimiento_Create",
        Icons.Filled.Accessibility
    )

    object CulturaTab : Destinations("explorar/cultura", "Cultura", Icons.Filled.Museum)
    object ProductosTab : Destinations("explorar/productos", "Productos", Icons.Filled.ShoppingCart)
    object GastronomiaTab : Destinations("explorar/gastronomia", "Gastronomia", Icons.Filled.Cable)
    object ExperienciasTab :
        Destinations("explorar/experiencia", "Experiencias", Icons.Filled.Cable)

    object AlojamientoTab : Destinations("explorar/alojamiento", "Alojamientos", Icons.Filled.Cable)
    object GuiasTab : Destinations("explorar/guias", "Guias", Icons.Filled.Cable)
    object Alojamientos : Destinations(
        "explorar/alojamiento/alojamientoDetalle/{id}",  // con {id}
        "AlojamientosDetalle",
        Icons.Filled.Hotel
    )
    object Servicios : Destinations(
        "explorar/Servicio/ServicioDetalle/{id}",  // con {id}
        "AlojamientosDetalle",
        Icons.Filled.Hotel
    )


}