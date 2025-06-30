package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.AlojamientoDetalleUi
import pe.edu.upeu.appturismo202501.modelo.ServicioUi
import pe.edu.upeu.appturismo202501.modelo.toAlojamientoDetalleUi
import pe.edu.upeu.appturismo202501.modelo.toAlojamientoUi

import pe.edu.upeu.appturismo202501.modelo.toServicioUi
import pe.edu.upeu.appturismo202501.repository.ServicioRepository
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.AlojamientoUi


import pe.edu.upeu.appturismo202501.utils.TokenUtils
import javax.inject.Inject

@HiltViewModel
class AlojamientoViewModel @Inject constructor(
    private val servicioRepository: ServicioRepository
) : ViewModel() {

    private val _alojamientos = mutableStateOf<List<AlojamientoUi>>(emptyList())
    val alojamientos: State<List<AlojamientoUi>> get() = _alojamientos

    private val _favorites = mutableStateMapOf<Long, Boolean>()
    val favorites: Map<Long, Boolean> get() = _favorites

    fun servicioFetch() {
        viewModelScope.launch {
            try {
                val response = servicioRepository.servicioFetch(tipoNegocioId = 4)  // 4 = Alojamiento
                if (response.isSuccessful) {
                    val servicios = response.body() ?: emptyList()
                    _alojamientos.value = servicios.map { it.toAlojamientoUi() }
                } else {
                    Log.e("AlojamientoVM", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AlojamientoVM", "Excepción: ${e.message}")
            }
        }
    }

    fun toggleFavorite(id: Long) {
        _alojamientos.value = _alojamientos.value.map { item ->
            if (item.id == id) {
                item.copy(isFavorite = !item.isFavorite)
            } else {
                item
            }
        }
    }


    fun fetchAlojamientoDetalle(id: Long): Flow<AlojamientoDetalleUi?> {
        return flow {
            try {
                val response = servicioRepository.servicioDetalle(id)
                if (response.isSuccessful) {
                    val servicio = response.body()
                    emit(servicio?.toAlojamientoDetalleUi())
                } else {
                    Log.e("AlojamientoVM", "Error: ${response.code()}")
                    emit(null)
                }
            } catch (e: Exception) {
                Log.e("AlojamientoVM", "Excepción: ${e.message}")
                emit(null)
            }
        }
    }
}