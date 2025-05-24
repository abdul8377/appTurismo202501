package pe.edu.upeu.appturismo202501.ui.presentation.alertas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay

@Composable
fun SnackbarNotification(
    message: String, // El mensaje que se mostrará
    isSuccess: Boolean, // Si es éxito o error
    durationMillis: Long = 3000, // Duración de la notificación en milisegundos
    onDismiss: () -> Unit // Acción cuando se cierra la notificación
) {
    // Estado para controlar la visibilidad del Snackbar
    var isVisible by remember { mutableStateOf(true) }

    // Usamos LaunchedEffect para controlar el tiempo de duración de la notificación
    LaunchedEffect(message) {
        delay(durationMillis) // Esperar el tiempo definido antes de ocultar el snackbar
        isVisible = false // Ocultar el snackbar
        onDismiss() // Llamar a la acción de cierre
    }

    // Solo mostrar el Snackbar si es visible
    if (isVisible) {
        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            contentColor = Color.White,
            containerColor = if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isSuccess) Icons.Filled.CheckCircle else Icons.Filled.Error,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = message,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        }
    }
}
