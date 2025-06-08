package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ServicioDetailScreen
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.AlojamientoViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.ServiciosViewModel
import androidx.compose.runtime.getValue

@Composable
fun AlojamientoDetailWrapper(
    alojamientoId: Long,
    navController: NavController,
    viewModel: AlojamientoViewModel = hiltViewModel()
) {
    val alojamientoDetalle by viewModel.fetchAlojamientoDetalle(alojamientoId)
        .collectAsState(initial = null)

    alojamientoDetalle?.let { detalle ->
        ServicioDetailScreen(
            servicio = detalle,
            isFavorite = detalle.isFavorite,
            onBackClick = { navController.popBackStack() },
            onFavoriteClick = { viewModel.toggleFavorite(detalle.id) },
            onCheckAvailabilityClick = { /* abrir disponibilidad */ }
        )
    }
}