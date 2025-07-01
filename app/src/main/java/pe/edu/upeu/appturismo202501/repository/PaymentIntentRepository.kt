package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestPaymentIntent
import pe.edu.upeu.appturismo202501.modelo.CreatePaymentIntentRequest
import pe.edu.upeu.appturismo202501.modelo.PaymentIntentResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Define las operaciones relacionadas con Stripe PaymentIntent.
 */
interface PaymentIntentRepository {
    /** Solicita al backend crear un nuevo PaymentIntent */
    suspend fun crearPaymentIntent(amount: Int): Response<PaymentIntentResponse>
}

/**
 * Implementación concreta que utiliza Retrofit (RestPaymentIntent)
 * para comunicarse con la API del servidor.
 */
@Singleton
class PaymentIntentRepositoryImpl @Inject constructor(
    private val restPaymentIntent: RestPaymentIntent
) : PaymentIntentRepository {

    override suspend fun crearPaymentIntent(amount: Int): Response<PaymentIntentResponse> {
        val request = CreatePaymentIntentRequest(amount = amount) // Crea el objeto request
        return restPaymentIntent.createPaymentIntent(request) // Pasa el request al backend
    }
}
