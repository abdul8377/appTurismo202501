package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.favorito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import pe.edu.upeu.appturismo202501.modelo.*
import pe.edu.upeu.appturismo202501.repository.*

import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject

data class FavoritosUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val productosFav: List<ProductoUi> = emptyList(),
    val serviciosFav: List<ServicioUi> = emptyList(),
    val errorMessage: String? = null,
    val uiEvent: String? = null
)

@HiltViewModel
class FavoritosViewModel @Inject constructor(
    private val favoritoRepo: FavoritoRepository,
    private val productoRepo: ProductoRespository,
    private val servicioRepo: ServicioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritosUiState())
    val uiState: StateFlow<FavoritosUiState> = _uiState

    private val _favoritosRecords = MutableStateFlow<List<FavoritoResp>>(emptyList())
    private val favoritosPendientes = mutableMapOf<Long, Job>()

    init {
        _uiState.update {
            it.copy(isLoggedIn = !SessionManager.getToken().isNullOrEmpty())
        }
        if (_uiState.value.isLoggedIn) loadFavoritos()
    }

    fun loadFavoritos() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        try {
            val favResp = favoritoRepo.obtenerFavoritos()
            if (!favResp.isSuccessful) {
                _uiState.update { it.copy(errorMessage = "Error cargando favoritos") }
                return@launch
            }

            val favs = favResp.body().orEmpty()
            _favoritosRecords.value = favs

            val prodIds = favs.mapNotNull { it.productosId }
            val servIds = favs.mapNotNull { it.serviciosId }

            val allProdResp = productoRepo.productoServices()
            val productosUi = if (allProdResp.isSuccessful) {
                allProdResp.body().orEmpty()
                    .filter { prodIds.contains(it.id) }
                    .map { it.toProductoUi() }
            } else emptyList()

            val serviciosUi = coroutineScope {
                servIds.map { id ->
                    async {
                        servicioRepo.servicioDetalle(id).body()?.toServicioUi()
                    }
                }.awaitAll().filterNotNull()
            }

            _uiState.update {
                it.copy(
                    productosFav = productosUi,
                    serviciosFav = serviciosUi,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "Error inesperado: ${e.message}") }
        }
    }

    fun agregarProductoFavoritoConDelay(productoId: Long) {
        favoritosPendientes[productoId]?.cancel()
        favoritosPendientes[productoId] = viewModelScope.launch {
            delay(2000)
            val resp = favoritoRepo.crearFavorito(FavoritoRequest(productosId = productoId))
            if (resp.isSuccessful) {
                _uiState.update { it.copy(uiEvent = "Producto agregado a favoritos") }
                loadFavoritos()
            } else {
                _uiState.update { it.copy(errorMessage = "No se pudo agregar el favorito") }
            }
            favoritosPendientes.remove(productoId)
        }
    }

    fun cancelarAgregarProductoFavorito(productoId: Long) {
        favoritosPendientes[productoId]?.cancel()
        favoritosPendientes.remove(productoId)
    }

    fun eliminarProductoFavorito(productoId: Long) = viewModelScope.launch {
        val record = _favoritosRecords.value.firstOrNull { it.productosId == productoId } ?: return@launch
        val resp = favoritoRepo.eliminarFavorito(record.favoritosId)
        if (resp.isSuccessful) {
            _uiState.update {
                it.copy(productosFav = it.productosFav.filter { prod -> prod.id != productoId })
            }
            _favoritosRecords.value = _favoritosRecords.value.filter { it.favoritosId != record.favoritosId }
        } else {
            _uiState.update { it.copy(errorMessage = "Error eliminando favorito") }
        }
    }

    fun eliminarServicioFavorito(servicioId: Long) = viewModelScope.launch {
        val record = _favoritosRecords.value.firstOrNull { it.serviciosId == servicioId } ?: return@launch
        val resp = favoritoRepo.eliminarFavorito(record.favoritosId)
        if (resp.isSuccessful) {
            _uiState.update {
                it.copy(serviciosFav = it.serviciosFav.filter { serv -> serv.id != servicioId })
            }
            _favoritosRecords.value = _favoritosRecords.value.filter { it.favoritosId != record.favoritosId }
        } else {
            _uiState.update { it.copy(errorMessage = "Error eliminando favorito") }
        }
    }

    fun limpiarMensajesUI() {
        _uiState.update { it.copy(errorMessage = null, uiEvent = null) }
    }

    private fun ProductResp.toProductoUi(): ProductoUi = ProductoUi(
        id = id,
        categoryId = categoriaProductoId,
        imageUrl = imagenUrl.orEmpty(),
        title = nombre,
        subtitle = descripcion.orEmpty(),
        price = precio,
        priceFormatted = "S/. ${"%.2f".format(precio)}",
        rating = 4.7
    )

    fun agregarServicioFavoritoConDelay(servicioId: Long) {
        favoritosPendientes[servicioId]?.cancel()
        favoritosPendientes[servicioId] = viewModelScope.launch {
            delay(2000)
            val resp = favoritoRepo.crearFavorito(FavoritoRequest(serviciosId = servicioId))
            if (resp.isSuccessful) {
                _uiState.update { it.copy(uiEvent = "Servicio agregado a favoritos") }
                loadFavoritos()
            } else {
                _uiState.update { it.copy(errorMessage = "No se pudo agregar el favorito") }
            }
            favoritosPendientes.remove(servicioId)
        }
    }

    fun cancelarAgregarServicioFavorito(servicioId: Long) {
        favoritosPendientes[servicioId]?.cancel()
        favoritosPendientes.remove(servicioId)
    }

}
