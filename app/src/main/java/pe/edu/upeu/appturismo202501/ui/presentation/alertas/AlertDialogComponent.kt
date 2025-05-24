package pe.edu.upeu.appturismo202501.ui.presentation.alertas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CheckCircle

@Composable
fun AlertDialogComponent(
    isOpen: MutableState<Boolean>,  // Controla si el diálogo está abierto
    title: String,                  // Título del diálogo
    message: String,                // Mensaje del diálogo
    onDismiss: () -> Unit,          // Acción cuando el diálogo se cierra
    onConfirm: () -> Unit,          // Acción cuando se confirma la alerta
    confirmText: String = "Confirmar",  // Texto del botón de confirmación
    dismissText: String = "Cancelar",  // Texto del botón de cancelación
    isSuccess: Boolean = true       // Determina si el mensaje es de éxito o error
) {
    if (isOpen.value) {
        AlertDialog(
            onDismissRequest = { isOpen.value = false },  // Cierra el diálogo al tocar fuera
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Icono de éxito o advertencia
                    Icon(
                        imageVector = if (isSuccess) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                        contentDescription = null,
                        tint = if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = title, color = MaterialTheme.colorScheme.onSurface)  // Usar color predeterminado del tema
                }
            },
            text = {
                Text(text = message, color = MaterialTheme.colorScheme.onSurface)  // Usar color predeterminado del tema
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirm()  // Acción del botón de confirmación
                        isOpen.value = false  // Cierra el diálogo
                    }
                ) {
                    Text(
                        text = confirmText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()  // Acción del botón de cancelación
                        isOpen.value = false  // Cierra el diálogo
                    }
                ) {
                    Text(
                        text = dismissText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            // Añadimos un espaciado adicional y un fondo más suave
            modifier = Modifier.padding(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        )
    }
}
