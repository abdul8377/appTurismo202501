package pe.edu.upeu.appturismo202501.modelo

import com.squareup.moshi.Json

data class UsersDto(
    val id: Long,
    val name: String,
    val lastName: String,
    val email: String,
    val roleId: Long,
    val roles: List<Role>,
    @Json(name = "is_active")
    val is_active: Boolean, // <- Cambia a Boolean
    val motivo_inactivo: String?,
    val created_at: String,
    val updated_at: String,
)


data class UserResp(
    val id: Long,
    val name: String,
    val lastName: String,
    val email: String,
    val roles: List<Role>,
    @Json(name = "is_active")
    val isActive: Boolean, // <- Cambia a Boolean
    val motivo_inactivo: String?,
    val created_at: String,
    val updated_at: String,
)

fun UserResp.toDto(): UsersDto {
    return UsersDto(
        id = id,
        name = name,
        lastName = lastName,
        email = email,
        roleId = roles.firstOrNull()?.id ?: 0L,
        roles = roles,
        is_active = isActive,
        motivo_inactivo = motivo_inactivo,
        created_at = created_at,
        updated_at = updated_at
    )
}




data class ToggleActiveRequest(
    val is_active: Boolean,
    val motivo_inactivo: String? = null
)

data class ChangePasswordRequest(
    val password: String,
    val password_confirmation: String
)

