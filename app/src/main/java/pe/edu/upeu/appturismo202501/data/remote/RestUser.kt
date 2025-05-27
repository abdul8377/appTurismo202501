package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.UsersDto
import pe.edu.upeu.appturismo202501.modelo.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface RestUser {

    // Listar usuarios (con filtros opcionales por query params)
    @GET("users")
    suspend fun getUsers(
        @Query("role") role: String? = null,
        @Query("is_active") isActive: Boolean? = null
    ): Response<List<UsersDto>>

    // Obtener usuario individual por ID
    @GET("users/{id}")
    suspend fun getUserById(
        @Path("id") userId: Long
    ): Response<UsersDto>

    @PATCH("users/{id}/active")
    suspend fun toggleActive(
        @Path("id") userId: Long,
        @Body body: ToggleActiveRequest
    ): Response<ApiResponse>

    @PATCH("users/{id}/password")
    suspend fun changePassword(
        @Path("id") userId: Long,
        @Body body: ChangePasswordRequest
    ): Response<ApiResponse>
}

// Clases para request bodies:

data class ToggleActiveRequest(
    val is_active: Boolean,
    val motivo_inactivo: String? = null
)

data class ChangePasswordRequest(
    val password: String,
    val password_confirmation: String
)
