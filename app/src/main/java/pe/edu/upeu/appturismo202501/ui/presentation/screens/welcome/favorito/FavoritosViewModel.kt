package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.FavoritoRequest
import pe.edu.upeu.appturismo202501.modelo.FavoritoResp
import pe.edu.upeu.appturismo202501.modelo.toServicioUi
import pe.edu.upeu.appturismo202501.repository.FavoritoRepository
import pe.edu.upeu.appturismo202501.repository.ProductoRespository
import pe.edu.upeu.appturismo202501.repository.ServicioRepository
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductoUi
import pe.edu.upeu.appturismo202501.modelo.ServicioUi
import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject

@HiltViewModel
class FavoritosViewModel @Inject constructor(
    private val favoritoRepo: FavoritoRepository,
    private val productoRepo: ProductoRespository,
    private val servicioRepo: ServicioRepository
) : ViewModel() {

    // Registros crudos de favoritos para conocer sus IDs
    private val _favoritosRecords = MutableStateFlow<List<FavoritoResp>>(emptyList())
    private val favoritosRecords: StateFlow<List<FavoritoResp>> = _favoritosRecords

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _productosFav = MutableStateFlow<List<ProductoUi>>(emptyList())
    val productosFav: StateFlow<List<ProductoUi>> = _productosFav

    private val _serviciosFav = MutableStateFlow<List<ServicioUi>>(emptyList())
    val serviciosFav: StateFlow<List<ServicioUi>> = _serviciosFav

    init {
        _isLoggedIn.value = !SessionManager.getToken().isNullOrEmpty()
        if (_isLoggedIn.value) {
            loadFavoritos()
        }
    }

    /**
     * Carga todos los favoritos del servidor y actualiza los flujos.
     */
    fun loadFavoritos() = viewModelScope.launch {
        val favResp = favoritoRepo.obtenerFavoritos()
        if (!favResp.isSuccessful) return@launch

        val favs = favResp.body().orEmpty()
        _favoritosRecords.value = favs

        val prodIds = favs.mapNotNull { it.productosId }
        val servIds = favs.mapNotNull { it.serviciosId }

        // Productos
        val allProdResp = productoRepo.productoServices()
        if (allProdResp.isSuccessful) {
            val listado = allProdResp.body().orEmpty()
            _productosFav.value = listado
                .filter { prodIds.contains(it.id) }
                .map { pr ->
                    ProductoUi(
                        id = pr.id,
                        categoryId = pr.categoriaProductoId,
                        imageUrl = pr.imagenUrl.orEmpty(),
                        title = pr.nombre,
                        subtitle = pr.descripcion.orEmpty(),
                        price = pr.precio,
                        priceFormatted = "S/. ${"%.2f".format(pr.precio)}",
                        rating = 4.7
                    )
                }
        }

        // Servicios
        val serviciosList = mutableListOf<ServicioUi>()
        servIds.forEach { id ->
            val resp = servicioRepo.servicioDetalle(id)
            if (resp.isSuccessful) {
                resp.body()?.toServicioUi()?.let { serviciosList.add(it) }
            }
        }
        _serviciosFav.value = serviciosList
    }

    /**
     * Elimina un producto de favoritos y actualiza los flujos.
     */
    fun eliminarProductoFavorito(productoId: Long) = viewModelScope.launch {
        val record = _favoritosRecords.value.firstOrNull { it.productosId == productoId }
            ?: return@launch
        val resp = favoritoRepo.eliminarFavorito(record.favoritosId)
        if (resp.isSuccessful) {
            _favoritosRecords.value = _favoritosRecords.value
                .filter { it.favoritosId != record.favoritosId }
            _productosFav.value = _productosFav.value
                .filter { it.id != productoId }
        }
    }

    /**
     * Elimina un servicio de favoritos y actualiza los flujos.
     */
    fun eliminarServicioFavorito(servicioId: Long) = viewModelScope.launch {
        val record = _favoritosRecords.value.firstOrNull { it.serviciosId == servicioId }
            ?: return@launch
        val resp = favoritoRepo.eliminarFavorito(record.favoritosId)
        if (resp.isSuccessful) {
            _favoritosRecords.value = _favoritosRecords.value
                .filter { it.favoritosId != record.favoritosId }
            _serviciosFav.value = _serviciosFav.value
                .filter { it.id != servicioId }
        }
    }

    /**
     * Agrega un producto a favoritos y recarga la lista.
     */
    fun agregarProductoFavorito(productoId: Long) = viewModelScope.launch {
        val request = FavoritoRequest(productosId = productoId, serviciosId = null)
        val resp = favoritoRepo.crearFavorito(request)
        if (resp.isSuccessful) loadFavoritos()
    }

    /**
     * Agrega un servicio a favoritos y recarga la lista.
     */
    fun agregarServicioFavorito(servicioId: Long) = viewModelScope.launch {
        val request = FavoritoRequest(productosId = null, serviciosId = servicioId)
        val resp = favoritoRepo.crearFavorito(request)
        if (resp.isSuccessful) loadFavoritos()
    }
}
