package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

// DTO que representa la estructura completa del Mensaje desde el servidor
data class MensajeDto(
    @SerializedName("mensajes_id")
    val mensajesId: Long,

    @SerializedName("conversaciones_id")
    val conversacionesId: Long,

    @SerializedName("emisor")
    val emisor: String, // 'usuario' o 'emprendimiento'

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("imagen_url")
    val imagenUrl: String?,

    @SerializedName("leido")
    val leido: Boolean,

    @SerializedName("enviado_en")
    val enviadoEn: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

// Clase para respuestas del servidor, idéntica a MensajeDto para simplificar
data class MensajeResp(
    @SerializedName("mensajes_id")
    val mensajesId: Long,

    @SerializedName("conversaciones_id")
    val conversacionesId: Long,

    @SerializedName("emisor")
    val emisor: String, // 'usuario' o 'emprendimiento'

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("imagen_url")
    val imagenUrl: String?,

    @SerializedName("leido")
    val leido: Boolean,

    @SerializedName("enviado_en")
    val enviadoEn: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

data class MensajeRequest(
    @SerializedName("contenido")
    val contenido: String, // El texto del mensaje

    @SerializedName("imagen_url")
    val imagenUrl: String? = null // URL de la imagen (opcional)
)

// Conversión desde MensajeResp hacia DTO (si necesitas esta conversión)
fun MensajeResp.toDto(): MensajeDto {
    return MensajeDto(
        mensajesId = mensajesId,
        conversacionesId = conversacionesId,
        emisor = emisor,
        contenido = contenido,
        imagenUrl = imagenUrl,
        leido = leido,
        enviadoEn = enviadoEn,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
