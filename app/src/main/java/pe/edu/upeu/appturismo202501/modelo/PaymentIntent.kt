package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

// Representa la respuesta del servidor al crear un PaymentIntent
data class PaymentIntentResponse(
    @SerializedName("clientSecret")
    val clientSecret: String
)

data class CreatePaymentIntentRequest(
    val amount: Int, // Stripe espera los montos en la unidad más pequeña, ej. centavos
)
