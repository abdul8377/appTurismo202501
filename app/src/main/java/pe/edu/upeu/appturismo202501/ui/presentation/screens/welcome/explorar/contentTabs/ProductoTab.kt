package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductItem
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.ProductoViewModel
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.alertas.AlertDialogComponent
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductoUi
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductsFilterPanel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.CategoryUi
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.CategoryViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProductosContent(
    navController: NavController,
    prodVm: ProductoViewModel = hiltViewModel(),
    catVm: CategoryViewModel = hiltViewModel()
) {
    // 1) Estados desde el ViewModel
    val productos  by prodVm.productosUi.collectAsState(initial = emptyList())
    val categorias by catVm.categoriesUi.collectAsState(initial = emptyList())
    val favoritosIds by prodVm.favoritosIds.collectAsState()
    val isLoggedIn   by prodVm.isLoggedIn.collectAsState()

    // 2) Dialog para invitar a login
    val showLoginDialog = remember { mutableStateOf(false) }
    val context        = LocalContext.current
    val scope          = rememberCoroutineScope()

    var filtroCatId  by remember { mutableStateOf<Long?>(null) }
    var minPrice     by remember { mutableStateOf("") }
    var maxPrice     by remember { mutableStateOf("") }
    var filtroRating by remember { mutableStateOf<Int?>(null) }

    Column {
        // Panel de filtros
        ProductsFilterPanel(
            categories         = categorias,
            selectedCategoryId = filtroCatId,
            onCategorySelected = { filtroCatId = it },
            minPrice           = minPrice,
            onMinPriceChange   = { minPrice = it },
            maxPrice           = maxPrice,
            onMaxPriceChange   = { maxPrice = it },
            selectedRating     = filtroRating,
            onRatingSelected   = { filtroRating = it }
        )

        Spacer(Modifier.height(8.dp))

        // Listado en 2 columnas sin scroll interno
        Column(modifier = Modifier.fillMaxWidth()) {
            productos
                .filter { filtroCatId == null || it.categoryId == filtroCatId }
                .filter {
                    val valor = it.price
                    val min   = minPrice.toDoubleOrNull() ?: Double.MIN_VALUE
                    val max   = maxPrice.toDoubleOrNull() ?: Double.MAX_VALUE
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
                            ProductItem(
                                producto    = prod,
                                isFavorite  = favoritosIds.contains(prod.id),
                                onItemClick = {
                                },
                                onFavoriteClick = {
                                    if (!isLoggedIn) {
                                        showLoginDialog.value = true
                                    } else {
                                        val wasFav = favoritosIds.contains(prod.id)
                                        prodVm.toggleFavorito(prod.id)
                                        if (wasFav) {
                                            // Notificar solo al eliminar
                                            Toast
                                                .makeText(context, "Producto eliminado de favoritos", Toast.LENGTH_SHORT)
                                                .show()
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

    // Alerta de login si intenta gestionar sin sesión
    AlertDialogComponent(
        isOpen       = showLoginDialog,
        title        = "Inicia sesión",
        message      = "Debes iniciar sesión para gestionar favoritos.",
        onConfirm    = { navController.navigate(Destinations.Login.route) },
        onDismiss    = { /* no-op */ },
        confirmText  = "Iniciar sesión",
        dismissText  = "Cancelar",
        isSuccess    = true
    )
}
