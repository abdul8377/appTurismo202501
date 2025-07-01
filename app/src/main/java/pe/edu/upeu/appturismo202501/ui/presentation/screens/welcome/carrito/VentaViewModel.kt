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
    fun realizarCheckout(ventaRequest: CheckoutRequest, onResult: (Response<VentaDto>) -> Unit) {
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

    // Procesar pago con el PaymentIntent ID
    fun procesarPagoConToken(paymentIntentId: String, ventaId: Long, onResult: (Response<VentaDto>) -> Unit) {
        viewModelScope.launch {
            try {
                // Crear un objeto PagoRequest con el paymentIntentId y otros detalles necesarios
                val pagoRequest = PagoRequest(paymentIntentId = paymentIntentId)

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
