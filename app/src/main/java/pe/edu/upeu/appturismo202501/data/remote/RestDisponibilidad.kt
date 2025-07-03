package pe.edu.upeu.appturismo202501.data.remote


import pe.edu.upeu.appturismo202501.modelo.DisponibilidadDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Response

interface RestDisponibilidad {
    @GET("emprendedor/servicios/{servicioId}/disponibilidades")
    suspend fun getDisponibilidades(
        @Path("servicioId") servicioId: Long
    ): Response<List<DisponibilidadDto>>

    @POST("emprendedor/servicios/{servicioId}/disponibilidades")
    suspend fun createDisponibilidad(
        @Path("servicioId") servicioId: Long,
        @Body nueva: DisponibilidadDto
    ): Response<DisponibilidadDto>

    @DELETE("emprendedor/disponibilidades/{disponibilidadId}")
    suspend fun deleteDisponibilidad(
        @Path("disponibilidadId") disponibilidadId: Long
    ): Response<Void>
}