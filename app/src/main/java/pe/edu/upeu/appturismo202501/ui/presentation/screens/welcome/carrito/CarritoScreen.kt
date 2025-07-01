package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductItem
import pe.edu.upeu.appturismo202501.modelo.ProductoResp
import pe.edu.upeu.appturismo202501.modelo.CarritoRespUi

@Composable
fun CarritoScreen(
    carritoItems: List<CarritoRespUi>,  // Lista de items en el carrito
    onComprarClick: () -> Unit,  // Acción al hacer clic en el botón de comprar
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título de la pantalla
        Text(
            text = "Mi Carrito",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de productos en el carrito
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(carritoItems) { item ->
                ProductItem(
                    producto = item.producto!!,  // Usamos el producto de CarritoRespUi
                    cantidad = item.carritoResp.cantidad,  // Cantidad del producto en el carrito
                    isFavorite = false, // O manejar si el producto es favorito
                    onItemClick = { /* Acción para el producto */ },
                    onFavoriteClick = { /* Acción para el favorito */ },
                    onIncreaseQuantity = { /* Aumentar cantidad */ },
                    onDecreaseQuantity = { /* Disminuir cantidad */ },
                    onRemoveFromCart = { /* Eliminar producto del carrito */ }
                )
            }
        }

        // Botón de compra al final
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onComprarClick,
            shape = RoundedCornerShape(CornerSize(16.dp)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_shopping_cart_24), // Ícono del carrito de compras
                contentDescription = "Comprar",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Comprar Ahora",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCarritoScreen() {
    // Simulación de productos en el carrito
    val productos = listOf(
        CarritoRespUi(
            carritoResp = pe.edu.upeu.appturismo202501.modelo.CarritoResp(
                carritoId = 1,
                userId = 1,
                productosId = 1,
                serviciosId = null,
                cantidad = 2,
                precioUnitario = 19.99,
                subtotal = 39.98,
                totalCarrito = 39.98,
                estado = "en proceso",
                createdAt = "2025-06-30",
                updatedAt = "2025-06-30",
                stockDisponible = 100
            ),
            producto = ProductoResp(
                id = 1,
                emprendimientoId = 1,
                categoriaProductoId = 1,
                nombre = "Producto 1",
                descripcion = "Descripción del producto",
                precio = 19.99,
                stock = 100,
                estado = "activo",
                createdAt = "2025-06-30",
                updatedAt = "2025-06-30",
                imagenUrl = "https://via.placeholder.com/150",
                images = listOf()
            )
        ),
        CarritoRespUi(
            carritoResp = pe.edu.upeu.appturismo202501.modelo.CarritoResp(
                carritoId = 2,
                userId = 1,
                productosId = 2,
                serviciosId = null,
                cantidad = 1,
                precioUnitario = 29.99,
                subtotal = 29.99,
                totalCarrito = 29.99,
                estado = "en proceso",
                createdAt = "2025-06-30",
                updatedAt = "2025-06-30",
                stockDisponible = 50
            ),
            producto = ProductoResp(
                id = 2,
                emprendimientoId = 1,
                categoriaProductoId = 2,
                nombre = "Producto 2",
                descripcion = "Descripción del producto",
                precio = 29.99,
                stock = 50,
                estado = "activo",
                createdAt = "2025-06-30",
                updatedAt = "2025-06-30",
                imagenUrl = "https://via.placeholder.com/150",
                images = listOf()
            )
        )
    )

    CarritoScreen(
        carritoItems = productos,
        onComprarClick = { /* Acción de compra */ }
    )
}
