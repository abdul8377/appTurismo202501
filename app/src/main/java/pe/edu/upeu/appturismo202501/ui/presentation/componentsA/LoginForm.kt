package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    onCheckEmail: (String) -> Unit,
    onLogin: (String, String) -> Unit,
    onRegister: (String, String, String) -> Unit, // Acción para registro
    isLoading: Boolean = false,
    emailExists: Boolean? = null,
    errorMessage: String? = null,
    userName: String = "",
    userRoles: List<String> = emptyList(),
    onClearError: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    isLogin: Boolean = true // Condición para decidir si es Login o Registro
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    // Mostrar snackbar de error
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                onClearError()
            }
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (isLogin) "Bienvenido" else "Registro",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (errorMessage != null) onClearError()
                },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                singleLine = true,
                enabled = emailExists != true || !isLogin,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (emailExists) {
                null -> {
                    Button(
                        onClick = {
                            if (email.isNotBlank()) {
                                onCheckEmail(email.trim())
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
                    // Saludo según rol (solo si es login)
                    if (isLogin) {
                        val rolSaludo = when {
                            "Administrador" in userRoles -> "Administrador"
                            "Emprendedor" in userRoles -> "Emprendedor"
                            "Usuario" in userRoles -> "Usuario"
                            else -> ""
                        }

                        Text(
                            text = if (rolSaludo.isNotEmpty())
                                "Bienvenido $rolSaludo, qué bueno verte de vuelta"
                            else
                                "Bienvenido",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (userName.isNotBlank()) {
                            Text("Nombre: $userName", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    // Mostrar campos de contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (errorMessage != null) onClearError()
                        },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imageVector = image, contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium
                    )

                    if (!isLogin) {
                        // Campo de confirmación de contraseña (solo para registro)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = passwordConfirm,
                            onValueChange = {
                                passwordConfirm = it
                                if (errorMessage != null) onClearError()
                            },
                            label = { Text("Confirmar Contraseña") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isLogin) {
                        // Botón de login
                        Button(
                            onClick = {
                                if (password.isNotBlank()) {
                                    onLogin(email.trim(), password)
                                } else {
                                    Toast.makeText(context, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Ingresar")
                        }
                    } else {
                        // Botón de registro
                        Button(
                            onClick = {
                                if (password.isNotBlank() && password == passwordConfirm) {
                                    onRegister(email.trim(), password, passwordConfirm)
                                } else {
                                    Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Registrar")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = onForgotPassword,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("¿Olvidaste tu contraseña?", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                }

                false -> {
                    Text(
                        text = "Usuario no encontrado. Redirigiendo a registro...",
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

            SnackbarHost(hostState = snackbarHostState)
        }
    }
}
