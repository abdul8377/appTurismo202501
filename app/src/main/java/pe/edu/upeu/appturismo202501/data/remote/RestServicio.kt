package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.modelo.ServicioResp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestServicio {
    @GET("servicios")
    suspend fun getServicios(
        @Query("tipo_negocio_id") tipoNegocioId: Long
    ): Response<List<ServicioResp>>

    @GET("servicios/{id}")
    suspend fun getServicioDetalle(@Path("id") id: Long): Response<ServicioResp>
}