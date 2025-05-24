package pe.edu.upeu.appturismo202501.ui.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.edu.upeu.appturismo202501.ui.presentation.screens.*
import pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador.AdministradorScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.EmprendedorScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.forgotpassword.ForgotPasswordScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.register.RegisterScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.tipodenegocio.VerTipoDeNegocioScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.user.UserScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.usuario.UsuarioScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.*
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.main.WelcomeMain
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil.PerfilScreen
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.utils.TokenUtils

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun NavigationHost(
    navController: NavHostController,
    darkMode: MutableState<Boolean>,
    innerPadding: PaddingValues,
) {
    val token = SessionManager.getToken()
    val role = SessionManager.getUserRole()

    val startDestination = when {
        token.isNullOrEmpty() -> Destinations.Welcome.route
        else -> when (role) {
            "Emprendedor" -> Destinations.Emprendedor.route
            "Usuario" -> Destinations.Usuario.route
            "Administrador" -> Destinations.Administrador.route
            else -> Destinations.Administrador.route
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        // Comunes
        composable(Destinations.Welcome.route) { WelcomeMain() }  // Se elimina el navController aquí
        composable(Destinations.Search.route) {
            SearchScreen(
                navController = navController,
                suggestions = listOf("Isla Taquile", "Isla Amantaní", "Lago Titicaca", "Puno", "Cusco")
            )
        }
        composable(Destinations.PerfilWelcome.route) { PerfilScreen(navController) }

        // Login y registro
        composable(Destinations.Login.route) {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(Destinations.Administrador.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                navigateToEmprendedorScreen = {
                    navController.navigate(Destinations.Emprendedor.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                navigateToUsuarioScreen = {
                    navController.navigate(Destinations.Usuario.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                navigateToAdministradorScreen = {
                    navController.navigate(Destinations.Administrador.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Destinations.Register.route)
                },
                navigateToForgotPasswordScreen = {
                    navController.navigate(Destinations.ForgotPassword.route)
                }
            )
        }

        composable(Destinations.Register.route) {
            RegisterScreen(
                onNavigateByRole = { role ->
                    val route = when (role) {
                        "Emprendedor" -> Destinations.Emprendedor.route
                        "Usuario" -> Destinations.Usuario.route
                        "Administrador" -> Destinations.Administrador.route
                        else -> Destinations.Administrador.route
                    }
                    navController.navigate(route) {
                        popUpTo(Destinations.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.ForgotPassword.route) {
            ForgotPasswordScreen(onBack = { navController.popBackStack() })
        }

        // Rutas por rol
        composable(Destinations.Emprendedor.route) { EmprendedorScreen(navController) }
        composable(Destinations.Usuario.route) { UsuarioScreen(navController) }
        composable(Destinations.Administrador.route) {
            AdministradorScreen(
                navController = navController,
                onLogoutClicked = {
                    TokenUtils.clearToken()
                    SessionManager.clearSession()
                    navController.navigate(Destinations.Welcome.route) {
                        popUpTo(Destinations.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        // Otros
        composable(Destinations.User.route) { UserScreen() }
        composable("ver_tipo_de_negocio_screen/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull()
            if (id != null) {
                VerTipoDeNegocioScreen(id = id)
            } else {
                Text("ID inválido")
            }
        }
    }
}

