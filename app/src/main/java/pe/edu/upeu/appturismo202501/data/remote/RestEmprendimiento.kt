package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.Emprendimiento
import pe.edu.upeu.appturismo202501.modelo.EmprendimientoResponse
import pe.edu.upeu.appturismo202501.modelo.EstadoSolicitudResponse
import pe.edu.upeu.appturismo202501.modelo.SolicitudEmprendimiento
import retrofit2.Response
import retrofit2.http.*

interface RestEmprendimiento {

    // --- Rutas públicas ---

    // Listar todos los emprendimientos
    @GET("emprendimientos")
    suspend fun listarEmprendimientos(): Response<EmprendimientoResponse>

    // Obtener detalle de un emprendimiento (si existe)
    @GET("emprendimientos/{id}")
    suspend fun obtenerEmprendimiento(
        @Path("id") id: Long
    ): Response<EmprendimientoResponse>

    // --- Rutas protegidas (requieren token) ---

    // Crear nuevo emprendimiento (estado pendiente)
    @POST("emprendimientos")
    suspend fun crearEmprendimiento(
        @Header("Authorization") token: String,
        @Body nuevoEmprendimiento: EmprendimientoRequest
    ): Response<EmprendimientoResponse>

    // Activar emprendimiento pendiente
    @POST("emprendimientos/{id}/activar")
    suspend fun activarEmprendimiento(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<EmprendimientoResponse>

    // Enviar solicitud para unirse a emprendimiento
    @POST("emprendimientos/solicitud")
    suspend fun enviarSolicitud(
        @Header("Authorization") token: String,
        @Body solicitudRequest: SolicitudEmprendimientoRequest
    ): Response<GenericResponse>

    // Listar solicitudes pendientes para un emprendimiento
    @GET("emprendimientos/{id}/solicitudes")
    suspend fun listarSolicitudesPendientes(
        @Header("Authorization") token: String,
        @Path("id") emprendimientoId: Long
    ): Response<List<SolicitudEmprendimiento>>

    // Aprobar o rechazar solicitud
    @POST("solicitudes/{id}/responder")
    suspend fun responderSolicitud(
        @Header("Authorization") token: String,
        @Path("id") solicitudId: Long,
        @Body respuesta: ResponderSolicitudRequest
    ): Response<GenericResponse>

    // Listar solicitudes del usuario autenticado
    @GET("solicitudes")
    suspend fun solicitudesUsuario(
        @Header("Authorization") token: String
    ): Response<List<SolicitudEmprendimiento>>

    // Consultar estado de solicitud/emprendimiento del usuario autenticado
    @GET("emprendimientos/estado-solicitud")
    suspend fun estadoSolicitudEmprendedor(
        @Header("Authorization") token: String
    ): Response<EstadoSolicitudResponse>

}


// Request para crear emprendimiento (puedes ajustar campos según backend)
data class EmprendimientoRequest(
    val nombre: String,
    val descripcion: String?,
    val tipo_negocio_id: Long?,
    val direccion: String?,
    val telefono: String?
)

// Request para enviar solicitud para unirse a emprendimiento
data class SolicitudEmprendimientoRequest(
    val codigo_unico: String,
    val rol_solicitado: String // "colaborador" o "propietario"
)

// Request para responder a solicitud (aprobar o rechazar)
data class ResponderSolicitudRequest(
    val accion: String // "aprobar" o "rechazar"
)

// Respuesta genérica para mensajes simples
data class GenericResponse(
    val message: String
)
