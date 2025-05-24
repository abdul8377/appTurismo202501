package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.screens.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController) {
    // Estado para el ModalBottomSheet de inicio de sesión
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    // Abrir el ModalSheet en el primer renderizado
    LaunchedEffect(Unit) {
        sheetState.show()
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxHeight()
        ) {
            LoginScreen(
                navigateToHome = {
                    showSheet = false
                    navController.navigate(Destinations.Welcome.route)
                },
                navigateToEmprendedorScreen = {
                    showSheet = false
                    navController.navigate(Destinations.Emprendedor.route)
                },
                navigateToUsuarioScreen = {
                    showSheet = false
                    navController.navigate(Destinations.Usuario.route)
                },
                navigateToAdministradorScreen = {
                    showSheet = false
                    navController.navigate(Destinations.Administrador.route)
                },
                onRegisterClick = {
                    showSheet = false
                    navController.navigate(Destinations.Register.route)
                },
                navigateToForgotPasswordScreen = {
                    showSheet = false
                    navController.navigate(Destinations.ForgotPassword.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp)
            )
        }
    }

    // Pantalla principal del perfil
    Scaffold(
        bottomBar = {} // Se elimina la barra de navegación para el perfil
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item {
                // Título del perfil
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Descripción del perfil
                Text(
                    text = "Accede a tu reserva desde cualquier dispositivo. Regístrate, sincroniza tus reservas, añade actividades a tus favoritos y guarda tus datos personales para reservar más rápidamente.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Botón para iniciar sesión o registrarse
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

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Sección de Ajustes
            section("Ajustes", listOf(
                "Moneda" to "EUR (€)",
                "Idioma" to "Español",
                "Apariencia" to "Configuración predeterminada del sistema",
                "Notificaciones" to null
            ))

            // Sección de Ayuda
            section("Ayuda", listOf(
                "Sobre GetYourGuide" to null,
                "Centro de ayuda" to null,
                "Escríbenos" to null
            ))

            // Sección de Comentarios
            section("Comentarios", listOf(
                "Comparte tu opinión" to null,
                "Valora la aplicación" to null
            ))

            // Sección de Información legal
            section("Información legal", listOf(
                "Términos y condiciones generales" to null,
                "Información legal" to null,
                "Privacidad" to null
            ))
        }
    }
}

// Función reutilizable para crear secciones
fun LazyListScope.section(title: String, items: List<Pair<String, String?>>) {
    item {
        Spacer(modifier = Modifier.height(12.dp))
        SectionTitle(title)
    }
    items(items) { (label, value) ->
        ItemRow(label = label, value = value)
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

@Composable
fun ItemRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
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
        Icon(
            imageVector = Icons.Outlined.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
        )
    }
}
