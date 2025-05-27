package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import android.annotation.SuppressLint
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    val productos  by prodVm.productosUi.collectAsState(initial = emptyList())
    val categorias by catVm.categoriesUi.collectAsState(initial = emptyList())
    val favorites  = remember { mutableStateMapOf<Long, Boolean>() }

    var filtroCatId  by remember { mutableStateOf<Long?>(null) }
    var minPrice     by remember { mutableStateOf("") }
    var maxPrice     by remember { mutableStateOf("") }
    var filtroRating by remember { mutableStateOf<Int?>(null) }

    // Solo lazyColumn afuera: aquí un Column
    Column {
        // Panel de filtros con categorías reales
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
                // Filtrar por categoría
                .filter { filtroCatId == null || it.categoryId == filtroCatId }
                // Filtrar por precio numérico
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
                                producto        = prod,
                                isFavorite      = favorites[prod.id] == true,
                                onItemClick     = { /* navegar */ },
                                onFavoriteClick = {
                                    favorites[prod.id] = !(favorites[prod.id] ?: false)
                                }
                            )
                        }
                        if (fila.size == 1) Spacer(Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(12.dp))
                }
        }
    }

}