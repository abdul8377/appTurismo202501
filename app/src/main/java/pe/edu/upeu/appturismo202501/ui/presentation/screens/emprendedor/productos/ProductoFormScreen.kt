package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.productos

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import pe.edu.upeu.appturismo202501.modelo.ProductResp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoFormScreen(
    viewModel: EmprendedorProductoViewModel = hiltViewModel(),
    producto: ProductResp?,                // null = crear, no-null = editar
    onDone: () -> Unit,                    // volver atrás tras guardar
    onCancel: () -> Unit                   // cancelar
) {
    var nombre by remember { mutableStateOf(producto?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(producto?.descripcion ?: "") }
    var precioText by remember { mutableStateOf(producto?.precio?.toString() ?: "") }
    var stockText by remember { mutableStateOf(producto?.stock?.toString() ?: "") }

    // Estado local del formulario
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (producto == null) "Nuevo Producto" else "Editar Producto")
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    // Validaciones básicas
                    val precio = precioText.toDoubleOrNull() ?: run {
                        errorMsg = "Precio inválido"
                        return@Button
                    }
                    val stock = stockText.toIntOrNull() ?: run {
                        errorMsg = "Stock inválido"
                        return@Button
                    }

                    // Limpiamos estado
                    isSubmitting = true
                    errorMsg = null

                    // Llamamos al ViewModel
                    if (producto == null) {
                        viewModel.crearProducto(
                            nombre         = nombre,
                            descripcion    = descripcion,
                            precio         = precio,
                            stock          = stock,
                            imagenFile     = null // TODO: reemplaza por tu File de la imagen
                        ) { success ->
                            isSubmitting = false
                            if (success) onDone() else errorMsg = "Error al crear"
                        }
                    } else {
                        viewModel.actualizarProducto(
                            id             = producto.id,
                            nombre         = nombre,
                            descripcion    = descripcion,
                            precio         = precio,
                            stock          = stock,
                            imagenFile     = null // TODO: reemplaza por tu File de la nueva imagen
                        ) { success ->
                            isSubmitting = false
                            if (success) onDone() else errorMsg = "Error al actualizar"
                        }
                    }
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val label = if (producto == null) "Crear" else "Guardar"
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(label)
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isSubmitting) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            errorMsg?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            OutlinedTextField(
                value = precioText,
                onValueChange = { precioText = it },
                label = { Text("Precio") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = stockText,
                onValueChange = { stockText = it },
                label = { Text("Stock") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // TODO: aquí podrías integrar un ImagePicker para que el usuario seleccione
            // una foto y rellenar `imagenFile` en el llamado a viewModel.
        }
    }
}