package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedorcreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.data.remote.EmprendimientoRequest
import pe.edu.upeu.appturismo202501.data.remote.ResponderSolicitudRequest
import pe.edu.upeu.appturismo202501.data.remote.SolicitudEmprendimientoRequest
import pe.edu.upeu.appturismo202501.modelo.Emprendimiento
import pe.edu.upeu.appturismo202501.modelo.EmprendimientoResponse
import pe.edu.upeu.appturismo202501.modelo.EstadoSolicitudResponse
import pe.edu.upeu.appturismo202501.modelo.SolicitudEmprendimiento
import pe.edu.upeu.appturismo202501.modelo.TipoDeNegocio
import pe.edu.upeu.appturismo202501.repository.EmprendimientoRepository
import pe.edu.upeu.appturismo202501.repository.TipoDeNegocioRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject

@HiltViewModel
class EmprendedorCreateViewModel @Inject constructor(
    private val emprendimientoRepository: EmprendimientoRepository,
    private val tipoDeNegocioRepository: TipoDeNegocioRepository

) : ViewModel() {

    // Estado de los campos
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

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Mensajes de error o éxito
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // Estado para controlar el éxito de la creación
    private val _creationSuccess = MutableStateFlow(false)
    val creationSuccess: StateFlow<Boolean> = _creationSuccess


    // Estado para los tipos de negocio
    private val _tiposDeNegocio = MutableStateFlow<List<TipoDeNegocio>>(emptyList())
    val tiposDeNegocio: StateFlow<List<TipoDeNegocio>> = _tiposDeNegocio


    // Cambios en los campos
    fun onNombreChange(value: String) { _nombre.value = value }
    fun onDescripcionChange(value: String) { _descripcion.value = value }
    fun onTipoNegocioIdChange(value: String) { _tipoNegocioId.value = value }
    fun onDireccionChange(value: String) { _direccion.value = value }
    fun onTelefonoChange(value: String) { _telefono.value = value }

    // **Función para listar todos los emprendimientos**
    fun listarEmprendimientos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = emprendimientoRepository.listarEmprendimientos()
                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Emprendimientos obtenidos correctamente"
                } else {
                    _message.value = "Error al obtener emprendimientos: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // **Función para obtener detalle de un emprendimiento específico**
    fun obtenerEmprendimiento(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = emprendimientoRepository.obtenerEmprendimiento(id)
                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Emprendimiento obtenido correctamente"
                } else {
                    _message.value = "Error al obtener emprendimiento: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Cargar tipos de negocio
    fun cargarTiposDeNegocio() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = tipoDeNegocioRepository.getTiposDeNegocio()
                if (response.isSuccessful && response.body() != null) {
                    _tiposDeNegocio.value = response.body()!!
                } else {
                    _message.value = "Error al obtener los tipos de negocio: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // **Función para crear un nuevo emprendimiento**
    fun crearEmprendimiento() {
        // Validación de campos
        if (_nombre.value.isBlank()) {
            _message.value = "El nombre del emprendimiento es obligatorio."
            return
        }
        if (_tipoNegocioId.value.isBlank()) {
            _message.value = "El tipo de negocio es obligatorio."
            return
        }

        // Obtener el token
        val token = SessionManager.getToken() ?: run {
            _message.value = "No hay token disponible. Inicia sesión."
            return
        }
        val tokenBearer = "Bearer $token"

        // Validación de tipo de negocio
        val tipoNegocioIdLong = _tipoNegocioId.value.toLongOrNull()
        if (tipoNegocioIdLong == null) {
            _message.value = "El tipo de negocio debe ser un número válido."
            return
        }

        // Construcción del request
        val request = EmprendimientoRequest(
            nombre = _nombre.value.trim(),
            descripcion = _descripcion.value.trim().ifEmpty { null },
            tipo_negocio_id = tipoNegocioIdLong,
            direccion = _direccion.value.trim().ifEmpty { null },
            telefono = _telefono.value.trim().ifEmpty { null }
        )

        // Llamada al repositorio para crear el emprendimiento
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = emprendimientoRepository.crearEmprendimiento(tokenBearer, request)
                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Emprendimiento creado con éxito"
                    _creationSuccess.value = true
                    limpiarFormulario()
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

    // **Función para activar un emprendimiento pendiente**
    fun activarEmprendimiento(id: Long) {
        val token = SessionManager.getToken() ?: run {
            _message.value = "No hay token disponible. Inicia sesión."
            return
        }
        val tokenBearer = "Bearer $token"

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = emprendimientoRepository.activarEmprendimiento(tokenBearer, id)
                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Emprendimiento activado correctamente"
                } else {
                    _message.value = "Error al activar emprendimiento: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // **Función para enviar solicitud para unirse a un emprendimiento**
    fun enviarSolicitud(request: SolicitudEmprendimientoRequest) {
        val token = SessionManager.getToken() ?: run {
            _message.value = "No hay token disponible. Inicia sesión."
            return
        }
        val tokenBearer = "Bearer $token"

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Llamada al repositorio para enviar la solicitud
                val response = emprendimientoRepository.enviarSolicitud(tokenBearer, request)

                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Solicitud enviada con éxito, esperando aprobación."
                } else {
                    _message.value = "Error al enviar solicitud: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }






    // **Función para listar solicitudes pendientes para un emprendimiento**
    fun listarSolicitudesPendientes(emprendimientoId: Long) {
        val token = SessionManager.getToken() ?: run {
            _message.value = "No hay token disponible. Inicia sesión."
            return
        }
        val tokenBearer = "Bearer $token"

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = emprendimientoRepository.listarSolicitudesPendientes(tokenBearer, emprendimientoId)
                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Solicitudes obtenidas correctamente"
                } else {
                    _message.value = "Error al obtener solicitudes: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // **Función para responder a una solicitud (aprobar/rechazar)**
    fun responderSolicitud(solicitudId: Long, accion: String) {
        val token = SessionManager.getToken() ?: run {
            _message.value = "No hay token disponible. Inicia sesión."
            return
        }
        val tokenBearer = "Bearer $token"

        val request = ResponderSolicitudRequest(accion = accion)

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = emprendimientoRepository.responderSolicitud(tokenBearer, solicitudId, request)
                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Solicitud respondida correctamente"
                } else {
                    _message.value = "Error al responder solicitud: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // **Función para obtener el estado de la solicitud/emprendimiento**
    fun getEstadoSolicitud() {
        val token = SessionManager.getToken() ?: run {
            _message.value = "No hay token disponible. Inicia sesión."
            return
        }
        val tokenBearer = "Bearer $token"

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = emprendimientoRepository.getEstadoSolicitud(tokenBearer)
                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Estado de solicitud obtenido correctamente"
                } else {
                    _message.value = "Error al obtener estado: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // **Función para obtener las solicitudes del usuario**
    fun solicitudesUsuario() {
        val token = SessionManager.getToken() ?: run {
            _message.value = "No hay token disponible. Inicia sesión."
            return
        }
        val tokenBearer = "Bearer $token"

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = emprendimientoRepository.solicitudesUsuario(tokenBearer)
                if (response.isSuccessful && response.body() != null) {
                    _message.value = "Solicitudes obtenidas correctamente"
                } else {
                    _message.value = "Error al obtener solicitudes: ${response.message()}"
                }
            } catch (e: Exception) {
                _message.value = "Error de red: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para limpiar los campos
    private fun limpiarFormulario() {
        _nombre.value = ""
        _descripcion.value = ""
        _tipoNegocioId.value = ""
        _direccion.value = ""
        _telefono.value = ""
    }
}
