package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.productos

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pe.edu.upeu.appturismo202501.data.remote.RestProductos
import pe.edu.upeu.appturismo202501.modelo.CategoryProductcsResp
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.repository.CategoryProductsRespository
import pe.edu.upeu.appturismo202501.repository.ProductoRespository
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EmprendedorProductoViewModel @Inject constructor(
    private val repoProd: ProductoRespository,
    private val repoCats: CategoryProductsRespository,
    @ApplicationContext private val ctx: Context
) : ViewModel() {

    var productos by mutableStateOf<List<ProductResp>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMsg by mutableStateOf<String?>(null)
        private set

    var isSaving by mutableStateOf(false)
        private set

    var saveError by mutableStateOf<String?>(null)
        private set


    private val _isLoading   = mutableStateOf(false)
    private val _errorMsg    = mutableStateOf<String?>(null)



    private val _editingProduct = MutableStateFlow<ProductResp?>(null)
    val editingProduct: StateFlow<ProductResp?> = _editingProduct


    private val _categories = MutableStateFlow<List<CategoryProductcsResp>>(emptyList())
    val categories: StateFlow<List<CategoryProductcsResp>> = _categories


    init {

        viewModelScope.launch {
            val resp = repoCats.categoryProductsServices()
            if (resp.isSuccessful) {
                _categories.value = resp.body().orEmpty()
            } else {
                // opcionalmente maneja un error aquí
            }
        }
        // 2) cargar productos del emprendedor
        cargarMisProductos()
    }

    fun cargarMisProductos() {
        viewModelScope.launch {
            isLoading = true
            errorMsg = null
            try {
                val resp = repoProd.productoServicesMios()
                if (resp.isSuccessful) {
                    productos = resp.body().orEmpty()
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

    fun loadProductForEdit(id: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMsg  = null

            val resp = repoProd.getProductoById(id)
            if (resp.isSuccessful) {
                _editingProduct.value = resp.body()
            } else {
                errorMsg = "Error cargando producto (código ${resp.code()})"
            }

            isLoading = false
        }
    }

    fun eliminarProducto(id: Long) {
        viewModelScope.launch {
            isLoading = true
            errorMsg = null
            try {
                val resp = repoProd.eliminarProducto(id)
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
     * Construye un nuevo producto y lo envía al repo
     */
    fun saveProduct(
        name: String,
        description: String,
        priceText: String,
        stockText: String,
        status: Boolean,
        categoryId: Long,
        images: List<Uri>,
        onResult: (Boolean) -> Unit
    ) {
        val mt = "text/plain".toMediaType()
        val rbCat  = categoryId.toString().toRequestBody(mt)
        val rbName = name.toRequestBody(mt)
        val rbDesc = description.toRequestBody(mt)
        val rbPrice= priceText.toRequestBody(mt)
        val rbStock= stockText.toRequestBody(mt)
        val rbEst  = (if (status) "activo" else "inactivo").toRequestBody(mt)

        // Construye la lista de partes de imagenes[]
        val parts = images.mapIndexed { index, uri ->
            val stream  = ctx.contentResolver.openInputStream(uri)!!
            val bytes   = stream.readBytes().also { stream.close() }
            val mime    = ctx.contentResolver.getType(uri) ?: "image/jpeg"
            val reqBody = bytes.toRequestBody(mime.toMediaType())
            // IMPORTANTE: el name debe ser "imagenes[]" para que Laravel lo reconozca como array
            MultipartBody.Part.createFormData(
                name = "imagenes[]",
                filename = "photo_$index.jpg",
                body = reqBody
            )
        }

        viewModelScope.launch {
            isSaving = true
            saveError = null
            try {
                val resp = repoProd.crearProducto(
                    categoriasProductosId    = rbCat,
                    nombre                   = rbName,
                    descripcion              = rbDesc,
                    precio                   = rbPrice,
                    stock                    = rbStock,
                    estado                   = rbEst,
                    imagenes                 = parts
                )
                if (resp.isSuccessful) {
                    cargarMisProductos()
                    onResult(true)
                } else {
                    val err = resp.errorBody()?.string().orEmpty()
                    saveError = "Error ${resp.code()}: $err"
                    onResult(false)
                }
            } catch (e: Exception) {
                saveError = e.message
                onResult(false)
            } finally {
                isSaving = false
            }
        }
    }

    /**
     * Actualiza un producto existente
     */
    fun actualizarProducto(
        id: Long,
        nombre: String,
        descripcion: String,
        precio: Double,
        stock: Int,
        estado: Boolean,
        categoryId: Long,
        images: List<Uri>,
        onResult: (Boolean) -> Unit
    ) {
        val mt      = "text/plain".toMediaType()
        val rbMethod= "PUT".toRequestBody(mt)
        val rbCat   = categoryId.toString().toRequestBody(mt)
        val rbNom   = nombre.toRequestBody(mt)
        val rbDesc  = descripcion.toRequestBody(mt)
        val rbPre   = precio.toString().toRequestBody(mt)
        val rbSto   = stock.toString().toRequestBody(mt)
        val rbEst   = (if (estado) "activo" else "inactivo").toRequestBody(mt)

        // Sólo las URIs locales
        val toUpload = images.filter {
            it.scheme != "http" && it.scheme != "https"
        }

        viewModelScope.launch {
            isSaving = true
            saveError = null
            try {
                // 1) Armo la lista de partes _dentro_ de la corrutina
                val parts = mutableListOf<MultipartBody.Part>()
                // Método override
                parts += MultipartBody.Part.createFormData("_method", null, rbMethod)
                // Nuevas imágenes
                toUpload.forEachIndexed { i, uri ->
                    ctx.contentResolver.openInputStream(uri)?.use { stream ->
                        val bytes = stream.readBytes()
                        val mime  = ctx.contentResolver.getType(uri) ?: "image/jpeg"
                        val body  = bytes.toRequestBody(mime.toMediaType())
                        parts += MultipartBody.Part.createFormData(
                            name     = "imagenes[]",
                            filename = "photo_$i.jpg",
                            body     = body
                        )
                    }
                }

                // 2) Llamo a la API pasando los rbCat…rbEst y la lista de parts
                val resp = repoProd.actualizarProducto(
                    id,
                    rbCat, rbNom, rbDesc, rbPre, rbSto, rbEst,
                    parts
                )

                // 3) Si la llamada OK, refresco lista Y llamo al callback
                if (resp.isSuccessful) {
                    cargarMisProductos()
                    onResult(true)
                } else {
                    saveError = "Error ${resp.code()}: ${resp.errorBody()?.string().orEmpty()}"
                    onResult(false)
                }
            } catch (e: Exception) {
                saveError = e.message
                onResult(false)
            } finally {
                isSaving = false
            }
        }
    }

    fun eliminarProducto(
        id: Long,
        onResult: (Boolean) -> Unit
    ) = viewModelScope.launch {
        _isLoading.value = true
        _errorMsg.value  = null
        try {
            val resp = repoProd.eliminarProducto(id)
            if (resp.isSuccessful) {
                // vuelvo a recargar la lista
                cargarMisProductos()
                onResult(true)
            } else {
                _errorMsg.value = "Error ${resp.code()}: ${resp.errorBody()?.string().orEmpty()}"
                onResult(false)
            }
        } catch (e: Exception) {
            _errorMsg.value = e.localizedMessage
            onResult(false)
        } finally {
            _isLoading.value = false
        }
    }
}