package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.modelo.CarritoDto
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.DetalleProducto
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito.CarritoViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.ProductoViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito.FavoritosViewModel
import pe.edu.upeu.appturismo202501.utils.SessionManager

@Composable
fun ProductoDetailWrapper(
    productoId: Long,
    navController: NavController,
    viewModel: ProductoViewModel = hiltViewModel(),
    favViewModel: FavoritosViewModel = hiltViewModel(),
    carritoViewModel: CarritoViewModel = hiltViewModel() // ← Agrega esta línea
) {
    LaunchedEffect(productoId) {
        viewModel.fetchProductoDetalle(productoId)
    }

    val productoDetalle by viewModel.productoDetalle.collectAsState()
    val favUiState by favViewModel.uiState.collectAsState()

    productoDetalle?.let { detalle ->
        val isFavorite = detalle.producto.id in favUiState.productosFav.map { it.id }

        DetalleProducto(
            producto = detalle.producto,
            isFavorite = isFavorite,
            isLoggedIn = favUiState.isLoggedIn,
            onBackClick = { navController.popBackStack() },
            onFavoriteClick = {
                if (isFavorite) {
                    favViewModel.eliminarProductoFavorito(detalle.producto.id)
                } else {
                    favViewModel.agregarProductoFavoritoConDelay(detalle.producto.id)
                }
            },
            onAddToCartClick = {
                val carritoDto = CarritoDto(
                    userId = SessionManager.getUserId().toLong(),
                    productosId = detalle.producto.id,
                    cantidad = 1,
                    precioUnitario = detalle.producto.precio ?: 0.0,
                    subtotal = detalle.producto.precio ?: 0.0,
                    estado = "en proceso"
                )
                carritoViewModel.agregarAlCarrito(carritoDto)
            },
            onCommentClick = { /* implementar comentarios */ }
        )
    }
}
