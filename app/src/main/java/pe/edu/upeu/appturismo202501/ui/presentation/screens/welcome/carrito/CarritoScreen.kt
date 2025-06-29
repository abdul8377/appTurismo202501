@file:OptIn(ExperimentalMaterial3Api::class)

package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import pe.edu.upeu.appturismo202501.modelo.CarritoRespUi

@Composable
fun CarritoScreen(
    viewModel: CarritoViewModel = hiltViewModel()
) {
    val carritoItems by viewModel.carritoItemsUi.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.cargarCarrito()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (carritoItems.isEmpty()) {
                Text(
                    "Tu carrito está vacío",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 32.dp)
                )
            } else {
                CarritoList(carritoItems)
            }
        }
    }
}

@Composable
fun CarritoList(carritoItems: List<CarritoRespUi>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(carritoItems) { item ->
            CarritoItemCard(item)
        }
    }
}

@Composable
fun CarritoItemCard(
    itemUi: CarritoRespUi,
    viewModel: CarritoViewModel = hiltViewModel()
) {
    val item = itemUi.carritoResp
    val nombre = itemUi.producto?.nombre ?: itemUi.servicio?.nombre ?: "Sin nombre"
    val imagenUrl = itemUi.producto?.imagenUrl ?: itemUi.servicio?.imagenUrl
    val descripcion = itemUi.producto?.descripcion ?: itemUi.servicio?.descripcion ?: "Sin descripción"
    val stockDisponible = itemUi.producto?.stock ?: itemUi.servicio?.capacidadMaxima ?: Int.MAX_VALUE

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    viewModel.actualizarCantidad(item, item.cantidad - 1, stockDisponible)
                }) {
                    Text("-", style = MaterialTheme.typography.titleLarge)
                }
                Text(
                    text = "${item.cantidad}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = {
                    viewModel.actualizarCantidad(item, item.cantidad + 1, stockDisponible)
                }) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    viewModel.eliminarDelCarrito(item.carritoId)
                }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Precio Unitario: S/ ${item.precioUnitario}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Subtotal: S/ ${item.subtotal}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Stock disponible: $stockDisponible",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
