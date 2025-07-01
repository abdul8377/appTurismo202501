package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.data.local.storage.CarritoLocalStorage
import pe.edu.upeu.appturismo202501.modelo.CarritoDto
import pe.edu.upeu.appturismo202501.modelo.CarritoItemUi
import pe.edu.upeu.appturismo202501.modelo.CarritoResp
import pe.edu.upeu.appturismo202501.modelo.CarritoRespUi
import pe.edu.upeu.appturismo202501.modelo.toCarritoItemUi
import pe.edu.upeu.appturismo202501.modelo.toProductoUiCart
import pe.edu.upeu.appturismo202501.repository.CarritoRepository
import pe.edu.upeu.appturismo202501.repository.ProductoRespository
import pe.edu.upeu.appturismo202501.repository.ServicioRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository,
    private val carritoLocalStorage: CarritoLocalStorage,
    private val productoRepository: ProductoRespository,
    private val servicioRepository: ServicioRepository
) : ViewModel() {

    // Exponemos la UI pura
    private val _itemsUi = MutableStateFlow<List<CarritoItemUi>>(emptyList())
    val itemsUi: StateFlow<List<CarritoItemUi>> = _itemsUi

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Mensaje de estado
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init { cargarCarrito() }


    fun cargarCarrito() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val rawItems = if (SessionManager.isLoggedIn()) {
                carritoRepository.obtenerCarrito()
                    .body()
                    .orEmpty()
                    .also { sincronizarCarritoLocal() }
            } else {
                carritoLocalStorage.obtenerCarritoLocal()
            }

            // Sólo aquí: carga y mapea TODOS los items (producto + servicio)
            cargarDetallesItems(rawItems)
        } catch (e: Exception) {
            _message.value = "Error al cargar el carrito: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun obtenerCarritoRemoto() {
        val response = carritoRepository.obtenerCarrito()
        if (response.isSuccessful) {
            cargarDetallesItems(response.body().orEmpty())
        }
    }

    private suspend fun obtenerCarritoLocal() {
        val items = carritoLocalStorage.obtenerCarritoLocal()
        cargarDetallesItems(items)
    }

    private suspend fun cargarDetallesItems(items: List<CarritoResp>) {
        val itemsUi = items.mapNotNull { resp ->
            try {
                when {
                    resp.productosId != null -> {
                        val pr = productoRepository.productoDetalle(resp.productosId)
                            .body()?.producto ?: return@mapNotNull null
                        resp.toCarritoItemUi(pr.toProductoUiCart())
                    }
                    else -> null
                }
            } catch (e: Exception) {
                null
            }
        }
        _itemsUi.value = itemsUi
    }

    fun agregarAlCarrito(carritoDto: CarritoDto, stockDisponible: Int? = null) {
        viewModelScope.launch {
            try {
                val carritoActual = if (SessionManager.isLoggedIn()) {
                    carritoRepository.obtenerCarrito().body().orEmpty()
                } else {
                    carritoLocalStorage.obtenerCarritoLocal()
                }

                val productoExistente = carritoActual.find {
                    it.productosId == carritoDto.productosId && it.serviciosId == carritoDto.serviciosId
                }

                if (productoExistente != null) {
                    val nuevaCantidad = (productoExistente.cantidad + carritoDto.cantidad).let { cantidad ->
                        if (stockDisponible != null) minOf(cantidad, stockDisponible) else cantidad
                    }

                    val dtoActualizado = carritoDto.copy(
                        cantidad = nuevaCantidad,
                        subtotal = carritoDto.precioUnitario * nuevaCantidad
                    )

                    if (SessionManager.isLoggedIn()) {
                        carritoRepository.actualizarItemEnCarrito(productoExistente.carritoId, dtoActualizado)
                    } else {
                        carritoLocalStorage.eliminarItemCarritoLocal(productoExistente.carritoId)
                        carritoLocalStorage.guardarItemCarritoLocal(dtoActualizado, stockDisponible)
                    }
                } else {
                    if (SessionManager.isLoggedIn()) {
                        carritoRepository.agregarItemAlCarrito(carritoDto)
                    } else {
                        carritoLocalStorage.guardarItemCarritoLocal(carritoDto, stockDisponible)
                    }
                }
                cargarCarrito()
            } catch (e: Exception) {
                // En caso de error, intentar recargar el carrito
                cargarCarrito()
            }
        }
    }

    private suspend fun sincronizarCarritoLocal() {
        val itemsLocales = carritoLocalStorage.obtenerCarritoLocal()

        itemsLocales.forEach { resp ->
            val dto = CarritoDto(
                userId = SessionManager.getUserId().toLong(),
                productosId = resp.productosId,
                serviciosId = resp.serviciosId,
                cantidad = resp.cantidad,
                precioUnitario = resp.precioUnitario,
                subtotal = resp.subtotal,
                totalCarrito = resp.totalCarrito,
                estado = resp.estado
            )
            carritoRepository.agregarItemAlCarrito(dto)
        }

        carritoLocalStorage.limpiarCarritoLocal()
    }

    fun eliminarDelCarrito(carritoId: Long) {
        viewModelScope.launch {
            if (SessionManager.isLoggedIn()) {
                carritoRepository.eliminarItemDelCarrito(carritoId)
            } else {
                carritoLocalStorage.eliminarItemCarritoLocal(carritoId)
            }
            cargarCarrito()
        }
    }


    fun actualizarCantidad(
        itemUi: CarritoItemUi,
        nuevaCantidad: Int,
        stockDisponible: Int
    ) {
        viewModelScope.launch {
            // 1) Construye el DTO usando los campos que tiene CarritoItemUi
            val dto = CarritoDto(
                userId          = SessionManager.getUserId().toLong(),
                productosId     = itemUi.productosId,    // tendrás que añadir estos props a CarritoItemUi
                cantidad        = nuevaCantidad,
                precioUnitario  = itemUi.unitPrice,
                subtotal        = itemUi.unitPrice * nuevaCantidad,
                totalCarrito    = null,
                estado          = itemUi.estado
            )

            if (SessionManager.isLoggedIn()) {
                carritoRepository.actualizarItemEnCarrito(itemUi.carritoId, dto)
            } else {
                carritoLocalStorage.eliminarItemCarritoLocal(itemUi.carritoId)
                carritoLocalStorage.guardarItemCarritoLocal(dto, stockDisponible)
            }

            // 2) Recarga la lista
            cargarCarrito()
        }
    }

    fun clearCart() = viewModelScope.launch {
        _isLoading.value = true
        try {
            if (SessionManager.isLoggedIn()) {
                itemsUi.value.map { it.carritoId }
                    .forEach { carritoRepository.eliminarItemDelCarrito(it) }
            } else {
                carritoLocalStorage.limpiarCarritoLocal()
            }
            // Limpiar inmediatamente el estado UI
            _itemsUi.value = emptyList()
            _message.value = "Carrito vaciado correctamente"
        } catch (e: Exception) {
            _message.value = "Error al vaciar el carrito: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}