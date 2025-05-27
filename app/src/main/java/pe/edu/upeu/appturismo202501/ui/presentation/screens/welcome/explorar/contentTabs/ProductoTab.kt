package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.Producto

import pe.edu.upeu.appturismo202501.R


import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductItem

@Composable
fun ProductosContent(
    navController: NavController
) {
    val demo = listOf(
        Producto(1, "https://images.unsplash.com/photo-1547721064-da6cfb341d50",
            "Recorrido fotográfico en auto vintage con estilo",
            "Roma, Italia",
            "Desde \$63 por viajero",
            4.95
        ),
        Producto(2, "https://images.unsplash.com/photo-1547721064-da6cfb341d50",
            "Sesión de fotos en el Museo Metropolitano de Nueva York",
            "Nueva York, Estados Unidos",
            "Desde \$300 por viajero",
            5.00
        )
    )
    val favorites = remember { mutableStateMapOf<Long, Boolean>() }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(demo) { prod ->
            ProductItem(
                producto        = prod,
                isFavorite      = favorites[prod.id] == true,
                onItemClick     = { /* navController.navigate(...) */ },
                onFavoriteClick = {
                    favorites[prod.id] = !(favorites[prod.id] ?: false)
                }
            )
        }
    }
}