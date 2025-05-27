package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedorcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.data.remote.EmprendimientoRequest
import pe.edu.upeu.appturismo202501.repository.EmprendimientoRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject

@HiltViewModel
class EmprendedorCreateViewModel @Inject constructor(
    private val emprendimientoRepository: EmprendimientoRepository
) : ViewModel() {

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre

    private val _descripcion = MutableStateFlow("")
    val descripcion: StateFlow<String> = _descripcion

    private val _tipoNegocioId = MutableStateFlow("")
    val tipoNegocioId: StateFlow<String> = _tipoNegocioId

    private val _direccion = MutableStateFlow("")
    val direccion: StateFlow<String> = _direccion

    private val _telefono = MutableStateFlow("")
    val telefono: StateFlow<String> = _telefono

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun onNombreChange(value: String) { _nombre.value = value }
    fun onDescripcionChange(value: String) { _descripcion.value = value }
    fun onTipoNegocioIdChange(value: String) { _tipoNegocioId.value = value }
    fun onDireccionChange(value: String) { _direccion.value = value }
    fun onTelefonoChange(value: String) { _telefono.value = value }

    fun crearEmprendimiento() {
        val token = SessionManager.getToken() ?: run {
            _message.value = "No hay token disponible. Inicia sesión."
            return
        }
        val tokenBearer = "Bearer $token"

        val tipoNegocioIdLong = _tipoNegocioId.value.toLongOrNull()

        val request = EmprendimientoRequest(
            nombre = _nombre.value.trim(),
            descripcion = _descripcion.value.trim().ifEmpty { null },
            tipo_negocio_id = tipoNegocioIdLong,
            direccion = _direccion.value.trim().ifEmpty { null },
            telefono = _telefono.value.trim().ifEmpty { null }
        )

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = emprendimientoRepository.crearEmprendimiento(tokenBearer, request)
                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Emprendimiento creado con éxito"
                    // Limpiar formulario si quieres
                    _nombre.value = ""
                    _descripcion.value = ""
                    _tipoNegocioId.value = ""
                    _direccion.value = ""
                    _telefono.value = ""
                } else {
                    _message.value = "Error al crear emprendimiento: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
