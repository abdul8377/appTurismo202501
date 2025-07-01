package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedorcreate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.data.remote.SolicitudEmprendimientoRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnirseEmprendimientoScreen(
    navController: NavController,
    viewModel: EmprendedorCreateViewModel = hiltViewModel()
) {
    // Estado para el código único
    var codigoUnico by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Validación del código único (solo permite letras y números en mayúsculas)
    val isCodeValid = codigoUnico.length == 6

    // Función para filtrar los caracteres permitidos (letras y números en mayúsculas)
    val filterInput: (String) -> String = { input ->
        input.toUpperCase().filter { it.isLetterOrDigit() }
    }

    // Observar el mensaje del ViewModel
    val message by viewModel.message.collectAsState()

    // Diseño de la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Unirse al emprendimiento", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Introduce el código único de 6 dígitos del emprendimiento",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 18.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campo de entrada para el código único
                OutlinedTextField(
                    value = codigoUnico,
                    onValueChange = { codigoUnico = filterInput(it).take(6) }, // Limita a 6 caracteres
                    label = { Text("Código único de 6 dígitos") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isCodeValid && codigoUnico.isNotEmpty(), // Validación
                    singleLine = true
                )

                // Mensaje de error si el código es inválido
                if (!isCodeValid && codigoUnico.isNotEmpty()) {
                    Text(
                        text = "El código debe ser de 6 caracteres alfanuméricos en mayúsculas.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enviar solicitud solo si el código es válido
                Button(
                    onClick = {
                        if (isCodeValid) {
                            // Crear la solicitud con rol 'colaborador' por defecto
                            val solicitud = SolicitudEmprendimientoRequest(
                                codigo_unico = codigoUnico,
                                rol_solicitado = "colaborador" // el rol por defecto es 'colaborador'
                            )

                            // Llamar al ViewModel para enviar la solicitud
                            viewModel.enviarSolicitud(solicitud)
                            isLoading = true
                        } else {
                            errorMessage = "Por favor ingresa un código válido."
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isCodeValid && !isLoading
                ) {
                    Text("Enviar Solicitud", fontWeight = FontWeight.Bold)
                }

                // Indicador de carga
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                }

                // Mostrar mensaje de éxito si la solicitud fue enviada correctamente
                message?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                // Mensaje de error si la solicitud no fue exitosa
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    )
}