package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.data.local.storage.CarritoLocalStorage
import pe.edu.upeu.appturismo202501.modelo.CarritoDto
import pe.edu.upeu.appturismo202501.modelo.CarritoResp
import pe.edu.upeu.appturismo202501.repository.CarritoRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository,
    private val carritoLocalStorage: CarritoLocalStorage
) : ViewModel() {

    private val _carritoState = MutableStateFlow<List<CarritoResp>>(emptyList())
    val carritoState: StateFlow<List<CarritoResp>> = _carritoState

    init {
        cargarCarrito()
    }

    fun cargarCarrito() {
        viewModelScope.launch {
            if (SessionManager.isLoggedIn()) {
                sincronizarCarritoLocal()
                obtenerCarritoRemoto()
            } else {
                obtenerCarritoLocal()
            }
        }
    }

    private suspend fun obtenerCarritoRemoto() {
        val response = carritoRepository.obtenerCarrito()
        if (response.isSuccessful) {
            _carritoState.value = response.body().orEmpty()
        }
    }

    private suspend fun obtenerCarritoLocal() {
        _carritoState.value = carritoLocalStorage.obtenerCarritoLocal()
    }

    fun agregarAlCarrito(carritoDto: CarritoDto) {
        viewModelScope.launch {
            if (SessionManager.isLoggedIn()) {
                val carritoActual = carritoState.value
                val productoExistente = carritoActual.find {
                    it.productosId == carritoDto.productosId && it.serviciosId == carritoDto.serviciosId
                }

                if (productoExistente != null) {
                    // Producto ya existe, aumenta la cantidad
                    val nuevaCantidad = productoExistente.cantidad + carritoDto.cantidad
                    val dtoActualizado = carritoDto.copy(
                        cantidad = nuevaCantidad,
                        subtotal = carritoDto.precioUnitario * nuevaCantidad
                    )

                    carritoRepository.actualizarItemEnCarrito(productoExistente.carritoId, dtoActualizado)
                } else {
                    // Producto nuevo, añade al carrito
                    carritoRepository.agregarItemAlCarrito(carritoDto)
                }
                cargarCarrito()

            } else {
                // Para almacenamiento local
                val carritoActualLocal = carritoLocalStorage.obtenerCarritoLocal()
                val productoExistente = carritoActualLocal.find {
                    it.productosId == carritoDto.productosId && it.serviciosId == carritoDto.serviciosId
                }

                if (productoExistente != null) {
                    // Producto existe localmente, actualiza cantidad
                    val nuevaCantidad = productoExistente.cantidad + carritoDto.cantidad
                    val dtoActualizado = carritoDto.copy(
                        cantidad = nuevaCantidad,
                        subtotal = carritoDto.precioUnitario * nuevaCantidad
                    )

                    carritoLocalStorage.eliminarItemCarritoLocal(productoExistente.carritoId)
                    carritoLocalStorage.guardarItemCarritoLocal(dtoActualizado)
                } else {
                    carritoLocalStorage.guardarItemCarritoLocal(carritoDto)
                }
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

    // Método para cambiar cantidad (incrementar o disminuir)
    fun actualizarCantidad(carritoItem: CarritoResp, nuevaCantidad: Int, stockDisponible: Int) {
        viewModelScope.launch {
            if (nuevaCantidad < 1 || nuevaCantidad > stockDisponible) return@launch // Evita cambios inválidos

            val carritoDto = CarritoDto(
                userId = carritoItem.userId,
                productosId = carritoItem.productosId,
                serviciosId = carritoItem.serviciosId,
                cantidad = nuevaCantidad,
                precioUnitario = carritoItem.precioUnitario,
                subtotal = carritoItem.precioUnitario * nuevaCantidad,
                estado = carritoItem.estado
            )

            if (SessionManager.isLoggedIn()) {
                carritoRepository.actualizarItemEnCarrito(carritoItem.carritoId, carritoDto)
            } else {
                carritoLocalStorage.eliminarItemCarritoLocal(carritoItem.carritoId)
                carritoLocalStorage.guardarItemCarritoLocal(carritoDto)
            }

            cargarCarrito()
        }
    }
}
