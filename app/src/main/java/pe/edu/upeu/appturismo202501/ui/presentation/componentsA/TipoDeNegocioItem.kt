package pe.edu.upeu.appturismo202501.ui.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import pe.edu.upeu.appturismo202501.modelo.TipoDeNegocio

@Composable
fun TipoDeNegocioItem(
    tipoDeNegocio: TipoDeNegocio,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onViewClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cambié h6 por titleLarge
            Text(text = tipoDeNegocio.nombre, style = MaterialTheme.typography.titleLarge)

            // Cambié body1 por bodyLarge
            Text(text = tipoDeNegocio.descripcion ?: "Sin descripción", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onViewClick) {
                    Icon(Icons.Filled.Visibility, contentDescription = "Ver", tint = Color.Gray)
                }

                IconButton(onClick = onEditClick) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = Color.Gray)
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                }
            }
        }
    }
}
