package pe.edu.upeu.appturismo202501.ui.presentation.componentsB

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.appturismo202501.modelo.TipoDeNegocio

@Composable
fun EmprendimientoForm(
    nombre: String,
    onNombreChange: (String) -> Unit,
    descripcion: String,
    onDescripcionChange: (String) -> Unit,
    tipoNegocioId: String,
    onTipoNegocioIdChange: (String) -> Unit,
    direccion: String,
    onDireccionChange: (String) -> Unit,
    telefono: String,
    onTelefonoChange: (String) -> Unit,
    tiposDeNegocio: List<TipoDeNegocio>,  // Nuevos parámetros
    isLoading: Boolean
) {
    var selectedTipoDeNegocio by remember { mutableStateOf<TipoDeNegocio?>(null) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre campos

        // Campo Descripción
        OutlinedTextField(
            value = descripcion,
            onValueChange = onDescripcionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Selector de Tipo de Negocio (Usando Chips)
        Text("Tipo de Negocio", style = MaterialTheme.typography.bodyLarge)

        // Mostrar los tipos de negocio como Chips
        Row  {
            tiposDeNegocio.forEach { tipoDeNegocio ->
                AssistChip(
                    label = { Text(tipoDeNegocio.nombre) },
                    onClick = {
                        selectedTipoDeNegocio = tipoDeNegocio
                        onTipoNegocioIdChange(tipoDeNegocio.id.toString()) // Actualizamos el id
                    },
                    modifier = Modifier.padding(4.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selectedTipoDeNegocio == tipoDeNegocio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        labelColor = if (selectedTipoDeNegocio == tipoDeNegocio) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Dirección
        OutlinedTextField(
            value = direccion,
            onValueChange = onDireccionChange,
            label = { Text("Dirección") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Teléfono
        OutlinedTextField(
            value = telefono,
            onValueChange = onTelefonoChange,
            label = { Text("Teléfono") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Indicador de carga
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
