package pe.edu.upeu.appturismo202501.modelo

import com.squareup.moshi.Json

data class LoginDto(
    val email: String,
    val password: String
)

data class LoginResp(
    val token: String,
    val name: String,
    val email: String,
    val roles: List<String>,
    val is_active: Int,
    val motivo_inactivo: String?
)
