package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.alertas.AlertDialogComponent
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductItem
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductsFilterPanel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.CategoryViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.ProductoViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito.FavoritosViewModel

@Composable
fun ProductosContent(
    navController: NavController,
    prodVm: ProductoViewModel = hiltViewModel(),
    catVm: CategoryViewModel = hiltViewModel(),
    favVm: FavoritosViewModel = hiltViewModel()
) {
    val productos by prodVm.productosUi.collectAsState(initial = emptyList())
    val categorias by catVm.categoriesUi.collectAsState(initial = emptyList())
    val favUiState by favVm.uiState.collectAsState()

    val favoritosIds = favUiState.productosFav.map { it.id }.toSet()
    val isLoggedIn = favUiState.isLoggedIn

    val favoritosPendientes = remember { mutableStateMapOf<Long, Boolean>() }

    val showLoginDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    var filtroCatId by remember { mutableStateOf<Long?>(null) }
    var minPrice by remember { mutableStateOf("") }
    var maxPrice by remember { mutableStateOf("") }
    var filtroRating by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        prodVm.loadProductos()
        catVm.loadCategories()
        favVm.loadFavoritos()
    }

    Column {
        ProductsFilterPanel(
            categories = categorias,
            selectedCategoryId = filtroCatId,
            onCategorySelected = { filtroCatId = it },
            minPrice = minPrice,
            onMinPriceChange = { minPrice = it },
            maxPrice = maxPrice,
            onMaxPriceChange = { maxPrice = it },
            selectedRating = filtroRating,
            onRatingSelected = { filtroRating = it }
        )

        Spacer(Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            productos
                .filter { filtroCatId == null || it.categoryId == filtroCatId }
                .filter {
                    val valor = it.price
                    val min = minPrice.toDoubleOrNull() ?: Double.MIN_VALUE
                    val max = maxPrice.toDoubleOrNull() ?: Double.MAX_VALUE
                    valor in min..max
                }
                .filter { filtroRating == null || it.rating >= filtroRating!! }
                .chunked(2)
                .forEach { fila ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        fila.forEach { prod ->
                            val isFavoriteBackend = prod.id in favoritosIds
                            val isFavoriteVisual = favoritosPendientes[prod.id] ?: isFavoriteBackend

                            ProductItem(
                                producto = prod,
                                isFavorite = isFavoriteVisual,
                                onItemClick = {},
                                onFavoriteClick = {
                                    if (!isLoggedIn) {
                                        showLoginDialog.value = true
                                    } else {
                                        val currentFavorite = favoritosPendientes[prod.id] ?: isFavoriteBackend
                                        val newFavorite = !currentFavorite
                                        favoritosPendientes[prod.id] = newFavorite

                                        if (newFavorite) {
                                            favVm.agregarProductoFavoritoConDelay(prod.id)
                                            Toast.makeText(context, "Agregando a favoritos...", Toast.LENGTH_SHORT).show()
                                        } else {
                                            favVm.eliminarProductoFavorito(prod.id)
                                            Toast.makeText(context, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            )
                        }
                        if (fila.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                }
        }
    }

    AlertDialogComponent(
        isOpen = showLoginDialog,
        title = "Inicia sesión",
        message = "Debes iniciar sesión para gestionar favoritos.",
        onConfirm = { navController.navigate(Destinations.Login.route) },
        onDismiss = {},
        confirmText = "Iniciar sesión",
        dismissText = "Cancelar",
        isSuccess = true
    )
}
