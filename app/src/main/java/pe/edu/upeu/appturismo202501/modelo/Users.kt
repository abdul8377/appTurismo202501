package pe.edu.upeu.appturismo202501.modelo

import com.squareup.moshi.Json

data class UsersDto(
    val id: Long,
    val name: String,
    val email: String,
    val roles: Long,
    val is_active: Boolean,
    val motivo_inactivo: String?,
    val created_at: String,
    val updated_at: String,
)



data class UserResp(
    val id: Long,
    val name: String,
    val email: String,
    val roles: Roles,
    @Json(name = "is_active")
    val isActive: Int,
    val motivo_inactivo: String?,
    val created_at: String,
    val updated_at: String,
)

fun UserResp.toDto(): UsersDto {
    return UsersDto(
        id = this.id,
        name = this.name,
        email = this.email,
        roles = this.roles.id,
        is_active = this.isActive == 1,  // Convierte Int a Boolean
        motivo_inactivo = this.motivo_inactivo,
        created_at = this.created_at,
        updated_at = this.updated_at
    )
}