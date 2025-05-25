package pe.edu.upeu.appturismo202501.ui.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pe.edu.upeu.appturismo202501.ui.presentation.screens.LoginScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.forgotpassword.ForgotPasswordScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.SearchScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador.AdministradorScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.EmprendedorScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.register.RegisterScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.tipodenegocio.VerTipoDeNegocioScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.user.UserScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.main.WelcomeMain
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

    // Siempre va a WelcomeMain si hay token, sino igual WelcomeMain (que tendrá login dentro si no hay sesión)
    val startDestination = Destinations.Welcome.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        // Pantalla principal con pestañas (incluye perfil)
        composable(Destinations.Welcome.route) {
            WelcomeMain(navControllerGlobal = navController)
        }

        composable(Destinations.Search.route) {
            SearchScreen(
                navController = navController,
                suggestions = listOf("Isla Taquile", "Isla Amantaní", "Lago Titicaca", "Puno", "Cusco")
            )
        }

        // Login y registro
        composable(Destinations.Login.route) {
            LoginScreen(
                navToHome = {
                    navController.navigate(Destinations.Welcome.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                navToEmprendedor = {
                    navController.navigate(Destinations.Emprendedor.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                navToUsuario = {
                    navController.navigate(Destinations.Welcome.route) {  // Va a WelcomeMain para usuario
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                navToAdministrador = {
                    navController.navigate(Destinations.Administrador.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                navToRegister = {
                    navController.navigate(Destinations.Register.route)
                },
                navToForgotPassword = {
                    navController.navigate(Destinations.ForgotPassword.route)
                }
            )
        }

        composable(Destinations.Register.route) {
            RegisterScreen(
                onNavigateByRole = { role ->
                    navController.navigate(Destinations.Welcome.route) {
                        popUpTo(Destinations.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.ForgotPassword.route) {
            ForgotPasswordScreen(onBack = { navController.popBackStack() })
        }

        // Rutas por rol sin PerfilScreen independiente
        composable(Destinations.Emprendedor.route) {
            EmprendedorScreen(
                navController = navController,
                onLogoutClicked = {
                    TokenUtils.clearToken()
                    SessionManager.clearSession()
                    navController.navigate(Destinations.Welcome.route) {
                        popUpTo(Destinations.Welcome.route)
                    }
                }
            )
        }

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
        composable(Destinations.User.route) {
            UserScreen()
        }

        // Detalle tipo de negocio
        composable(
            route = Destinations.VerTipoDeNegocio.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            VerTipoDeNegocioScreen(id = id)
        }
    }
}
