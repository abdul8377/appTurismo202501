package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestTipoDeNegocio
import pe.edu.upeu.appturismo202501.modelo.Emprendimiento
import pe.edu.upeu.appturismo202501.modelo.TipoDeNegocio
import retrofit2.Response
import javax.inject.Inject

interface TipoDeNegocioRepository {
    suspend fun getTiposDeNegocio(): Response<List<TipoDeNegocio>>
    suspend fun getTipoDeNegocio(id: Long): Response<TipoDeNegocio>
    suspend fun createTipoDeNegocio(tipoDeNegocio: TipoDeNegocio): Response<TipoDeNegocio>
    suspend fun updateTipoDeNegocio(id: Long, tipoDeNegocio: TipoDeNegocio): Response<TipoDeNegocio>
    suspend fun deleteTipoDeNegocio(id: Long): Response<Unit>
    suspend fun searchTiposDeNegocio(query: String): Response<List<TipoDeNegocio>> // Nueva función

    // Obtener los emprendimientos vinculados a un tipo de negocio
    suspend fun getEmprendimientosByTipo(tipoId: Long): Response<List<Emprendimiento>>
}

class TipoDeNegocioRepositoryImpl @Inject constructor(
    private val restTipoDeNegocio: RestTipoDeNegocio
) : TipoDeNegocioRepository {

    override suspend fun getTiposDeNegocio(): Response<List<TipoDeNegocio>> {
        return try {
            restTipoDeNegocio.getTiposDeNegocio()
        } catch (e: Exception) {
            Response.error(500, null) // Manejo de errores
        }
    }

    override suspend fun getTipoDeNegocio(id: Long): Response<TipoDeNegocio> {
        return try {
            restTipoDeNegocio.getTipoDeNegocio(id)
        } catch (e: Exception) {
            Response.error(500, null) // Manejo de errores
        }
    }

    override suspend fun createTipoDeNegocio(tipoDeNegocio: TipoDeNegocio): Response<TipoDeNegocio> {
        return try {
            restTipoDeNegocio.createTipoDeNegocio(tipoDeNegocio)
        } catch (e: Exception) {
            Response.error(500, null) // Manejo de errores
        }
    }

    override suspend fun updateTipoDeNegocio(id: Long, tipoDeNegocio: TipoDeNegocio): Response<TipoDeNegocio> {
        return try {
            restTipoDeNegocio.updateTipoDeNegocio(id, tipoDeNegocio)
        } catch (e: Exception) {
            Response.error(500, null) // Manejo de errores
        }
    }

    override suspend fun deleteTipoDeNegocio(id: Long): Response<Unit> {
        return try {
            restTipoDeNegocio.deleteTipoDeNegocio(id)
        } catch (e: Exception) {
            Response.error(500, null) // Manejo de errores
        }
    }

    override suspend fun getEmprendimientosByTipo(tipoId: Long): Response<List<Emprendimiento>> {
        return try {
            restTipoDeNegocio.getEmprendimientosByTipo(tipoId) // Asegúrate de tener esta función en la API
        } catch (e: Exception) {
            Response.error(500, null) // Manejo de errores
        }
    }

    override suspend fun searchTiposDeNegocio(query: String): Response<List<TipoDeNegocio>> {
        return try {
            restTipoDeNegocio.searchTiposDeNegocio(query) // Asegúrate de tener esta función en la API
        } catch (e: Exception) {
            Response.error(500, null) // Manejo de errores
        }
    }
}