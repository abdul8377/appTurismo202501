package pe.edu.upeu.appturismo202501.data.remote


import pe.edu.upeu.appturismo202501.modelo.DisponibilidadDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response

interface RestDisponibilidad {
    @GET("api/disponibilidad/{servicioId}")
    suspend fun getDisponibilidades(
        @Path("servicioId") servicioId: Long
    ): Response<List<DisponibilidadDto>>

    @POST("api/disponibilidad/{servicioId}")
    suspend fun createDisponibilidad(
        @Path("servicioId") servicioId: Long,
        @Body nueva: DisponibilidadDto
    ): Response<DisponibilidadDto>

    @DELETE("api/disponibilidad/{id}")
    suspend fun deleteDisponibilidad(
        @Path("id") id: Long
    ): Response<Unit>
}