package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations

import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ServicioGrid
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.ServiciosViewModel
import kotlin.collections.set


@Composable
fun GastronomiaContent(
    navController: NavController,
    viewModel: ServiciosViewModel = hiltViewModel()
) {

    val servicios = viewModel.servicios.value
    val favorites = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect (Unit) {
        viewModel.fetchServicios(tipoNegocioId = 2)
    }

    ServicioGrid  (
        items = servicios,
        favorites = favorites,
        onFavoriteClick = { id ->
            favorites[id] = !(favorites[id] ?: false)
        },
        onItemClick = { id ->
            if (id != 0L) {
                val route = Destinations.Servicios.route.replace("{id}", id.toString())
                navController.navigate(route)

            } else {
                Log.e("ServicioGrid", "ID inválido para Servicio: $id")
            }
        }
    )
}