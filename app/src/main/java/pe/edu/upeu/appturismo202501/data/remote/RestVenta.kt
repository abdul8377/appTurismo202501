package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.*
import retrofit2.Response
import retrofit2.http.*

interface RestVenta {

    // Realizar checkout inicial (crear venta pendiente, sin pago)
    // Realizar checkout inicial (crear venta pendiente, sin pago)
    @POST("venta")  // Ruta para crear la venta (Checkout)
    suspend fun realizarCheckout(@Body checkoutRequest: CheckoutRequest): Response<VentaDto>
    // Obtener historial de compras del usuario
    @GET("ventas")  // Ruta para obtener el historial de compras
    suspend fun listarCompras(): Response<List<VentaDto>>
}
