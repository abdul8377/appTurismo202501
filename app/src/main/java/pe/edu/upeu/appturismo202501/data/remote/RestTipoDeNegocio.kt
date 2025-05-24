package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.Emprendimiento
import pe.edu.upeu.appturismo202501.modelo.TipoDeNegocio
import retrofit2.Response
import retrofit2.http.*

// Interface que define las rutas de la API relacionadas con TipoDeNegocio
interface RestTipoDeNegocio {

    @GET("tipos-de-negocio")
    suspend fun getTiposDeNegocio(): Response<List<TipoDeNegocio>>

    @GET("tipos-de-negocio/{id}")
    suspend fun getTipoDeNegocio(@Path("id") id: Long): Response<TipoDeNegocio>

    @POST("tipos-de-negocio")
    suspend fun createTipoDeNegocio(@Body tipoDeNegocio: TipoDeNegocio): Response<TipoDeNegocio>

    @PUT("tipos-de-negocio/{id}")
    suspend fun updateTipoDeNegocio(@Path("id") id: Long, @Body tipoDeNegocio: TipoDeNegocio): Response<TipoDeNegocio>

    @DELETE("tipos-de-negocio/{id}")
    suspend fun deleteTipoDeNegocio(@Path("id") id: Long): Response<Unit>

    // Nueva función para obtener los emprendimientos por tipo de negocio
    @GET("tipos-de-negocio/{id}/emprendimientos")
    suspend fun getEmprendimientosByTipo(@Path("id") tipoId: Long): Response<List<Emprendimiento>>

    // Nueva función para realizar la búsqueda de tipos de negocio
    @GET("tipos-de-negocio")
    suspend fun searchTiposDeNegocio(@Query("search") query: String): Response<List<TipoDeNegocio>>
}
