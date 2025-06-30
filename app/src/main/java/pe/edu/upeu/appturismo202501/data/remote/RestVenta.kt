package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.*
import retrofit2.Response
import retrofit2.http.*

interface RestVenta {

    // Realizar el checkout: crea una venta y movimientos contables
    @POST("checkout")
    suspend fun realizarCheckout(@Body ventaRequest: VentaDto): Response<VentaDto>

    // Procesar el pago de una venta (Stripe)
    @POST("ventas/{venta}/pagar")
    suspend fun procesarPago(
        @Path("venta") ventaId: Long,
        @Body pagoRequest: PagoDto
    ): Response<PagoDto>

    // Obtener el historial de compras del usuario
    @GET("compras")
    suspend fun listarCompras(): Response<List<VentaDto>>

}
