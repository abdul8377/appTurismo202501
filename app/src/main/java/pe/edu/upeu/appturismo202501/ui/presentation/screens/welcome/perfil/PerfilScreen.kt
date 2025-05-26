package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsB.MonedaSelector
import pe.edu.upeu.appturismo202501.ui.presentation.componentsPerfil.SectionWithButtons
import pe.edu.upeu.appturismo202501.ui.presentation.screens.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navControllerGlobal: NavController,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    var showSheet by remember { mutableStateOf(false) }
    var showMonedaSelector by remember { mutableStateOf(false) }

    val loginSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val monedaSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val userState by viewModel.userState.collectAsState()

    // --- Login Modal ---
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = loginSheetState,
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
                    navControllerGlobal.navigate(Destinations.Welcome.route)
                },
                navToAdministrador = {
                    showSheet = false
                    navControllerGlobal.navigate(Destinations.Administrador.route)
                }
            )
        }
    }

    // --- Moneda Selector Modal ---
    if (showMonedaSelector) {
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

    Scaffold { padding ->
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

                when {
                    userState.isLoading -> {
                        CircularProgressIndicator()
                    }
                    userState.error != null -> {
                        Text(
                            text = "Error: ${userState.error}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    !userState.isLoggedIn -> {
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
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text("Iniciar sesión o registrarse")
                        }
                    }
                    else -> {
                        Text(
                            text = "Nombre: ${userState.name} ${userState.lastName}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Email: ${userState.email}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Rol: ${userState.role}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            if (userState.isLoggedIn && userState.name.isNotEmpty() && !userState.role.equals("Emprendedor", ignoreCase = true)) {
                item {
                    SectionWithButtons(
                        title = "¿Eres emprendedor?",
                        items = listOf(
                            Triple(
                                "Activa tu perfil como emprendedor",
                                "Publica tus negocios turísticos y llega a más personas"
                            ) {
                                navControllerGlobal.navigate(Destinations.Emprendedor.route)
                            }
                        ),
                        icons = listOf(Icons.Default.Star)
                    )
                }
            }

            item {
                val ajustesItems = mutableListOf(
                    Triple("Moneda", "EUR (€)") { showMonedaSelector = true },
                    Triple("Idioma", "Español") { },
                    Triple("Apariencia", "Configuración predeterminada del sistema") { },
                    Triple("Notificaciones", null) { }
                )
                val ajustesIcons = mutableListOf(
                    Icons.Default.AttachMoney,
                    Icons.Default.Language,
                    Icons.Default.ColorLens,
                    Icons.Default.Notifications
                )
                if (userState.isLoggedIn && userState.name.isNotEmpty()) {
                    ajustesItems.add(0, Triple("Perfil", null) { })
                    ajustesIcons.add(0, Icons.Default.AccountCircle)
                }

                SectionWithButtons(
                    title = "Ajustes",
                    items = ajustesItems,
                    icons = ajustesIcons
                )
            }

            item {
                SectionWithButtons(
                    title = "Ayuda",
                    items = listOf(
                        Triple("Sobre GetYourGuide", null) { },
                        Triple("Centro de ayuda", null) { },
                        Triple("Escríbenos", null) { }
                    ),
                    icons = listOf(Icons.Default.Info, Icons.Default.Help, Icons.Default.Email)
                )
            }

            item {
                SectionWithButtons(
                    title = "Comentarios",
                    items = listOf(
                        Triple("Comparte tu opinión", null) { },
                        Triple("Valora la aplicación", null) { }
                    ),
                    icons = listOf(Icons.Default.Share, Icons.Default.ThumbUp)
                )
            }

            if (userState.isLoggedIn && userState.name.isNotEmpty()) {
                item {
                    SectionWithButtons(
                        title = "Información legal",
                        items = listOf(
                            Triple("Términos y condiciones generales", null) { },
                            Triple("Información legal", null) { },
                            Triple("Privacidad", null) { },
                            Triple("Cerrar sesión", null) {
                                viewModel.logout()
                                navControllerGlobal.navigate(Destinations.Welcome.route) {
                                    popUpTo(Destinations.Welcome.route) { inclusive = true }
                                }
                            }
                        ),
                        icons = listOf(
                            Icons.Default.Description,
                            Icons.Default.Gavel,
                            Icons.Default.PrivacyTip,
                            Icons.Default.Logout
                        )
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}
