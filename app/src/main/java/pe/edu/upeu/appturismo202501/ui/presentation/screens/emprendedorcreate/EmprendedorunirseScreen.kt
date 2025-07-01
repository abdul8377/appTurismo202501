package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedorcreate

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext // Asegúrate de importar LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedorcreate.EmprendedorCreateViewModel

@Composable
fun EmprendedorUnirseScreen(
    viewModel: EmprendedorCreateViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    // Estados de los campos
    var codigoUnico by remember { mutableStateOf("") }
    var rolSolicitado by remember { mutableStateOf("") }

    // Estado de los mensajes de error y éxito
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    // Usamos LocalContext para obtener el contexto de la aplicación
    val context = LocalContext.current

    // Mostrar un mensaje de éxito o error cuando cambia el mensaje
    LaunchedEffect(message) {
        message?.let {
            // Mostrar el mensaje con un Toast
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    // UI de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título de la pantalla
        Text(
            text = "Unirse a Emprendimiento",
            style = MaterialTheme.typography.bodyLarge // Cambié a bodyLarge si h6 no se reconoce
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el código único
        TextField(
            value = codigoUnico,
            onValueChange = { codigoUnico = it },
            label = { Text("Código único del emprendimiento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para el rol solicitado
        TextField(
            value = rolSolicitado,
            onValueChange = { rolSolicitado = it },
            label = { Text("Rol solicitado (colaborador, propietario)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para enviar la solicitud
        Button(
            onClick = {
                // Llamar la función para enviar la solicitud
                viewModel.enviarSolicitud(codigoUnico, rolSolicitado)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading // Desactivar el botón si está cargando
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(text = "Enviar Solicitud")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Volver atrás
        Button(
            onClick = { onNavigateBack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Volver")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmprendedorUnirseScreenPreview() {
    EmprendedorUnirseScreen(
        onNavigateBack = { }
    )
}
