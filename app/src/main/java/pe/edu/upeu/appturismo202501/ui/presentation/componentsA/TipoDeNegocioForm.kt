package pe.edu.upeu.appturismo202501.ui.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import pe.edu.upeu.appturismo202501.modelo.TipoDeNegocio

@Composable
fun TipoDeNegocioForm(
    tipoDeNegocio: TipoDeNegocio? = null, // Si es null, es para agregar; si no, es para editar
    onSubmit: (TipoDeNegocio) -> Unit, // Función para enviar los datos
    onDismiss: () -> Unit // Función para cerrar el formulario
) {
    // Estados para los campos del formulario
    val nombre = remember { mutableStateOf(TextFieldValue(tipoDeNegocio?.nombre ?: "")) }
    val descripcion = remember { mutableStateOf(TextFieldValue(tipoDeNegocio?.descripcion ?: "")) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
            .shadow(4.dp, RoundedCornerShape(12.dp)) // Añadimos sombra para un efecto más profesional
            .padding(20.dp)
    ) {
        // Título
        Text(
            text = if (tipoDeNegocio == null) "Agregar Tipo de Negocio" else "Editar Tipo de Negocio",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo de nombre
        TextField(
            value = nombre.value,
            onValueChange = { nombre.value = it },
            label = { Text("Nombre del tipo de negocio") },
            placeholder = { Text("Ejemplo: Restaurante") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary
            ),
            shape = MaterialTheme.shapes.medium,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo de descripción
        TextField(
            value = descripcion.value,
            onValueChange = { descripcion.value = it },
            label = { Text("Descripción del tipo de negocio") },
            placeholder = { Text("Ejemplo: Restaurante especializado en comida italiana") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary
            ),
            shape = MaterialTheme.shapes.medium,
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Botones para cancelar o guardar
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Botón para cancelar
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancelar", color = Color.White)
            }

            // Botón para guardar
            Button(
                onClick = {
                    val newTipoDeNegocio = TipoDeNegocio(
                        id = tipoDeNegocio?.id ?: 0, // Si es agregar, el id puede ser 0
                        nombre = nombre.value.text,
                        descripcion = descripcion.value.text,
                        created_at = tipoDeNegocio?.created_at ?: "",
                        updated_at = "",
                        emprendimientos_count = 0
                    )
                    onSubmit(newTipoDeNegocio) // Enviar los datos
                    onDismiss() // Cerrar el formulario después de enviar
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Guardar", color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun PreviewTipoDeNegocioForm() {
    TipoDeNegocioForm(
        onSubmit = {},
        onDismiss = {}
    )
}
