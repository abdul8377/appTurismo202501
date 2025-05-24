package pe.edu.upeu.appturismo202501.ui.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithHistory(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    searchHistory: List<String>, // Historial de búsqueda
    onHistoryItemClick: (String) -> Unit, // Acción cuando se selecciona un historial
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(modifier) {
        // Barra de búsqueda ajustada para solo ocupar el espacio necesario
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter) // Asegurando que la barra de búsqueda esté en la parte superior
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Buscar tipo de negocio") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Mostrar historial de búsqueda o resultados de búsqueda
            Column(Modifier.verticalScroll(rememberScrollState())) {
                if (expanded && searchHistory.isNotEmpty()) {
                    Text("Historial de búsqueda", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(8.dp))
                    searchHistory.forEach { historyItem ->
                        ListItem(
                            headlineContent = { Text(historyItem) },
                            modifier = Modifier
                                .clickable {
                                    onHistoryItemClick(historyItem)
                                    textFieldState.edit { replace(0, length, historyItem) }
                                    expanded = false
                                }
                                .fillMaxWidth()
                        )
                    }
                } else {
                    // Mostrar resultados de búsqueda
                    searchResults.forEach { result ->
                        ListItem(
                            headlineContent = { Text(result) },
                            modifier = Modifier
                                .clickable {
                                    textFieldState.edit { replace(0, length, result) }
                                    expanded = false
                                }
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchBarWithHistoryPreview() {
    val items = listOf("Restaurant", "Store", "Hotel", "Spa")
    val history = listOf("Restaurant", "Hotel")

    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(items) }

    val textFieldState = remember { mutableStateOf(TextFieldState()) }

    SearchBarWithHistory(
        textFieldState = textFieldState.value,
        onSearch = { query ->
            searchResults = items.filter { it.contains(query, ignoreCase = true) }
        },
        searchResults = searchResults,
        searchHistory = history,
        onHistoryItemClick = { historyItem -> /* Acción al seleccionar del historial */ }
    )
}
