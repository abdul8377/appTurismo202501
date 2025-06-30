package pe.edu.upeu.appturismo202501.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.modelo.ServicioDto
import pe.edu.upeu.appturismo202501.modelo.ServicioResp
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RestServicio {
    @GET("servicios")
    suspend fun getServicios(
        @Query("tipo_negocio_id") tipoNegocioId: Long
    ): Response<List<ServicioResp>>

    @GET("servicios/{id}")
    suspend fun getServicioDetalle(@Path("id") id: Long): Response<ServicioResp>

    @GET("emprendedor/servicios")
    suspend fun getServiciosEmprendedor(): Response<List<ServicioDto>>

    @Multipart
    @POST("emprendedor/servicios")
    suspend fun createServicio(
        @Part("nombre")             nombre: RequestBody,
        @Part("descripcion")        descripcion: RequestBody?,
        @Part("precio")             precio: RequestBody,
        @Part("capacidad_maxima")   capacidadMaxima: RequestBody,
        @Part("duracion_servicio")  duracionServicio: RequestBody?,
        @Part("estado")             estado: RequestBody?,
        @Part images: List<MultipartBody.Part>?         // ← aquí
    ): Response<ServicioDto>

    /** Actualizar un servicio */
    @Multipart
    @POST("emprendedor/servicios/{id}")
    suspend fun updateServicio(
        @Path("id") id: Long,
        @Part("_method") method: RequestBody,
        @Part("nombre") nombre: RequestBody?,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("precio") precio: RequestBody?,
        @Part("capacidad_maxima") capacidad: RequestBody?,
        @Part("duracion_servicio") duracion_servicio: RequestBody?,
        @Part("estado") estado: RequestBody?,
        @Part("imagenes_a_eliminar[]") eliminarImagenes: List<@JvmSuppressWildcards RequestBody>?, // <<-- LOS CORCHETES SON OBLIGATORIOS PARA ARRAY
        @Part images: List<MultipartBody.Part>?
    ): Response<ServicioDto>

    /** Eliminar un servicio */
    @DELETE("emprendedor/servicios/{id}")
    suspend fun deleteServicio(@Path("id") id: Long): Response<Unit>
}
