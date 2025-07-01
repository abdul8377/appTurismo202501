package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.modelo.ProductoDetalleResponse
import pe.edu.upeu.appturismo202501.modelo.ProductoUi
import pe.edu.upeu.appturismo202501.repository.ProductoRespository

import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val repo: ProductoRespository
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

    private val _productoDetalle = MutableStateFlow<ProductoDetalleResponse?>(null)
    val productoDetalle: StateFlow<ProductoDetalleResponse?> = _productoDetalle

    init {
        loadProductos()
    }

    fun loadProductos() = viewModelScope.launch {
        repo.productoServices().takeIf { it.isSuccessful }?.body()?.let {
            _productos.value = it
        }
    }

    fun fetchProductoDetalle(productoId: Long) = viewModelScope.launch {
        val response = repo.productoDetalle(productoId)
        if (response.isSuccessful) {
            _productoDetalle.value = response.body()
        } else {
            _productoDetalle.value = null
        }
    }
}
