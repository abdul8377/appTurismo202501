package pe.edu.upeu.appturismo202501.modelo

data class TipoDeNegocio(
    val id: Long,               // ID del tipo de negocio
    val nombre: String,         // Nombre del tipo de negocio
    val descripcion: String,    // Descripción del tipo de negocio (no nulo si es obligatorio)
    val created_at: String,     // Fecha de creación (no nulo)
    val updated_at: String,    // Fecha de última actualización (no nulo)
    val emprendimientos_count: Int // Cantidad de emprendimientos vinculados
)

data class Emprendimiento(
    val emprendimiento_id: Long,  // ID del emprendimiento
    val nombre: String,           // Nombre del emprendimiento
    val descripcion: String?,     // Descripción del emprendimiento (opcional)
    val tipo_negocio_id: Long,    // ID del tipo de negocio al que pertenece
    val direccion: String?,       // Dirección del emprendimiento
    val telefono: String?,        // Teléfono del emprendimiento
    val estado: String,           // Estado del emprendimiento (activo, inactivo, pendiente)
    val fecha_registro: String?   // Fecha de registro
)

data class TipoDeNegocioResponse(
    val success: Boolean,         // Indica si la solicitud fue exitosa
    val message: String,          // Mensaje de la respuesta
    val data: List<TipoDeNegocio>? = null, // Lista de tipos de negocio (para respuestas con múltiples datos)
    val tipoDeNegocio: TipoDeNegocio? = null // Un solo tipo de negocio (para respuestas con un solo dato)
)

data class EmprendimientoResponse(
    val success: Boolean,         // Indica si la solicitud fue exitosa
    val message: String,          // Mensaje de la respuesta
    val data: List<Emprendimiento>? = null, // Lista de emprendimientos (para respuestas con múltiples datos)
    val emprendimiento: Emprendimiento? = null // Un solo emprendimiento (para respuestas con un solo dato)
)
