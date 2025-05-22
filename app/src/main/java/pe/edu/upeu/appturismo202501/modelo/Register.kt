package pe.edu.upeu.appturismo202501.modelo

data class RegisterDto(
    val name: String,
    val last_name: String,
    val email: String,
    val password: String,
    val password_confirmation: String,
    val country: String?,
    val zip_code: String?
)

data class RegisterResp(
    val message: String,
    val user: User
)

// Asumo que tienes esta clase User, si no, se puede definir así:

data class User(
    val id: Int,
    val name: String,
    val last_name: String,
    val email: String,
    val country: String?,
    val zip_code: String?,
    val created_at: String?,
    val updated_at: String?
)