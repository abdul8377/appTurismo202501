package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestFavorito
import pe.edu.upeu.appturismo202501.modelo.FavoritoRequest
import pe.edu.upeu.appturismo202501.modelo.FavoritoResp
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Define las operaciones que podemos hacer sobre Favoritos.
 */
interface FavoritoRepository {
    /** Obtiene la lista de favoritos del usuario autenticado */
    suspend fun obtenerFavoritos(): Response<List<FavoritoResp>>

    /** Crea un nuevo favorito (producto o servicio) */
    suspend fun crearFavorito(favoritoRequest: FavoritoRequest): Response<FavoritoResp>

    /** Elimina un favorito por su ID */
    suspend fun eliminarFavorito(favoritoId: Long): Response<Unit>
}

/**
 * Implementación concreta que usa Retrofit (RestFavorito) para
 * realizar las llamadas a la API de Favoritos.
 */
@Singleton
class FavoritoRepositoryImp @Inject constructor(
    private val restFavorito: RestFavorito
) : FavoritoRepository {

    override suspend fun obtenerFavoritos(): Response<List<FavoritoResp>> =
        restFavorito.getFavoritos()

    override suspend fun crearFavorito(favoritoRequest: FavoritoRequest): Response<FavoritoResp> =
        restFavorito.crearFavorito(favoritoRequest)

    override suspend fun eliminarFavorito(favoritoId: Long): Response<Unit> =
        restFavorito.eliminarFavorito(favoritoId)
}
