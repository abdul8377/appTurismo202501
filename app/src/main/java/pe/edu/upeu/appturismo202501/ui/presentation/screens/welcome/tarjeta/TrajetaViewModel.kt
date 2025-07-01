package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.tarjeta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.repository.PaymentIntentRepository
import javax.inject.Inject

class TarjetaViewModel @Inject constructor(
    private val paymentIntentRepository: PaymentIntentRepository
) : ViewModel() {

    private val _clientSecret = MutableStateFlow("Obteniendo client secret...")
    val clientSecret = _clientSecret.asStateFlow()

    init {
        obtenerClientSecret()
    }

    fun obtenerClientSecret() {
        viewModelScope.launch {
            try {
                // Usar el monto que necesitas
                val amount = 1000 // Por ejemplo, 10.00 USD (en centavos)
                val response = paymentIntentRepository.crearPaymentIntent(amount)
                if (response.isSuccessful && response.body() != null) {
                    _clientSecret.value = response.body()!!.clientSecret
                } else {
                    _clientSecret.value = "Error obteniendo clientSecret: ${response.errorBody()?.string() ?: "Error desconocido"}"
                }
            } catch (e: Exception) {
                _clientSecret.value = "Excepción: ${e.localizedMessage ?: "Error desconocido"}"
            }
        }
    }

    fun resetClientSecret() {
        _clientSecret.value = "Obteniendo nuevo client secret..."
        obtenerClientSecret()
    }
}
