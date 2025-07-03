package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.carrito

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.repository.VentaRepository
import pe.edu.upeu.appturismo202501.modelo.*
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

// ViewModel para gestionar la lógica relacionada con las ventas
@HiltViewModel
class VentaViewModel @Inject constructor(
    private val ventaRepository: VentaRepository
) : ViewModel() {

    // Realizar el checkout
    fun realizarCheckout(ventaRequest: CheckoutRequest, onResult: (Response<VentaDto>) -> Unit) {
        viewModelScope.launch {
            try {
                // Verificar los datos del request (mostrar token en los logs)
                Log.d("CheckoutRequest", "Datos del checkout: ${ventaRequest.token}")

                // Llamar al repositorio para realizar el checkout
                val response = ventaRepository.realizarCheckout(ventaRequest)

                // Verificar la respuesta
                Log.d("CheckoutResponse", "Respuesta del checkout: ${response.body()}")

                // Pasar la respuesta a la función onResult
                onResult(response)
            } catch (e: Exception) {
                // Manejo de errores
                Log.e("CheckoutError", "Error en el checkout: ${e.message}")
                onResult(Response.error(500, "Error en el checkout".toResponseBody()))
            }
        }
    }



    // Obtener el historial de compras del usuario
    fun listarCompras(onResult: (Response<List<VentaDto>>) -> Unit) {
        viewModelScope.launch {
            try {
                // Llamar al repositorio para obtener el historial de compras
                val response = ventaRepository.listarCompras()
                onResult(response)
            } catch (e: Exception) {
                // Manejo de errores: si ocurre un error, se envía un mensaje
                onResult(Response.error(500, "Error al obtener compras".toResponseBody()))
            }
        }
    }
}
