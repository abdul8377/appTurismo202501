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
import pe.edu.upeu.appturismo202501.modelo.CarritoRespUi
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

    private val _carritoItemsUi = MutableStateFlow<List<CarritoRespUi>>(emptyList())
    val carritoItemsUi: StateFlow<List<CarritoRespUi>> = _carritoItemsUi

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
            cargarDetallesItems(response.body().orEmpty())
        }
    }

    private suspend fun obtenerCarritoLocal() {
        val items = carritoLocalStorage.obtenerCarritoLocal()
        cargarDetallesItems(items)
    }

    private suspend fun cargarDetallesItems(items: List<CarritoResp>) {
        val itemsUi = items.map { item ->
            try {
                when {
                    item.productosId != null -> {
                        val productoResponse = productoRepository.productoDetalle(item.productosId)
                        val producto = productoResponse.body()?.producto
                        CarritoRespUi(carritoResp = item, producto = producto)
                    }
                    item.serviciosId != null -> {
                        val servicioResponse = servicioRepository.servicioDetalle(item.serviciosId)
                        val servicio = servicioResponse.body()
                        CarritoRespUi(carritoResp = item, servicio = servicio)
                    }
                    else -> CarritoRespUi(carritoResp = item)
                }
            } catch (e: Exception) {
                CarritoRespUi(carritoResp = item)
            }
        }
        _carritoItemsUi.value = itemsUi
    }

    fun agregarAlCarrito(carritoDto: CarritoDto, stockDisponible: Int? = null) {
        viewModelScope.launch {
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

    fun actualizarCantidad(carritoItem: CarritoResp, nuevaCantidad: Int, stockDisponible: Int) {
        viewModelScope.launch {
            if (nuevaCantidad < 1 || nuevaCantidad > stockDisponible) return@launch

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
                carritoLocalStorage.guardarItemCarritoLocal(carritoDto, stockDisponible)
            }

            cargarCarrito()
        }
    }
}