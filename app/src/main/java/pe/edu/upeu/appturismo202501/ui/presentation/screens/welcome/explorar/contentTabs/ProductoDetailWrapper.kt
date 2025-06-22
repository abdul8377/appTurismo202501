package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.DetalleProducto
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.ProductoViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito.FavoritosViewModel

@Composable
fun ProductoDetailWrapper(
    productoId: Long,
    navController: NavController,
    viewModel: ProductoViewModel = hiltViewModel(),
    favViewModel: FavoritosViewModel = hiltViewModel()
) {
    // Llama el método una sola vez al inicio para cargar el producto
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
            onAddToCartClick = { /* implementar lógica del carrito */ },
            onCommentClick = { /* implementar comentarios */ }
        )
    }
}
