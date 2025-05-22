package pe.edu.upeu.appturismo202501.ui.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.edu.upeu.appturismo202501.ui.presentation.Pantalla1
import pe.edu.upeu.appturismo202501.ui.presentation.screens.LoginScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.forgotpassword.ForgotPasswordScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.PerfilScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.SearchScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.WelcomeScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador.AdministradorScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.EmprendedorScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.register.RegisterScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.usuario.UsuarioScreen
import pe.edu.upeu.appturismo202501.utils.TokenUtils

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun NavigationHost(
    navController: NavHostController,
    darkMode: MutableState<Boolean>,
    innerPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Welcome.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        // ---------- PANTALLAS PRINCIPALES ----------
        composable(Destinations.Welcome.route) {
            WelcomeScreen(navController)
        }

        composable(Destinations.Search.route) {
            SearchScreen(
                navController = navController,
                suggestions = listOf(
                    "Isla Taquile", "Isla Amantaní",
                    "Lago Titicaca", "Puno", "Cusco"
                )
            )
        }

        composable(Destinations.PerfilWelcome.route) {
            PerfilScreen(navController = navController)
        }

        composable(Destinations.Login.route) {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(Destinations.Pantalla1.route) {
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

        // Ruta Forgot Password
        composable(Destinations.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ---------- RUTAS POR ROL ----------
        composable(Destinations.Emprendedor.route) {
            EmprendedorScreen(navController)
        }

        composable(Destinations.Usuario.route) {
            UsuarioScreen(navController)
        }

        composable(Destinations.Administrador.route) {
            AdministradorScreen(
                navController = navController,
                onLogoutClicked = {
                    TokenUtils.clearToken()
                    navController.navigate(Destinations.Login.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.Register.route) {
            RegisterScreen(
                onNavigateByRole = { role ->
                    when (role) {
                        "Emprendedor" -> navController.navigate(Destinations.Emprendedor.route) {
                            popUpTo(Destinations.Register.route) { inclusive = true }
                        }
                        "Usuario" -> navController.navigate(Destinations.Usuario.route) {
                            popUpTo(Destinations.Register.route) { inclusive = true }
                        }
                        "Administrador" -> navController.navigate(Destinations.Administrador.route) {
                            popUpTo(Destinations.Register.route) { inclusive = true }
                        }
                        else -> navController.navigate(Destinations.Pantalla1.route) {
                            popUpTo(Destinations.Register.route) { inclusive = true }
                        }
                    }
                }
            )
        }



    }
}
