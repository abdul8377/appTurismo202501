package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.PaqueteDto
import pe.edu.upeu.appturismo202501.repository.PaqueteRepository
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.PaqueteTuristico
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ServicioPaquete
import javax.inject.Inject

@HiltViewModel
class PaquetesViewModel @Inject constructor(
    private val paqueteRepository: PaqueteRepository
) : ViewModel() {

    private val _paquetes = MutableStateFlow<List<PaqueteTuristico>>(emptyList())
    val paquetes: StateFlow<List<PaqueteTuristico>> = _paquetes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchPaquetes()
    }

    fun fetchPaquetes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = paqueteRepository.listAll()
                if (response.isSuccessful) {
                    val paquetesDto = response.body() ?: emptyList()
                    _paquetes.value = paquetesDto.map { it.toPaqueteTuristico() }
                } else {
                    _error.value = "Error al obtener paquetes: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Función de mapeo de PaqueteDto a PaqueteTuristico (para la UI)
fun PaqueteDto.toPaqueteTuristico(): PaqueteTuristico {
    return PaqueteTuristico(
        id = paquetesId.toInt(),
        nombre = nombre,
        imagenes = servicios.flatMap { it.imagenesUrl ?: emptyList() }.ifEmpty { listOf("https://via.placeholder.com/300") },
        precio = precioTotal,
        descripcion = descripcion ?: "",
        servicios = servicios.map { it.toServicioPaquete() }
    )
}

// Función de mapeo de ServicioDto a ServicioPaquete (para la UI)
fun pe.edu.upeu.appturismo202501.modelo.ServicioDto.toServicioPaquete(): ServicioPaquete {
    return ServicioPaquete(
        id = serviciosId.toInt(),
        nombre = nombre,
        imagenes = imagenesUrl ?: listOf(imagenUrl ?: "https://via.placeholder.com/150"),
        descripcion = descripcion,
        capacidad = capacidadMaxima
    )
} 