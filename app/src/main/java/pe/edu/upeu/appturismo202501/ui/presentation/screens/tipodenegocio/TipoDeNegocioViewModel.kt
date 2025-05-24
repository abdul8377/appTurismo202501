package pe.edu.upeu.appturismo202501.ui.presentation.screens.tipodenegocio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.Emprendimiento
import pe.edu.upeu.appturismo202501.modelo.TipoDeNegocio
import pe.edu.upeu.appturismo202501.repository.TipoDeNegocioRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TipoDeNegocioViewModel @Inject constructor(
    private val repository: TipoDeNegocioRepository
) : ViewModel() {

    // Estado para manejar los tipos de negocio
    private val _tiposDeNegocio = MutableStateFlow<List<TipoDeNegocio>>(emptyList())
    val tiposDeNegocio: StateFlow<List<TipoDeNegocio>> = _tiposDeNegocio

    // Estado para manejar los emprendimientos
    private val _emprendimientos = MutableStateFlow<List<Emprendimiento>>(emptyList())
    val emprendimientos: StateFlow<List<Emprendimiento>> = _emprendimientos

    // Estado para manejar el estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Estado para manejar los mensajes de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Estado para manejar los mensajes de éxito
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    // Función común para manejar las respuestas de éxito y error
    private fun handleResponse(response: Response<*>, successMessage: String? = null) {
        if (response.isSuccessful) {
            _errorMessage.value = null
            if (successMessage != null) {
                _successMessage.value = successMessage
            }
        } else {
            _errorMessage.value = "Error: ${response.code()} - ${response.message()}"
        }
    }

    // Función para cargar los tipos de negocio desde la API
    fun loadTiposDeNegocio() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<List<TipoDeNegocio>> = repository.getTiposDeNegocio()
                handleResponse(response)  // Actualiza el estado con la respuesta
                _tiposDeNegocio.value = response.body() ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para realizar la búsqueda de tipos de negocio
    fun searchTiposDeNegocio(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<List<TipoDeNegocio>> = repository.searchTiposDeNegocio(query)
                handleResponse(response)  // Actualiza el estado con la respuesta
                _tiposDeNegocio.value = response.body() ?: emptyList()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para eliminar un tipo de negocio
    fun deleteTipoDeNegocio(id: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<Unit> = repository.deleteTipoDeNegocio(id)
                handleResponse(response, "Tipo de negocio eliminado correctamente") // Actualiza con éxito
                if (response.isSuccessful) {
                    loadTiposDeNegocio() // Recargar los tipos de negocio después de la eliminación
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para crear o actualizar un tipo de negocio
    fun createOrUpdateTipoDeNegocio(tipoDeNegocio: TipoDeNegocio) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<TipoDeNegocio> = if (tipoDeNegocio.id == 0L) {
                    // Crear nuevo tipo de negocio
                    repository.createTipoDeNegocio(tipoDeNegocio)
                } else {
                    // Actualizar tipo de negocio existente
                    repository.updateTipoDeNegocio(tipoDeNegocio.id, tipoDeNegocio)
                }
                handleResponse(response, "Tipo de negocio guardado correctamente") // Actualiza con éxito
                if (response.isSuccessful) {
                    loadTiposDeNegocio() // Recargar los tipos de negocio después de la creación o actualización
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para cargar los emprendimientos de un tipo de negocio específico
    fun loadEmprendimientosByTipo(tipoId: Long) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<List<Emprendimiento>> = repository.getEmprendimientosByTipo(tipoId)
                if (response.isSuccessful) {
                    _emprendimientos.value = response.body() ?: emptyList()
                } else {
                    _errorMessage.value = "Error al cargar los emprendimientos"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
