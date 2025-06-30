package pe.edu.upeu.appturismo202501.modelo
import com.google.gson.annotations.SerializedName

// DTO que representa la estructura completa de la Conversación desde el servidor
data class ConversacionDto(
    @SerializedName("conversaciones_id")
    val conversacionesId: Long,

    @SerializedName("users_id")
    val usersId: Long,

    @SerializedName("destinatario_users_id")
    val destinatarioUsersId: Long?,

    @SerializedName("emprendimientos_id")
    val emprendimientosId: Long?,

    @SerializedName("tipo")
    val tipo: String, // 'usuario_emprendimiento', 'usuario_usuario', 'usuario_moderador'

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

// Clase para respuestas del servidor, idéntica a ConversacionDto para simplificar
data class ConversacionResp(
    @SerializedName("conversaciones_id")
    val conversacionesId: Long,

    @SerializedName("users_id")
    val usersId: Long,

    @SerializedName("destinatario_users_id")
    val destinatarioUsersId: Long?,

    @SerializedName("emprendimientos_id")
    val emprendimientosId: Long?,

    @SerializedName("tipo")
    val tipo: String, // 'usuario_emprendimiento', 'usuario_usuario', 'usuario_moderador'

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

data class ConversacionRequest(
    @SerializedName("tipo")
    val tipo: String, // 'usuario_emprendimiento', 'usuario_usuario', 'usuario_moderador'

    @SerializedName("destino_id")
    val destinoId: Long
)

// Conversión desde ConversacionResp hacia DTO (si necesitas esta conversión)
fun ConversacionResp.toDto(): ConversacionDto {
    return ConversacionDto(
        conversacionesId = conversacionesId,
        usersId          = usersId,
        destinatarioUsersId = destinatarioUsersId,
        emprendimientosId = emprendimientosId,
        tipo             = tipo,
        createdAt        = createdAt,
        updatedAt        = updatedAt
    )
}
