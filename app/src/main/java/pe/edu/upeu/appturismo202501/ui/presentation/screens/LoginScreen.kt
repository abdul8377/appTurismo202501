package pe.edu.upeu.appturismo202501.ui.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upeu.appturismo202501.modelo.LoginDto
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.LoginForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navToHome: () -> Unit,
    navToRegister: () -> Unit,
    navToForgotPassword: () -> Unit,
    navToEmprendedor: () -> Unit,
    navToUsuario: () -> Unit,           // Mantén, pero lo usaremos para PerfilScreen
    navToAdministrador: () -> Unit,
) {
    val emailExists by viewModel.emailExists.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    val userName by viewModel.userName.observeAsState("")
    val userRoles by viewModel.userRoles.observeAsState(emptyList())
    val userRole by viewModel.userRole.observeAsState()
    val isLogin by viewModel.islogin.observeAsState(false)

    val context = LocalContext.current

    // Navegación cuando login es exitoso
    LaunchedEffect(isLogin) {
        if (isLogin) {
            when (userRole) {
                "Emprendedor" -> navToEmprendedor()
                "Usuario" -> navToUsuario()   // Aquí navega a PerfilScreen (ruta perfilWelcome)
                "Administrador" -> navToAdministrador()
                else -> navToHome()
            }
        }
    }

    // Navegar a registro si email no existe
    LaunchedEffect(emailExists) {
        if (emailExists == false) {
            navToRegister()
        }
    }

    LoginForm(
        onCheckEmail = { email -> viewModel.checkEmail(email) },
        onLogin = { email, password -> viewModel.loginSys(LoginDto(email, password)) },
        isLoading = isLoading,
        emailExists = emailExists,
        errorMessage = errorMessage,
        userName = userName,
        userRoles = userRoles,
        onClearError = { viewModel.clearErrorMessage() },
        onForgotPassword = navToForgotPassword,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    )
}
