package pe.edu.upeu.appturismo202501.ui.presentation.componentsC

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SubtotalCompra(
    subtotales: List<Double>,  // Lista de subtotales de los productos en el carrito
    onComprarClick: () -> Unit // Acción a ejecutar cuando se hace click en el botón "Comprar"
) {
    // Sumar los subtotales para calcular el total
    val total = subtotales.sum()

    // Formatear el total a dos decimales
    val totalFormatted = "%.2f".format(total)

    // Mostrar el componente
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Subtotal
        Text(
            text = "$totalFormatted AED", // Mostrar el total formateado con moneda
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Botón de compra
        Button(
            onClick = onComprarClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Tramitar pedido", style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onPrimary))
        }
    }
}
