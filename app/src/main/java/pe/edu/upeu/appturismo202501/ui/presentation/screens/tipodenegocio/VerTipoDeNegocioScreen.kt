package pe.edu.upeu.appturismo202501.ui.presentation.screens.tipodenegocio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upeu.appturismo202501.modelo.Emprendimiento
import pe.edu.upeu.appturismo202501.modelo.TipoDeNegocio

@Composable
fun VerTipoDeNegocioScreen(
    id: Long,
    viewModel: TipoDeNegocioViewModel = hiltViewModel()
) {
    // Estado para los emprendimientos y el tipo de negocio
    val emprendimientos by viewModel.emprendimientos.collectAsState()
    val tiposDeNegocio by viewModel.tiposDeNegocio.collectAsState()

    // Buscamos el tipo de negocio que coincide con el id
    val tipoDeNegocio = tiposDeNegocio.firstOrNull { it.id == id }
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Llamar a la carga de datos cuando se pasa el ID
    LaunchedEffect(id) {
        viewModel.loadEmprendimientosByTipo(id)  // Cargar emprendimientos
        viewModel.loadTiposDeNegocio()  // Cargar el tipo de negocio
    }

    // Si el tipo de negocio existe
    tipoDeNegocio?.let { tipo ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Mostrar el nombre y la descripción del tipo de negocio
            Text(text = tipo.nombre, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = tipo.descripcion ?: "Sin descripción", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(24.dp))

            // Título para la lista de emprendimientos
            Text(text = "Emprendimientos vinculados", style = MaterialTheme.typography.titleMedium)

            // Indicador de carga
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            // Mostrar error si existe
            else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Ocurrió un error al cargar los emprendimientos.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            // Mostrar mensaje si no hay emprendimientos
            else if (emprendimientos.isEmpty()) {
                Text(text = "No hay emprendimientos vinculados.", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            // Mostrar la lista de emprendimientos si existen
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(emprendimientos) { emprendimiento ->
                        EmprendimientoItem(emprendimiento = emprendimiento)
                    }
                }
            }
        }
    } ?: run {
        // Si no se encuentra el tipo de negocio, mostrar mensaje
        Text(
            text = "Tipo de negocio no encontrado.",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun EmprendimientoItem(emprendimiento: Emprendimiento) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = emprendimiento.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = emprendimiento.descripcion ?: "Sin descripción", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Estado: ${emprendimiento.estado}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
