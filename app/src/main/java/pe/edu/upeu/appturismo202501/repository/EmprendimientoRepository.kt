package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.EmprendimientoRequest
import pe.edu.upeu.appturismo202501.data.remote.GenericResponse
import pe.edu.upeu.appturismo202501.data.remote.ResponderSolicitudRequest
import pe.edu.upeu.appturismo202501.data.remote.RestEmprendimiento
import pe.edu.upeu.appturismo202501.data.remote.SolicitudEmprendimientoRequest
import pe.edu.upeu.appturismo202501.modelo.EmprendimientoResponse
import pe.edu.upeu.appturismo202501.modelo.EstadoSolicitudResponse
import pe.edu.upeu.appturismo202501.modelo.SolicitudEmprendimiento
import retrofit2.Response
import javax.inject.Inject

interface EmprendimientoRepository {
    suspend fun listarEmprendimientos(): Response<EmprendimientoResponse>
    suspend fun obtenerEmprendimiento(id: Long): Response<EmprendimientoResponse>
    suspend fun crearEmprendimiento(token: String, request: EmprendimientoRequest): Response<EmprendimientoResponse>
    suspend fun activarEmprendimiento(token: String, id: Long): Response<EmprendimientoResponse>
    suspend fun enviarSolicitud(token: String, request: SolicitudEmprendimientoRequest): Response<GenericResponse>
    suspend fun listarSolicitudesPendientes(token: String, emprendimientoId: Long): Response<List<SolicitudEmprendimiento>>
    suspend fun responderSolicitud(token: String, solicitudId: Long, request: ResponderSolicitudRequest): Response<GenericResponse>
    suspend fun getEstadoSolicitud(token: String): Response<EstadoSolicitudResponse>
    suspend fun solicitudesUsuario(token: String): Response<List<SolicitudEmprendimiento>>
}

class EmprendimientoRepositoryImpl @Inject constructor(
    private val restEmprendimiento: RestEmprendimiento
) : EmprendimientoRepository {

    override suspend fun listarEmprendimientos(): Response<EmprendimientoResponse> {
        return restEmprendimiento.listarEmprendimientos()
    }

    override suspend fun obtenerEmprendimiento(id: Long): Response<EmprendimientoResponse> {
        return restEmprendimiento.obtenerEmprendimiento(id)
    }

    override suspend fun crearEmprendimiento(token: String, request: EmprendimientoRequest): Response<EmprendimientoResponse> {
        return restEmprendimiento.crearEmprendimiento(token, request)
    }

    override suspend fun activarEmprendimiento(token: String, id: Long): Response<EmprendimientoResponse> {
        return restEmprendimiento.activarEmprendimiento(token, id)
    }

    override suspend fun enviarSolicitud(token: String, request: SolicitudEmprendimientoRequest): Response<GenericResponse> {
        return restEmprendimiento.enviarSolicitud(token, request)
    }

    override suspend fun listarSolicitudesPendientes(token: String, emprendimientoId: Long): Response<List<SolicitudEmprendimiento>> {
        return restEmprendimiento.listarSolicitudesPendientes(token, emprendimientoId)
    }

    override suspend fun responderSolicitud(token: String, solicitudId: Long, request: ResponderSolicitudRequest): Response<GenericResponse> {
        return restEmprendimiento.responderSolicitud(token, solicitudId, request)
    }

    override suspend fun solicitudesUsuario(token: String): Response<List<SolicitudEmprendimiento>> {
        return restEmprendimiento.solicitudesUsuario(token)
    }

    override suspend fun getEstadoSolicitud(token: String): Response<EstadoSolicitudResponse> {
        return restEmprendimiento.estadoSolicitudEmprendedor(token)
    }
}
