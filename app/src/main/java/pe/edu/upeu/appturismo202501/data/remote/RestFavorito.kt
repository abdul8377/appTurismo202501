package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.FavoritoRequest
import pe.edu.upeu.appturismo202501.modelo.FavoritoResp
import retrofit2.Response
import retrofit2.http.*

interface RestFavorito {

    @GET("favoritos")
    suspend fun getFavoritos(): Response<List<FavoritoResp>>

    @POST("favoritos")
    suspend fun crearFavorito(@Body favoritoRequest: FavoritoRequest): Response<FavoritoResp>

    @DELETE("favoritos/{id}")
    suspend fun eliminarFavorito(@Path("id") favoritoId: Long): Response<Unit>

}
