package pe.edu.upeu.appturismo202501.ui.presentation.componentsC

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SeleccionarMetodoPago(
    onMetodoPagoSeleccionado: (String) -> Unit, // Acción cuando se selecciona un método de pago
    onCancelar: () -> Unit // Acción cuando el usuario cancela la selección
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título
        Text(
            text = "Selecciona un método de pago",
            style = MaterialTheme.typography.titleLarge // Cambiado a titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Botones para los métodos de pago
        Button(
            onClick = { onMetodoPagoSeleccionado("Tarjeta de Crédito") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Tarjeta de Crédito")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onMetodoPagoSeleccionado("PayPal") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "PayPal")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Cancelar
        TextButton(
            onClick = onCancelar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Cancelar")
        }
    }
}
