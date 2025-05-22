package pe.edu.upeu.appturismo202501.ui.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.LoginDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    navigateToEmprendedorScreen: () -> Unit,
    navigateToUsuarioScreen: () -> Unit,
    navigateToAdministradorScreen: () -> Unit,
    onRegisterClick: () -> Unit,
    navigateToForgotPasswordScreen: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val emailExists by viewModel.emailExists.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    val userName by viewModel.userName.observeAsState("")
    val userRoles by viewModel.userRoles.observeAsState(emptyList())
    val userRole by viewModel.userRole.observeAsState()
    val isLogin by viewModel.islogin.observeAsState(false)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Mostrar snackbar de errores
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.clearErrorMessage()
            }
        }
    }

    // Navegar según rol cuando el login sea exitoso
    LaunchedEffect(isLogin) {
        if (isLogin) {
            when (userRole) {
                "Emprendedor" -> navigateToEmprendedorScreen()
                "Usuario" -> navigateToUsuarioScreen()
                "Administrador" -> navigateToAdministradorScreen()
                else -> navigateToHome()
            }
        }
    }

    // Navegar automáticamente a RegisterScreen si el email no existe
    LaunchedEffect(emailExists) {
        if (emailExists == false) {
            onRegisterClick()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Indicador tipo barra para modal deslizable
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 480.dp) // Altura máxima para que no ocupe toda la pantalla
                    .align(Alignment.BottomCenter), // Alineado abajo dentro del Box
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Bienvenido",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (errorMessage != null) viewModel.clearErrorMessage()
                        },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = emailExists != true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    when (emailExists) {
                        null -> {
                            Button(
                                onClick = {
                                    if (email.isNotBlank()) {
                                        viewModel.checkEmail(email.trim())
                                    } else {
                                        Toast.makeText(context, "Ingrese un correo válido", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text("Continuar")
                            }
                        }
                        true -> {
                            Text(
                                "Usuario encontrado. Ingresa tu contraseña.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            if (userName.isNotBlank()) {
                                Text(
                                    "Nombre: $userName",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }

                            if (userRoles.isNotEmpty()) {
                                Text(
                                    "Roles: ${userRoles.joinToString(", ")}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }

                            OutlinedTextField(
                                value = password,
                                onValueChange = {
                                    password = it
                                    if (errorMessage != null) viewModel.clearErrorMessage()
                                },
                                label = { Text("Contraseña") },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                singleLine = true,
                                shape = MaterialTheme.shapes.medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            TextButton(
                                onClick = {
                                    navigateToForgotPasswordScreen()
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(
                                    text = "¿Olvidaste tu contraseña?",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (password.isNotBlank()) {
                                        viewModel.loginSys(LoginDto(email.trim(), password))
                                    } else {
                                        Toast.makeText(context, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text("Ingresar")
                            }
                        }
                        false -> {
                            Text(
                                "Usuario no encontrado. Redirigiendo a registro...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }
                    }

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
