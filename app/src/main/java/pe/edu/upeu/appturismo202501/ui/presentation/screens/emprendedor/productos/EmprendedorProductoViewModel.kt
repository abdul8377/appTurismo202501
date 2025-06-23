package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.productos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pe.edu.upeu.appturismo202501.data.remote.RestProductos
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.repository.ProductoRespository
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EmprendedorProductoViewModel @Inject constructor(
    private val repo: ProductoRespository
) : ViewModel() {

    /** 1) Lista de productos */
    var productos by mutableStateOf<List<ProductResp>>(emptyList())
        private set

    /** 2) Loading state */
    var isLoading by mutableStateOf(false)
        private set

    /** 3) Mensaje de error */
    var errorMsg by mutableStateOf<String?>(null)
        private set

    /**
     * Carga los productos del emprendedor autenticado.
     */
    fun cargarMisProductos() {
        viewModelScope.launch {
            isLoading = true
            errorMsg = null
            try {
                val resp = repo.productoServicesMios()
                if (resp.isSuccessful) {
                    productos = resp.body() ?: emptyList()
                } else {
                    errorMsg = "Error listando (código ${resp.code()})"
                }
            } catch (e: Exception) {
                errorMsg = e.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Elimina un producto y recarga la lista.
     */
    fun eliminarProducto(id: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMsg = null
            try {
                val resp = repo.eliminarProducto(id)
                if (resp.isSuccessful) {
                    cargarMisProductos()
                } else {
                    errorMsg = "No se pudo eliminar (código ${resp.code()})"
                }
            } catch (e: Exception) {
                errorMsg = e.message ?: "Error desconocido"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Crea un producto. Convierte los parámetros a RequestBody y MultipartBody.Part.
     *
     * @param onResult callback con true=éxito, false=error
     */
    fun crearProducto(
        nombre: String,
        descripcion: String,
        precio: Double,
        stock: Int,
        imagenFile: File?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMsg = null
            try {
                // Convierte a RequestBody
                val txtType = "text/plain".toMediaType()
                val nombreRb      = nombre.toRequestBody(txtType)
                val descripcionRb = descripcion.toRequestBody(txtType)
                val precioRb      = precio.toString().toRequestBody(txtType)
                val stockRb       = stock.toString().toRequestBody(txtType)

                // Convierte a MultipartBody.Part si hay imagen
                val imagenPart: MultipartBody.Part? = imagenFile?.let { file ->
                    val mime = "image/*".toMediaType()
                    val reqBody = file.asRequestBody(mime)
                    MultipartBody.Part.createFormData("imagen", file.name, reqBody)
                }

                // Llama al repositorio
                val resp = repo.crearProducto(
                    nombreRb, descripcionRb, precioRb, stockRb, imagenPart
                )
                if (resp.isSuccessful) {
                    onResult(true)
                    cargarMisProductos()
                } else {
                    errorMsg = "Error creando (código ${resp.code()})"
                    onResult(false)
                }
            } catch (e: Exception) {
                errorMsg = e.message ?: "Error desconocido"
                onResult(false)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Actualiza un producto. Igual conversión que en crear.
     */
    fun actualizarProducto(
        id: Long,
        nombre: String,
        descripcion: String,
        precio: Double,
        stock: Int,
        imagenFile: File?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMsg = null
            try {
                val txtType = "text/plain".toMediaType()
                val nombreRb      = nombre.toRequestBody(txtType)
                val descripcionRb = descripcion.toRequestBody(txtType)
                val precioRb      = precio.toString().toRequestBody(txtType)
                val stockRb       = stock.toString().toRequestBody(txtType)

                val imagenPart: MultipartBody.Part? = imagenFile?.let { file ->
                    val mime = "image/*".toMediaType()
                    val reqBody = file.asRequestBody(mime)
                    MultipartBody.Part.createFormData("imagen", file.name, reqBody)
                }

                val resp = repo.actualizarProducto(
                    id, nombreRb, descripcionRb, precioRb, stockRb, imagenPart
                )
                if (resp.isSuccessful) {
                    onResult(true)
                    cargarMisProductos()
                } else {
                    errorMsg = "Error actualizando (código ${resp.code()})"
                    onResult(false)
                }
            } catch (e: Exception) {
                errorMsg = e.message ?: "Error desconocido"
                onResult(false)
            } finally {
                isLoading = false
            }
        }
    }
}