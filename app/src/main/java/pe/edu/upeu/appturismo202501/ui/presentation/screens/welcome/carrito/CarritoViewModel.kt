package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import pe.edu.upeu.appturismo202501.data.local.storage.CarritoLocalStorage
import pe.edu.upeu.appturismo202501.modelo.CarritoDto
import pe.edu.upeu.appturismo202501.modelo.CarritoItemUi
import pe.edu.upeu.appturismo202501.modelo.CarritoResp
import pe.edu.upeu.appturismo202501.modelo.CarritoRespUi
import pe.edu.upeu.appturismo202501.modelo.CheckoutRequest
import pe.edu.upeu.appturismo202501.modelo.VentaDto
import pe.edu.upeu.appturismo202501.modelo.toCarritoItemUi
import pe.edu.upeu.appturismo202501.modelo.toProductoUiCart
import pe.edu.upeu.appturismo202501.repository.CarritoRepository
import pe.edu.upeu.appturismo202501.repository.ProductoRespository
import pe.edu.upeu.appturismo202501.repository.ServicioRepository
import pe.edu.upeu.appturismo202501.repository.VentaRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository,
    private val carritoLocalStorage: CarritoLocalStorage,
    private val productoRepository: ProductoRespository,
    private val servicioRepository: ServicioRepository,
    private val ventaRepository: VentaRepository

) : ViewModel() {

    private val _itemsUi = MutableStateFlow<List<CarritoItemUi>>(emptyList())
    val itemsUi: StateFlow<List<CarritoItemUi>> = _itemsUi

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init { cargarCarrito() }

    fun cargarCarrito() = viewModelScope.launch {
        _isLoading.value = true
        try {
            val rawItems = if (SessionManager.isLoggedIn()) {
                // Filtramos solo los productos que están en estado "en proceso"
                carritoRepository.obtenerCarrito()
                    .body()
                    .orEmpty()
                    .filter { it.estado == "en proceso" }  // Filtramos por estado
                    .also { sincronizarCarritoLocal() }
            } else {
                carritoLocalStorage.obtenerCarritoLocal()
                    .filter { it.estado == "en proceso" }  // Filtramos por estado
            }

            cargarDetallesItems(rawItems)
        } catch (e: Exception) {
            _message.value = "Error al cargar el carrito: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    // CarritoViewModel
// CarritoViewModel
    // Llamar al API para realizar el checkout
    fun realizarCheckout(onResult: (Response<VentaDto>) -> Unit) {
        viewModelScope.launch {
            try {
                // Token simulado (en producción, este token debe ser dinámico)
                val token = "tok_visa"

                // Crear el objeto CheckoutRequest con el token
                val checkoutRequest = CheckoutRequest(token)

                // Llamar a la API para realizar el checkout
                val response = ventaRepository.realizarCheckout(checkoutRequest)

                // Pasar la respuesta al callback
                onResult(response)
            } catch (e: Exception) {
                // Manejo de errores
                Log.e("CheckoutError", "Error en el checkout: ${e.message}")
                onResult(Response.error(500, "Error en el checkout".toResponseBody()))
            }
        }
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
                    // La cantidad no puede ser menor a 1
                    val nuevaCantidad = (productoExistente.cantidad + carritoDto.cantidad).let { cantidad ->
                        if (stockDisponible != null) {
                            // Asegura que la cantidad no supere el stock disponible
                            minOf(cantidad, stockDisponible)
                        } else {
                            cantidad
                        }
                    }

                    // Si la nueva cantidad es 0 o negativa, no hacer nada
                    if (nuevaCantidad < 1) return@launch

                    val dtoActualizado = carritoDto.copy(
                        cantidad = nuevaCantidad,
                        subtotal = carritoDto.precioUnitario * nuevaCantidad
                    )

                    // Actualizar en el carrito remoto y local
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
                cargarCarrito() // Intentar recargar el carrito en caso de error
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
            // No permitir que la cantidad sea menor a 1
            if (nuevaCantidad < 1) return@launch

            // No permitir que la cantidad supere el stock
            val cantidadFinal = minOf(nuevaCantidad, stockDisponible)

            val dto = CarritoDto(
                userId = SessionManager.getUserId().toLong(),
                productosId = itemUi.productosId,
                cantidad = cantidadFinal,
                precioUnitario = itemUi.unitPrice,
                subtotal = itemUi.unitPrice * cantidadFinal,
                totalCarrito = null,
                estado = itemUi.estado
            )

            // Actualizar carrito local o remoto
            if (SessionManager.isLoggedIn()) {
                carritoRepository.actualizarItemEnCarrito(itemUi.carritoId, dto)
            } else {
                carritoLocalStorage.eliminarItemCarritoLocal(itemUi.carritoId)
                carritoLocalStorage.guardarItemCarritoLocal(dto, stockDisponible)
            }

            // Recargar la lista de items
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

