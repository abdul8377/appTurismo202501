package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestProductos
import pe.edu.upeu.appturismo202501.data.remote.RestServicio
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.modelo.ServicioResp
import retrofit2.Response
import javax.inject.Inject

interface ServicioRepository {
    suspend fun servicioFetch(tipoNegocioId: Long): Response<List<ServicioResp>>
    suspend fun servicioDetalle(id: Long): Response<ServicioResp>
}

class ServicioRepositoryImp @Inject constructor(
    private val restServicio: RestServicio
) : ServicioRepository {
    override suspend fun servicioFetch(tipoNegocioId: Long): Response<List<ServicioResp>> {
        return restServicio.getServicios(tipoNegocioId)
    }
    override suspend fun servicioDetalle(id: Long): Response<ServicioResp> {
        return restServicio.getServicioDetalle(id)
    }
}