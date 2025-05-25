package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.presentation.componentsB.MonedaSelector
import pe.edu.upeu.appturismo202501.ui.presentation.screens.LoginScreen
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navControllerGlobal: NavController) {
    val token = SessionManager.getToken()
    val userId = SessionManager.getUserId() ?: "Desconocido"
    val userRole = SessionManager.getUserRole() ?: "Invitado"

    var showSheet by remember { mutableStateOf(false) }
    var showMonedaSelector by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    // Modal para Login
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            LoginScreen(
                navToHome = {
                    showSheet = false
                    navControllerGlobal.navigate(Destinations.Welcome.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
                navToRegister = {
                    showSheet = false
                    navControllerGlobal.navigate(Destinations.Register.route)
                },
                navToForgotPassword = {
                    showSheet = false
                    navControllerGlobal.navigate(Destinations.ForgotPassword.route)
                },
                navToEmprendedor = {
                    showSheet = false
                    navControllerGlobal.navigate(Destinations.Emprendedor.route)
                },
                navToUsuario = {
                    showSheet = false
                    navControllerGlobal.navigate(Destinations.Welcome.route)  // Navega a WelcomeMain para usuarios
                },
                navToAdministrador = {
                    showSheet = false
                    navControllerGlobal.navigate(Destinations.Administrador.route)
                }
            )
        }
    }

    // Modal para selector moneda
    if (showMonedaSelector) {
        val monedaSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            confirmValueChange = { it != SheetValue.Hidden }
        )

        ModalBottomSheet(
            onDismissRequest = { showMonedaSelector = false },
            sheetState = monedaSheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
        ) {
            MonedaSelector(onClose = { showMonedaSelector = false })
        }
    }

    Scaffold(
        bottomBar = {} // Sin barra inferior aquí
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (token.isNullOrEmpty()) {
                    // Sin sesión activa: mostrar invitación a iniciar sesión
                    Text(
                        text = "Accede a tu reserva desde cualquier dispositivo. Regístrate, sincroniza tus reservas, añade actividades a tus favoritos y guarda tus datos personales para reservar más rápidamente.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    Button(
                        onClick = { showSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("Iniciar sesión o registrarse")
                    }
                } else {
                    // Con sesión activa: mostrar datos del usuario
                    Text(
                        text = "ID: $userId",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Rol: $userRole",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    // Botón de Cerrar Sesión
                    Button(
                        onClick = {
                            // Limpiar sesión
                            SessionManager.clearSession()
                            navControllerGlobal.navigate(Destinations.Welcome.route) {
                                popUpTo(Destinations.Welcome.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text("Cerrar sesión")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Secciones adicionales
            sectionWithButtons("Ajustes", listOf(
                Triple("Moneda", "EUR (€)") { showMonedaSelector = true },
                Triple("Idioma", "Español") { /* Acción */ },
                Triple("Apariencia", "Configuración predeterminada del sistema") { /* Acción */ },
                Triple("Notificaciones", null) { /* Acción */ }
            ))

            sectionWithButtons("Ayuda", listOf(
                Triple("Sobre GetYourGuide", null) { /* Acción */ },
                Triple("Centro de ayuda", null) { /* Acción */ },
                Triple("Escríbenos", null) { /* Acción */ }
            ))

            sectionWithButtons("Comentarios", listOf(
                Triple("Comparte tu opinión", null) { /* Acción */ },
                Triple("Valora la aplicación", null) { /* Acción */ }
            ))

            sectionWithButtons("Información legal", listOf(
                Triple("Términos y condiciones generales", null) { /* Acción */ },
                Triple("Información legal", null) { /* Acción */ },
                Triple("Privacidad", null) { /* Acción */ }
            ))
        }
    }
}

fun LazyListScope.sectionWithButtons(
    title: String,
    items: List<Triple<String, String?, () -> Unit>>
) {
    item {
        Spacer(modifier = Modifier.height(12.dp))
        SectionTitle(title)
    }
    items(items) { (label, value, onClick) ->
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                value?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    val isDark = isSystemInDarkTheme()
    val background = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
    val textColor = if (isDark) Color.LightGray else Color.Gray

    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(color = textColor),
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
