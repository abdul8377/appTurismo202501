package pe.edu.upeu.appturismo202501.modelo

import com.squareup.moshi.Json

data class Emprendimiento(
    @Json(name = "emprendimientos_id")
    val id: Long,
    @Json(name = "codigo_unico")
    val codigoUnico: String,
    val nombre: String,
    val descripcion: String?,
    @Json(name = "tipo_negocio_id")
    val tipoNegocioId: Long?,
    val direccion: String?,
    val telefono: String?,
    val estado: String,
    @Json(name = "fecha_registro")
    val fechaRegistro: String,
    val usuarios: List<UserEmprendimiento>? = emptyList(),
    @Json(name = "solicitudes")
    val solicitudes: List<SolicitudEmprendimiento>? = emptyList(),
    @Json(name = "tipo_negocio")
    val tipoNegocio: TipoNegocio? = null
)

data class UserEmprendimiento(
    val id: Long,
    val name: String,
    val lastName: String?,
    val email: String,
    val roles: List<Role>? = emptyList(),
    @Json(name = "rol_emprendimiento")
    val rolEmprendimiento: String?,
    @Json(name = "fecha_asignacion")
    val fechaAsignacion: String?
)

data class SolicitudEmprendimiento(
    val id: Long,
    @Json(name = "emprendimientos_id")
    val emprendimientoId: Long,
    @Json(name = "users_id")
    val userId: Long,
    val estado: String,
    @Json(name = "rol_solicitado")
    val rolSolicitado: String,
    @Json(name = "fecha_solicitud")
    val fechaSolicitud: String,
    @Json(name = "fecha_respuesta")
    val fechaRespuesta: String? = null,
    val usuario: UserEmprendimiento? = null,
    val emprendimiento: Emprendimiento? = null
)

data class TipoNegocio(
    val id: Long,
    val nombre: String,
    val descripcion: String?
)

data class EmprendimientoResponse(
    val success: Boolean,
    val message: String,
    val data: Emprendimiento?,
    val emprendimiento: Emprendimiento? = null
)

// Nuevo modelo para respuesta del estado de solicitud y emprendimiento
data class EstadoSolicitudResponse(
    val tieneSolicitudPendiente: Boolean,
    val tieneEmprendimientoActivo: Boolean,
    val estadoSolicitud: String? = null
)
