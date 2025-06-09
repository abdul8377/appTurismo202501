package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedorcreate

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import pe.edu.upeu.appturismo202501.ui.presentation.componentsB.EmprendimientoForm

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun EmprendedorCreateScreen(
    navController: NavHostController,
    viewModel: EmprendedorCreateViewModel = hiltViewModel()
){
    val nombre by viewModel.nombre.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()
    val tipoNegocioId by viewModel.tipoNegocioId.collectAsState()
    val direccion by viewModel.direccion.collectAsState()
    val telefono by viewModel.telefono.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val creationSuccess by viewModel.creationSuccess.collectAsState()

    // Navegar cuando la creación sea exitosa
    LaunchedEffect(creationSuccess) {
        if (creationSuccess) {
            navController.navigate("perfil") {
                popUpTo("emprendimiento_create") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Fila con el título y botón "X"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Crear Emprendimiento", style = MaterialTheme.typography.headlineMedium)
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Cerrar"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        EmprendimientoForm(
            nombre = nombre,
            onNombreChange = viewModel::onNombreChange,
            descripcion = descripcion,
            onDescripcionChange = viewModel::onDescripcionChange,
            tipoNegocioId = tipoNegocioId,
            onTipoNegocioIdChange = viewModel::onTipoNegocioIdChange,
            direccion = direccion,
            onDireccionChange = viewModel::onDireccionChange,
            telefono = telefono,
            onTelefonoChange = viewModel::onTelefonoChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.crearEmprendimiento() },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Crear")
            }
        }

        message?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}

