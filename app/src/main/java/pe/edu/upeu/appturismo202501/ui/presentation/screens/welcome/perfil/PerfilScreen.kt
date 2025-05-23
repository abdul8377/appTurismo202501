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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsB.MonedaSelector
import pe.edu.upeu.appturismo202501.ui.presentation.screens.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController) {
    // Estado para el ModalBottomSheet de inicio de sesión
    var showSheet by remember { mutableStateOf(false) }
    var showMonedaSelector by remember { mutableStateOf(false) }  // Control del modal para seleccionar moneda
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
            // Aquí puedes colocar el LoginScreen u otro contenido según necesites
            Text(text = "Iniciar sesión aquí")
        }
    }

    if (showMonedaSelector) {
        ModalBottomSheet(
            onDismissRequest = { showMonedaSelector = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()  // Modal ocupa todo el ancho
                .height(700.dp)  // Fijar la altura para que sea fija y no cambie
        ) {
            MonedaSelector(onClose = { showMonedaSelector = false })
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
            sectionWithButtons("Ajustes", listOf(
                Triple("Moneda", "EUR (€)") { showMonedaSelector = true }, // Al presionar, abre el selector de moneda
                Triple("Idioma", "Español") { /* Acción para Idioma */ },
                Triple("Apariencia", "Configuración predeterminada del sistema") { /* Acción para Apariencia */ },
                Triple("Notificaciones", null) { /* Acción para Notificaciones */ }
            ))

            // Sección de Ayuda
            sectionWithButtons("Ayuda", listOf(
                Triple("Sobre GetYourGuide", null) { /* Acción para Sobre GetYourGuide */ },
                Triple("Centro de ayuda", null) { /* Acción para Centro de ayuda */ },
                Triple("Escríbenos", null) { /* Acción para Escríbenos */ }
            ))

            // Sección de Comentarios
            sectionWithButtons("Comentarios", listOf(
                Triple("Comparte tu opinión", null) { /* Acción para Comparte tu opinión */ },
                Triple("Valora la aplicación", null) { /* Acción para Valora la aplicación */ }
            ))

            // Sección de Información legal
            sectionWithButtons("Información legal", listOf(
                Triple("Términos y condiciones generales", null) { /* Acción para Términos y condiciones generales */ },
                Triple("Información legal", null) { /* Acción para Información legal */ },
                Triple("Privacidad", null) { /* Acción para Privacidad */ }
            ))
        }
    }
}

// Función reutilizable para crear secciones con botones
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
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
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
