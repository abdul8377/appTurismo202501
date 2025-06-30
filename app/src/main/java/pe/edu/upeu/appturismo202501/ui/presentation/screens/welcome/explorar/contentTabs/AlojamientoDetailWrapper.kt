package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ServicioDetailScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.AlojamientoViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito.FavoritosViewModel

@Composable
fun AlojamientoDetailWrapper(
    alojamientoId: Long,
    navController: NavController,
    viewModel: AlojamientoViewModel = hiltViewModel(),
    favViewModel: FavoritosViewModel = hiltViewModel()
) {
    val alojamientoDetalle by viewModel.fetchAlojamientoDetalle(alojamientoId)
        .collectAsState(initial = null)

    val favUiState by favViewModel.uiState.collectAsState()

    alojamientoDetalle?.let { detalle ->
        val isFavorite = detalle.id in favUiState.serviciosFav.map { it.id }

        ServicioDetailScreen(
            servicio = detalle,
            isFavorite = isFavorite,
            isLoggedIn = favUiState.isLoggedIn,
            navController = navController,
            onBackClick = { navController.popBackStack() },
            onFavoriteClick = {
                if (isFavorite) {
                    favViewModel.eliminarServicioFavorito(detalle.id)
                } else {
                    favViewModel.agregarServicioFavoritoConDelay(detalle.id)
                }
            },
            onCheckAvailabilityClick = { /* abrir disponibilidad */ }
        )
    }
}
