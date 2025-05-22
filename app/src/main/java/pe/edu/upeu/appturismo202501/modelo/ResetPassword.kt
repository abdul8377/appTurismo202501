package pe.edu.upeu.appturismo202501.modelo

data class ResetPasswordRequest(
    val email: String
)

data class ApiResponse(
    val message: String
)