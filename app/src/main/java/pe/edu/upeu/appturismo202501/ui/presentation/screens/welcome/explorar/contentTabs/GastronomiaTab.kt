package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.alertas.AlertDialogComponent
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ServicioGrid
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.ServiciosViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito.FavoritosViewModel

@Composable
fun GastronomiaContent(
    navController: NavController,
    viewModel: ServiciosViewModel = hiltViewModel(),
    favVm: FavoritosViewModel = hiltViewModel()
) {
    val servicios = viewModel.servicios.value
    val favUiState = favVm.uiState.value

    val favoritosIds = favUiState.serviciosFav.map { it.id }.toSet()
    val favoritosPendientes = remember { mutableStateMapOf<Long, Boolean>() }

    val showLoginDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchServicios(tipoNegocioId = 2)
        favVm.loadFavoritos()
    }

    val favoritesVisual = servicios.associate { servicio ->
        val favoritoBackend = servicio.id in favoritosIds
        val favoritoTemporal = favoritosPendientes[servicio.id] ?: favoritoBackend
        servicio.id to favoritoTemporal
    }

    ServicioGrid(
        items = servicios,
        favorites = favoritesVisual,
        onFavoriteClick = { id ->
            if (!favUiState.isLoggedIn) {
                showLoginDialog.value = true
            } else {
                val isCurrentlyFavorite = favoritosPendientes[id] ?: (id in favoritosIds)
                val newFavorite = !isCurrentlyFavorite

                favoritosPendientes[id] = newFavorite

                if (newFavorite) {
                    favVm.agregarServicioFavoritoConDelay(id)
                    Toast.makeText(context, "Agregando a favoritos...", Toast.LENGTH_SHORT).show()
                } else {
                    favVm.eliminarServicioFavorito(id)
                    Toast.makeText(context, "Eliminado de favoritos", Toast.LENGTH_SHORT).show()
                }
            }
        },
        onItemClick = { /* Navegar a detalle */ }
    )

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
