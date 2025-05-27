package pe.edu.upeu.appturismo202501.ui.presentation.componentsC

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchToggleField(
    modifier: Modifier = Modifier, // ✅ aquí se agrega
    onQueryChanged: (String) -> Unit
) {
    var showSearch by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Column (modifier = modifier) { // ✅ se usa aquí también
        Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = {
                showSearch = !showSearch
                if (!showSearch) {
                    searchText = ""
                    onQueryChanged("")
                }
            }) {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            }
        }

        AnimatedVisibility(visible = showSearch) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    onQueryChanged(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                placeholder = { Text("Buscar por nombre o correo") },
                singleLine = true
            )
        }
    }
}
