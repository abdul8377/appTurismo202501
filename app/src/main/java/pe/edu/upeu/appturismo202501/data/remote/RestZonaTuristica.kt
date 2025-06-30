package pe.edu.upeu.appturismo202501.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaDto
import retrofit2.Response
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaResp
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RestZonaTuristica {
    @GET("zonas-turisticas")
    suspend fun getZonas(): Response<List<ZonaTuristicaResp>>


    @GET("zonas-turisticas/{id}")
    suspend fun getZonasById(@Path("id") id: Long): Response<ZonaTuristicaResp>

//    @GET("emprendedor/servicios")
//    suspend fun getServiciosEmprendedor(): Response<List<ServicioDto>>

    @Multipart
    @POST("zonas-turisticas")
    suspend fun createZonas(
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("ubicacion") ubicacion: RequestBody?,
        @Part("estado") estado: RequestBody?,
        @Part imagen: List<MultipartBody.Part>?
    ): Response<ZonaTuristicaDto>


    @Multipart
    @POST("zonas-turisticas/{id}")
    suspend fun updateZonas(
        @Path("id") id: Long,
        @Part("_method") method: RequestBody,
        @Part("nombre") nombre: RequestBody?,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("ubicacion") ubicacion: RequestBody?,
        @Part("estado") estado: RequestBody?,
        @Part("imagen_a_eliminar[]") eliminarImagen: List<@JvmSuppressWildcards RequestBody>?,
        @Part images: List<MultipartBody.Part>?
    ): Response<ZonaTuristicaDto>

    /** Eliminar un servicio */
    @DELETE("zonas-turisticas/{id}")
    suspend fun deleteZonas(@Path("id") id: Long): Response<Unit>
}