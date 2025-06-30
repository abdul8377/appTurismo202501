package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductItem
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ServicioGrid

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    navController: NavController,
    vm: FavoritosViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsState()

    val isLoggedIn = !SessionManager.getToken().isNullOrEmpty()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            vm.loadFavoritos()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Mis Favoritos") })
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                !isLoggedIn -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Sin sesión",
                            modifier = Modifier.size(120.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Inicia sesión para añadir actividades a tus favoritos")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigate(Destinations.Login.route) }
                        ) {
                            Text("Inicia sesión")
                        }
                    }
                }
                uiState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.errorMessage.orEmpty())
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (uiState.productosFav.isNotEmpty()) {
                            Text(
                                text = "Productos",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            Column {
                                uiState.productosFav.chunked(2).forEach { fila ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        fila.forEach { prod ->
                                            ProductItem(
                                                producto = prod,
                                                isFavorite = true,
                                                onItemClick = {},
                                                onFavoriteClick = {
                                                    vm.eliminarProductoFavorito(prod.id)
                                                },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        if (fila.size == 1) Spacer(Modifier.weight(1f))
                                    }
                                }
                            }
                        }

                        if (uiState.serviciosFav.isNotEmpty()) {
                            Text(
                                text = "Servicios",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            ServicioGrid(
                                items = uiState.serviciosFav,
                                favorites = uiState.serviciosFav.associate { it.id to true },
                                onFavoriteClick = { servicioId ->
                                    vm.eliminarServicioFavorito(servicioId)
                                },
                                onItemClick = {}
                            )
                        }

                        if (uiState.productosFav.isEmpty() && uiState.serviciosFav.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Aún no tienes favoritos guardados.")
                            }
                        }
                    }
                }
            }
        }
    }
}
