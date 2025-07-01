package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.*
import retrofit2.Response
import retrofit2.http.*

interface RestVenta {

    // Realizar checkout inicial (crear venta pendiente, sin pago)
    @POST("checkout")
    suspend fun realizarCheckout(@Body checkoutRequest: CheckoutRequest): Response<VentaDto>

    // Procesar el pago confirmando PaymentIntent
    @POST("ventas/{venta}/pagar")
    suspend fun procesarPago(
        @Path("venta") ventaId: Long,
        @Body pagoRequest: PagoRequest
    ): Response<VentaDto>

    // Obtener historial de compras
    @GET("compras")
    suspend fun listarCompras(): Response<List<VentaDto>>
}

