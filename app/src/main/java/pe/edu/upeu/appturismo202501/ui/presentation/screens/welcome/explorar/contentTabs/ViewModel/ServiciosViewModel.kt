package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Flow
import pe.edu.upeu.appturismo202501.modelo.AlojamientoDetalleUi
import pe.edu.upeu.appturismo202501.modelo.ServicioUi
import pe.edu.upeu.appturismo202501.modelo.toAlojamientoDetalleUi

import pe.edu.upeu.appturismo202501.modelo.toServicioUi
import pe.edu.upeu.appturismo202501.repository.ServicioRepository


import pe.edu.upeu.appturismo202501.utils.TokenUtils
import javax.inject.Inject

@HiltViewModel
class ServiciosViewModel @Inject constructor(
    private val servicioRepository: ServicioRepository
) : ViewModel() {

    private val _servicios = mutableStateOf<List<ServicioUi>>(emptyList())
    val servicios: State<List<ServicioUi>> get() = _servicios

    private val _favorites = mutableStateMapOf<Long, Boolean>()
    val favorites: Map<Long, Boolean> get() = _favorites

    fun fetchServicios(tipoNegocioId: Long) {
        viewModelScope.launch {
            try {
                val response = servicioRepository.servicioFetch(tipoNegocioId)
                if (response.isSuccessful) {
                    val servicios = response.body() ?: emptyList()
                    _servicios.value = servicios.map { it.toServicioUi() }
                } else {
                    Log.e("ServiciosVM", "Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ServiciosVM", "Excepción: ${e.message}")
            }
            Log.d("ServiciosVM", "URL Base: ${TokenUtils.API_URL}")
            Log.d("ServiciosVM", "Tipo Negocio ID: $tipoNegocioId")
        }
    }

    fun fetchServicioDetalle(id: Long): kotlinx.coroutines.flow.Flow<ServicioUi?> {
        return flow {
            try {
                val response = servicioRepository.servicioDetalle(id)
                if (response.isSuccessful) {
                    val servicio = response.body()
                    emit(servicio?.toServicioUi())
                } else {
                    Log.e("ServicioVM", "Error: ${response.code()}")
                    emit(null)
                }
            } catch (e: Exception) {
                Log.e("ServicioVM", "Excepción: ${e.message}")
                emit(null)
            }
        }
    }

    fun toggleFavorite(id: Long) {
        _servicios.value = _servicios.value.map { item ->
            if (item.id == id) {
                item.copy(isFavorite = !item.isFavorite)
            } else {
                item
            }
        }
    }

}