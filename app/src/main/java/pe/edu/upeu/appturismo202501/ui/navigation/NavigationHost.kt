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
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedorcreate.EmprendedorCreateScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.register.RegisterScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.tipodenegocio.VerTipoDeNegocioScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.user.UserScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.ExplorarScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.AlojamientoDetailWrapper
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.main.WelcomeMain
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.utils.TokenUtils


@Composable
fun NavigationHost(
    navController: NavHostController,
    darkMode: MutableState<Boolean>,
    innerPadding: PaddingValues,
) {
    val token = SessionManager.getToken()
    val role = SessionManager.getUserRole()

    // ✅ Redirige dinámicamente según el rol si hay token
    val startDestination = when {
        token.isNullOrEmpty() -> Destinations.Welcome.route
        role.equals("ADMINISTRADOR", ignoreCase = true) -> Destinations.Administrador.route
        role.equals("EMPRENDEDOR", ignoreCase = true) -> Destinations.Emprendedor.route
        else -> Destinations.Welcome.route
    }


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        // Pantalla principal con pestañas (usuario normal)
        composable(Destinations.Welcome.route) {
            WelcomeMain(navControllerGlobal = navController)
        }
        composable(Destinations.Search.route) {
            SearchScreen(
                navController = navController,
                suggestions   = listOf("Isla Taquile", "Amantaní", "Titicaca")
            )
        }


        // Login
        composable(Destinations.Login.route) {
            LoginScreen(
                navToHome = {
                    navController.navigate(Destinations.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navToEmprendedor = {
                    navController.navigate(Destinations.Emprendedor.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navToUsuario = {
                    navController.navigate(Destinations.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navToAdministrador = {
                    navController.navigate(Destinations.Administrador.route) {
                        popUpTo(0) { inclusive = true }
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
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinations.ForgotPassword.route) {
            ForgotPasswordScreen(onBack = { navController.popBackStack() })
        }

        // Pantalla Emprendedor
        composable(Destinations.Emprendedor.route) {
            EmprendedorScreen(
                navController = navController,
                onLogoutClicked = {
                    TokenUtils.clearToken()
                    SessionManager.clearSession()
                    navController.navigate(Destinations.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla Administrador
        composable(Destinations.Administrador.route) {
            AdministradorScreen(
                navController = navController,
                onLogoutClicked = {
                    TokenUtils.clearToken()
                    SessionManager.clearSession()
                    navController.navigate(Destinations.Welcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla User básica
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

        composable(Destinations.Create_Emprendimiento.route) {
            EmprendedorCreateScreen(navController = navController)
        }




        composable(
            route = "explorar/alojamiento/alojamientoDetalle/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val alojamientoId = backStackEntry.arguments?.getLong("id") ?: 0L
            AlojamientoDetailWrapper(alojamientoId, navController)
        }
    }
}
