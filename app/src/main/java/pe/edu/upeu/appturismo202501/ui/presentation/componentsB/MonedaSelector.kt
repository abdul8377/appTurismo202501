package pe.edu.upeu.appturismo202501.ui.presentation.componentsB

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)  // Añadir esta anotación para aceptar el uso de APIs experimentales
@Composable
fun MonedaSelector(onClose: () -> Unit) {
    // Estado para almacenar la moneda seleccionada
    var selectedCurrency by remember { mutableStateOf("EUR (€)") }

    // Lista de monedas disponibles
    val currencies = listOf("EUR (€)", "USD ($)", "GBP (£)", "MXN (₱)", "Dólar americano", "Baht tailandés", "Corona checa", "Corona danesa", "Corona noruega", "Corona sueca", "Dirham EAU", "Dirham marroquí", "Dólar australiano")

    // Estado para el Scroll
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize()) {
        // TopBar con el botón de cierre
        TopAppBar(
            title = {
                Text("Moneda", style = MaterialTheme.typography.headlineMedium)
            },
            navigationIcon = {
                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Contenido del selector de moneda
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Selecciona tu moneda",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Crear un RadioButton para cada moneda
            currencies.forEach { currency ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = selectedCurrency == currency,
                        onClick = { selectedCurrency = currency },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = currency,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Mostrar la moneda seleccionada
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Moneda seleccionada: $selectedCurrency",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen() {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)  // Hacer que el modal no se expanda
    val scope = rememberCoroutineScope()
    var showMonedaSelector by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Show bottom sheet") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = {
                    showMonedaSelector = true
                }
            )
        }
    ) { contentPadding ->
        // Si se debe mostrar el ModalBottomSheet, lo mostramos
        if (showMonedaSelector) {
            ModalBottomSheet(
                onDismissRequest = { showMonedaSelector = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)  // Limitar la altura
                    .padding(bottom = 0.dp)  // Eliminar el espaciado inferior
            ) {
                MonedaSelector(onClose = { showMonedaSelector = false })
            }
        }
    }
}

@Preview
@Composable
fun PreviewMonedaSelector() {
    MonedaSelector(onClose = { /* Handle Close */ })
}
