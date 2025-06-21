package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestServicio
import pe.edu.upeu.appturismo202501.modelo.ServicioResp
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Define las operaciones disponibles para los servicios.
 */
interface ServicioRepository {
    /**
     * Obtiene la lista de servicios filtrados por tipo de negocio.
     *
     * @param tipoNegocioId ID del tipo de negocio.
     * @return Lista de [ServicioResp] envuelta en [Response].
     */
    suspend fun servicioFetch(tipoNegocioId: Long): Response<List<ServicioResp>>

    /**
     * Obtiene el detalle de un servicio por su ID.
     *
     * @param id ID del servicio.
     * @return Detalle de servicio envuelto en [Response].
     */
    suspend fun servicioDetalle(id: Long): Response<ServicioResp>
}

/**
 * Implementación de [ServicioRepository] que usa Retrofit para comunicarse con la API.
 */
@Singleton
class ServicioRepositoryImp @Inject constructor(
    private val restServicio: RestServicio
) : ServicioRepository {

    override suspend fun servicioFetch(tipoNegocioId: Long): Response<List<ServicioResp>> =
        restServicio.getServicios(tipoNegocioId)

    override suspend fun servicioDetalle(id: Long): Response<ServicioResp> =
        restServicio.getServicioDetalle(id)
}
