package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsB.MonedaSelector
import pe.edu.upeu.appturismo202501.ui.presentation.componentsPerfil.SectionWithButtons
import pe.edu.upeu.appturismo202501.ui.presentation.screens.LoginScreen
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navControllerGlobal: NavController,
    navControllerLocal: NavHostController,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    var showSheet by remember { mutableStateOf(false) }
    var showMonedaSelector by remember { mutableStateOf(false) }

    val loginSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val monedaSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val userState by viewModel.userState.collectAsState()

    // Detectar el error y esperar 2 segundos para recargar la pantalla
    LaunchedEffect(userState.error) {
        if (userState.error != null) {
            delay(2000) // Esperar 2 segundos
            navControllerGlobal.popBackStack(Destinations.Welcome.route, inclusive = false)
            navControllerGlobal.navigate(Destinations.Welcome.route)
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = loginSheetState,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {
            LoginScreen(
                navToHome = { showSheet = false; navControllerGlobal.navigate(Destinations.Welcome.route) },
                navToRegister = { showSheet = false; navControllerGlobal.navigate(Destinations.Register.route) },
                navToForgotPassword = { showSheet = false; navControllerGlobal.navigate(Destinations.ForgotPassword.route) },
                navToEmprendedor = { showSheet = false; navControllerGlobal.navigate(Destinations.Emprendedor.route) },
                navToUsuario = { showSheet = false; navControllerGlobal.navigate(Destinations.Welcome.route) },
                navToAdministrador = { showSheet = false; navControllerGlobal.navigate(Destinations.Administrador.route) }
            )
        }
    }

    if (showMonedaSelector) {
        ModalBottomSheet(
            onDismissRequest = { showMonedaSelector = false },
            sheetState = monedaSheetState,
            modifier = Modifier
                .fillMaxWidth()
                .height(700.dp)
        ) {
            MonedaSelector(onClose = { showMonedaSelector = false })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                actions = {
                    if (userState.isLoggedIn) {
                        IconButton(onClick = {
                            viewModel.logout()
                            navControllerGlobal.navigate(Destinations.Welcome.route) {
                                popUpTo(Destinations.Welcome.route) { inclusive = true }
                            }
                        }) {
                            Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                when {
                    userState.isLoading -> Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }

                    userState.error != null -> Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("⚠️ ${userState.error}", modifier = Modifier.padding(16.dp))
                    }

                    !userState.isLoggedIn -> Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AccountCircle, null, Modifier.size(72.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(16.dp))
                        Text("Accede a tu reserva desde cualquier dispositivo", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { showSheet = true }) {
                            Text("Iniciar sesión o registrarse")
                        }
                    }

                    else -> UserProfile(userState)
                }
            }

            if (userState.isLoggedIn && userState.role != "Emprendedor") {
                item {
                    SectionWithButtons(
                        title = "¿Eres emprendedor?",
                        items = listOf(
                            Triple("Activa tu perfil como emprendedor", "Publica tus negocios turísticos y llega a más personas") {
                                navControllerLocal.navigate("emprendimiento_create")
                            }
                        ),
                        icons = listOf(Icons.Default.Star)
                    )
                }
            }

            item {
                SectionWithButtons("Ajustes", listOf(
                    Triple("Moneda", "EUR (€)") { showMonedaSelector = true },
                    Triple("Idioma", "Español") {},
                    Triple("Apariencia", "Configuración predeterminada") {},
                    Triple("Notificaciones", null) {}
                ), icons = listOf(Icons.Default.AttachMoney, Icons.Default.Language, Icons.Default.ColorLens, Icons.Default.Notifications))
            }

            item {
                SectionWithButtons("Ayuda", listOf(
                    Triple("Sobre GetYourGuide", null) {},
                    Triple("Centro de ayuda", null) {},
                    Triple("Escríbenos", null) {}
                ), icons = listOf(Icons.Default.Info, Icons.Default.Help, Icons.Default.Email))
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun UserProfile(userState: UserState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Perfil",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${userState.name} ${userState.lastName}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = userState.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                ElevatedAssistChip(
                    onClick = {},
                    label = { Text(text = "Rol: ${userState.role}") },
                    colors = AssistChipDefaults.elevatedAssistChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(50)
                )
            }
        }
    }
}
