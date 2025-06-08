package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.AlojamientoGrid
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ServicioGrid
import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.AlojamientoViewModel

import pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel.ServiciosViewModel

@Composable
fun AlojamientoContent(
    navController: NavController,
    viewModel: AlojamientoViewModel = hiltViewModel()
) {
    val alojamientos = viewModel.alojamientos.value
    val favorites = remember { mutableStateMapOf<Long, Boolean>() }

    LaunchedEffect(Unit) {
        viewModel.servicioFetch() // ✅ Ajuste aquí
    }

    AlojamientoGrid(
        items = alojamientos, // ✅ Ajuste aquí
        favorites = favorites,
        onFavoriteClick = { id ->
            favorites[id] = !(favorites[id] ?: false)
        },
        onItemClick = { id ->
            if (id != 0L) {
                val route = Destinations.Alojamientos.route.replace("{id}", id.toString())
                navController.navigate(route)

            } else {
                Log.e("AlojamientoGrid", "ID inválido para alojamiento: $id")
            }
        }
    )
}