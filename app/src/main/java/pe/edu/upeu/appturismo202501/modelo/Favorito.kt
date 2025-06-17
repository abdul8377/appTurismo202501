package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

// DTO que representa la estructura completa del Favorito desde el servidor
data class FavoritoDto(
    @SerializedName("favoritos_id")
    val favoritosId: Long,

    @SerializedName("users_id")
    val usersId: Long,

    @SerializedName("productos_id")
    val productosId: Long?,

    @SerializedName("servicios_id")
    val serviciosId: Long?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

// Clase para respuestas del servidor, idéntica a FavoritoDto para simplificar
data class FavoritoResp(
    @SerializedName("favoritos_id")
    val favoritosId: Long,

    @SerializedName("users_id")
    val usersId: Long,

    @SerializedName("productos_id")
    val productosId: Long?,

    @SerializedName("servicios_id")
    val serviciosId: Long?,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)

// Conversión desde FavoritoResp hacia DTO (si necesitas esta conversión)
fun FavoritoResp.toDto(): FavoritoDto {
    return FavoritoDto(
        favoritosId = favoritosId,
        usersId      = usersId,
        productosId  = productosId,
        serviciosId  = serviciosId,
        createdAt    = createdAt,
        updatedAt    = updatedAt
    )
}

// Clase para request al enviar nuevo favorito
data class FavoritoRequest(
    @SerializedName("productos_id")
    val productosId: Long? = null,

    @SerializedName("servicios_id")
    val serviciosId: Long? = null
)
