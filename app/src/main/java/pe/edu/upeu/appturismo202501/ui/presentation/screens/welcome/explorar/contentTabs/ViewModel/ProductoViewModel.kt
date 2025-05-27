package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.repository.ProductoRespository
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductoUi
import pe.edu.upeu.appturismo202501.utils.TokenUtils

import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val repo: ProductoRespository
) : ViewModel() {

    // 1) Flujo privado de DTO desde el repo
    private val _productos = MutableStateFlow<List<ProductResp>>(emptyList())
    val productos: StateFlow<List<ProductResp>> = _productos.asStateFlow()

    // 2) Flujo de UI models listos para la UI
    //    Igual que en banners: mapeas directamente usando el campo imagen_url
    val productosUi: StateFlow<List<ProductoUi>> = productos
        .map { list ->
            list.map { pr ->
                ProductoUi(
                    id        = pr.id,
                    categoryId = pr.categoriaProductoId,
                    imageUrl  = pr.imagenUrl.orEmpty(),           // uso directo
                    title     = pr.nombre,
                    subtitle  = pr.descripcion.orEmpty(),
                    price     = pr.precio,
                    priceFormatted = "S/. ${"%.2f".format(pr.precio)}",
                    rating    = 4.7                                // o pr.rating si lo tienes
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        loadProductos()
    }

    fun loadProductos() = viewModelScope.launch {
        val resp = repo.productoServices()
        if (resp.isSuccessful) {
            _productos.value = resp.body().orEmpty()
        } else {
            // logging o manejo de error
        }
    }
}