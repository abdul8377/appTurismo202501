package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.PaymentIntentResponse
import retrofit2.Response
import retrofit2.http.POST

interface RestPaymentIntent {

    @POST("create-payment-intent")
    suspend fun createPaymentIntent(): Response<PaymentIntentResponse>

}
