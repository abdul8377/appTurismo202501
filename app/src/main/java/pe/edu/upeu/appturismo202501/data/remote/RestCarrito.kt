package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.CarritoDto
import pe.edu.upeu.appturismo202501.modelo.CarritoResp
import retrofit2.Response
import retrofit2.http.*

interface RestCarrito {

    // Ver el carrito del usuario
    @GET("carrito")
    suspend fun getCarrito(): Response<List<CarritoResp>>

    // Agregar un ítem al carrito
    @POST("carrito")
    suspend fun agregarItemCarrito(@Body carritoDto: CarritoDto): Response<CarritoResp>

    // Actualizar un ítem en el carrito
    @PUT("carrito/{carritoId}")
    suspend fun actualizarItemCarrito(
        @Path("carritoId") carritoId: Long,
        @Body carritoDto: CarritoDto
    ): Response<CarritoResp>

    // Eliminar un ítem del carrito
    @DELETE("carrito/{carritoId}")
    suspend fun eliminarItemCarrito(@Path("carritoId") carritoId: Long): Response<Void>
}
