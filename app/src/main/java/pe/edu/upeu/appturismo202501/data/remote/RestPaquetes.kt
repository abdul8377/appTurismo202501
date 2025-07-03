package pe.edu.upeu.appturismo202501.data.remote


import pe.edu.upeu.appturismo202501.modelo.CreatePaqueteRequest
import pe.edu.upeu.appturismo202501.modelo.PaqueteDto
import pe.edu.upeu.appturismo202501.modelo.UpdatePaqueteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RestPaquetes {
    @GET("paquetes")
    suspend fun getAllPaquetes(): Response<List<PaqueteDto>>

    @GET("emprendedor/paquetes")
    suspend fun getMyPaquetes(): Response<List<PaqueteDto>>

    @GET("emprendedor/paquetes/{id}")
    suspend fun getPaquete(@Path("id") id: Long): Response<PaqueteDto>

    @POST("emprendedor/paquetes")
    suspend fun createPaquete(@Body req: CreatePaqueteRequest): Response<PaqueteDto>

    @PUT("emprendedor/paquetes/{id}")
    suspend fun updatePaquete(
        @Path("id") id: Long,
        @Body req: UpdatePaqueteRequest
    ): Response<PaqueteDto>

    @DELETE("emprendedor/paquetes/{id}")
    suspend fun deletePaquete(@Path("id") id: Long): Response<Unit>
}