package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import pe.edu.upeu.appturismo202501.repository.VentaRepository
import pe.edu.upeu.appturismo202501.modelo.*
import retrofit2.Response
import java.math.BigDecimal
import javax.inject.Inject

// ViewModel para gestionar la lógica relacionada con las ventas
class VentaViewModel @Inject constructor(
    private val ventaRepository: VentaRepository
) : ViewModel() {

    // Llamar al método de realizar el checkout
    fun realizarCheckout(ventaRequest: VentaDto, onResult: (Response<VentaDto>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ventaRepository.realizarCheckout(ventaRequest)
                onResult(response)
            } catch (e: Exception) {
                // Manejo de errores
                onResult(Response.error(500, "Error en el checkout".toResponseBody()))
            }
        }
    }

    // Procesar pago con el token de Stripe
    fun procesarPagoConToken(token: String, ventaId: Long, monto: BigDecimal, metodoPagoId: Long, userId: Long, referencia: String, onResult: (Response<PagoDto>) -> Unit) {
        viewModelScope.launch {
            try {
                // Crear un objeto PagoDto con el token y otros detalles necesarios
                val pagoRequest = PagoDto(
                    pagoId = 0,  // Esto es solo un valor predeterminado, el ID del pago lo genera el backend
                    metodoPagoId = metodoPagoId,
                    userId = userId,
                    monto = monto,
                    referencia = referencia,
                    estado = "pendiente",  // El estado podría ser "pendiente" mientras se espera la confirmación del pag
                )

                // Llamar al repositorio para procesar el pago
                val response = ventaRepository.procesarPago(ventaId, pagoRequest)
                onResult(response)
            } catch (e: Exception) {
                // Manejo de errores
                onResult(Response.error(500, "Error en el pago".toResponseBody()))
            }
        }

    }

    // Obtener el historial de compras del usuario
    fun listarCompras(onResult: (Response<List<VentaDto>>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = ventaRepository.listarCompras()
                onResult(response)
            } catch (e: Exception) {
                // Manejo de errores
                onResult(Response.error(500, "Error al obtener compras".toResponseBody()))
            }
        }
    }


}
