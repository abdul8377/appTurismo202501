package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.MetodoPagoResp
import pe.edu.upeu.appturismo202501.modelo.MetodoPagoDto
import retrofit2.Response
import retrofit2.http.*

interface RestMetodoPago {

    // Listar todos los métodos de pago
    @GET("metodo-pago")
    suspend fun getMetodosPago(): Response<List<MetodoPagoResp>>

    // Crear un nuevo método de pago
    @POST("metodo-pago")
    suspend fun createMetodoPago(@Body metodoPagoDto: MetodoPagoDto): Response<MetodoPagoResp>

    // Obtener un método de pago específico
    @GET("metodo-pago/{id}")
    suspend fun getMetodoPago(@Path("id") id: Long): Response<MetodoPagoResp>

    // Actualizar un método de pago
    @PUT("metodo-pago/{id}")
    suspend fun updateMetodoPago(
        @Path("id") id: Long,
        @Body metodoPagoDto: MetodoPagoDto
    ): Response<MetodoPagoResp>

    // Eliminar un método de pago
    @DELETE("metodo-pago/{id}")
    suspend fun deleteMetodoPago(@Path("id") id: Long): Response<Void>

    // Suspender un método de pago
    @PATCH("metodo-pago/{id}/suspend")
    suspend fun suspendMetodoPago(@Path("id") id: Long): Response<MetodoPagoResp>

    // Activar un método de pago
    @PATCH("metodo-pago/{id}/activate")
    suspend fun activateMetodoPago(@Path("id") id: Long): Response<MetodoPagoResp>
}
