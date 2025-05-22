package pe.edu.upeu.appturismo202501.modelo

data class CheckEmailDto(
    val email: String
)

data class CheckEmailResp(
    val exists: Boolean,
    val name: String?,            // nullable, porque si no existe no hay nombre
    val roles: List<String>?      // lista de roles, nullable o vacía si no existe
)