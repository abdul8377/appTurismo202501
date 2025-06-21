package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.FavoriteProductCard
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.FavoriteServiceCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    navController: NavController,
    vm: FavoritosViewModel = hiltViewModel()
) {
    // Recargar la lista al retomar el foco (e.g. volver de otra pantalla)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                vm.loadFavoritos()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val isLoggedIn = vm.isLoggedIn.collectAsState().value
    val productos = vm.productosFav.collectAsState().value
    val servicios = vm.serviciosFav.collectAsState().value

    if (!isLoggedIn) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Inicia sesión para añadir actividades a tus favoritos",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Button(onClick = {
                    navController.navigate(Destinations.Login.route)
                }) {
                    Text("Inicia sesión")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Favoritos") }) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (productos.isNotEmpty()) {
                item {
                    Text(
                        text = "Productos",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        items(productos) { prod ->
                            FavoriteProductCard(
                                producto = prod,
                                isFavorite = true,
                                onFavoriteToggle = { vm.eliminarProductoFavorito(prod.id) },
                                onClick = { /* navegación o detalle */ }
                            )
                        }
                    }
                }
            }

            if (servicios.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Servicios",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                item {
                    LazyHorizontalGrid(
                        rows = GridCells.Fixed(1),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        items(servicios) { serv ->
                            FavoriteServiceCard(
                                servicio = serv,
                                isFavorite = true,
                                onFavoriteToggle = { vm.eliminarServicioFavorito(serv.id) },
                                onClick = { /* navegación o detalle */ }
                            )
                        }
                    }
                }
            }

            if (productos.isEmpty() && servicios.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aún no tienes favoritos",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
