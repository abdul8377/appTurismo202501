package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.servicios

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pe.edu.upeu.appturismo202501.modelo.ServicioDto
import pe.edu.upeu.appturismo202501.modelo.ServicioEmprendedorUi
import pe.edu.upeu.appturismo202501.modelo.ServicioUi
import pe.edu.upeu.appturismo202501.modelo.UpdateServicioRequest
import pe.edu.upeu.appturismo202501.modelo.toServicioEmprendedorUi
import pe.edu.upeu.appturismo202501.modelo.toServicioUi
import pe.edu.upeu.appturismo202501.repository.ServicioRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ServiciosViewModel @Inject constructor(
    private val repo: ServicioRepository
): ViewModel() {

//    private val _propios = MutableStateFlow<Result<List<ServicioEmprendedorUi>>?>(null)
//    val propios: StateFlow<Result<List<ServicioEmprendedorUi>>?> = _propios

    private val _serviciosEmpr = MutableStateFlow<List<ServicioEmprendedorUi>>(emptyList())
    val serviciosEmprendedor: StateFlow<List<ServicioEmprendedorUi>> = _serviciosEmpr


    // Un SharedFlow para notificar resultados de CRUD
    private val _operacion = MutableSharedFlow<Result<ServicioDto>>()
    val operacion: SharedFlow<Result<ServicioDto>> = _operacion


//    fun loadPropios() = viewModelScope.launch {
//        _propios.value = repo.fetchServicioEmprendedor()
//            .mapCatching { it.map { dto -> dto.toServicioEmprendedorUi() } }
//    }
    fun loadPropios() = viewModelScope.launch {
        repo.fetchServicioEmprendedor()
            .mapCatching { dtos -> dtos.map { it.toServicioEmprendedorUi() } }
            .onSuccess { list -> _serviciosEmpr.value = list }
            .onFailure {
                // podrías emitir un error con otro SharedFlow si lo deseas
            }
    }


    fun createService(
        nombre: String,
        descripcion: String?,
        precio: Double,
        capacidad: Int,
        duracion: String?,
        estado: String?,
        imagenFiles: List<File>?
    ) = viewModelScope.launch {
        Log.d("ServiciosVM", "createService: $nombre, archivos=${imagenFiles?.size}")
        val resultado: Result<ServicioDto> = repo.createServicio(
            nombre, descripcion,
            precio, capacidad, duracion, estado, imagenFiles
        )
        _operacion.emit(resultado)
        resultado.onSuccess {
            loadPropios()
        }
    }

    fun updateService(
        req: UpdateServicioRequest,
        newImages: List<File>?,
        eliminarImagenIds: List<Long>?,
        onResult: (Boolean) -> Unit
    ) = viewModelScope.launch {
        Log.d(
            "ServiciosVM", "updateService: ${req.serviciosId}, archivos=${newImages?.size}, eliminar=${eliminarImagenIds?.size}"
        )

        val result = repo.updateServicio(
            req = req,
            newImages = newImages,
            eliminarImagenIds = eliminarImagenIds
        )

        // Emitir el resultado al SharedFlow
        _operacion.emit(result)

        // Llamar al callback
        result.fold(
            onSuccess = {
                Log.d("ServiciosVM", "Actualización exitosa")
                loadPropios()
                onResult(true)
            },
            onFailure = {
                Log.e("ServiciosVM", "Error en actualización: ${it.message}")
                onResult(false)
            }
        )
    }

    fun deleteService(id: Long) = viewModelScope.launch {
        repo.deleteServicio(id)
            .onSuccess { loadPropios() }
            .onFailure { /* aquí podrías emitir un error por otro SharedFlow */ }
    }
}
