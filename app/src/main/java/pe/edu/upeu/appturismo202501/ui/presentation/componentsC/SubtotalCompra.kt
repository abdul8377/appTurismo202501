package pe.edu.upeu.appturismo202501.ui.presentation.componentsC

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubtotalCompra(
    subtotales: List<Double>,  // Lista de subtotales de los productos en el carrito
    onComprarClick: () -> Unit // Acción a ejecutar cuando se hace click en el botón "Comprar"
) {
    val totalCompra = subtotales.sum()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mostrar el total de la compra
        Text(
            text = "Total: S/ ${"%.2f".format(totalCompra)}",
            style = MaterialTheme.typography.headlineLarge // Cambiado a headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón "Comprar"
        Button(
            onClick = onComprarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Comprar")
        }
    }
}
