// ProductoViewModel.kt
package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.FavoritoRequest
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.repository.FavoritoRepository
import pe.edu.upeu.appturismo202501.repository.ProductoRespository
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductoUi
import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val repo: ProductoRespository,
    private val favoritoRepo: FavoritoRepository
) : ViewModel() {

    private val _productos = MutableStateFlow<List<ProductResp>>(emptyList())
    val productosUi: StateFlow<List<ProductoUi>> = _productos
        .map { list ->
            list.map { pr ->
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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _favoritosMap = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val favoritosIds: StateFlow<Set<Long>> = _favoritosMap
        .map { it.keys }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    private val _isLoggedIn = MutableStateFlow(!SessionManager.getToken().isNullOrEmpty())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    init {
        loadProductos()
        if (_isLoggedIn.value) loadFavoritos()
    }

    fun loadProductos() = viewModelScope.launch {
        repo.productoServices().takeIf { it.isSuccessful }?.body()?.let {
            _productos.value = it
        }
    }

    fun loadFavoritos() = viewModelScope.launch {
        favoritoRepo.obtenerFavoritos().takeIf { it.isSuccessful }?.body()?.let { list ->
            _favoritosMap.value = list.mapNotNull { resp ->
                resp.productosId?.let { pid -> pid to resp.favoritosId }
            }.toMap()
        }
    }

    fun toggleFavorito(productoId: Long) = viewModelScope.launch {
        val favId = _favoritosMap.value[productoId]
        if (favId != null) {
            favoritoRepo.eliminarFavorito(favId)
            _favoritosMap.value -= productoId
        } else {
            val request = FavoritoRequest(productosId = productoId, serviciosId = null)
            favoritoRepo.crearFavorito(request).takeIf { it.isSuccessful }?.body()?.let {
                _favoritosMap.value += (productoId to it.favoritosId)
            }
        }
    }
}