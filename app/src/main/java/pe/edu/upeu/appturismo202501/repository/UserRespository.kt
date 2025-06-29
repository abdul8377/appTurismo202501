package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestUser
import pe.edu.upeu.appturismo202501.modelo.ApiResponse
import pe.edu.upeu.appturismo202501.modelo.ChangePasswordRequest
import pe.edu.upeu.appturismo202501.modelo.ToggleActiveRequest
import pe.edu.upeu.appturismo202501.modelo.UsersDto
import retrofit2.Response
import javax.inject.Inject

interface UserRepository {
    suspend fun getUsers(role: String? = null, isActive: Boolean? = null): Response<List<UsersDto>>
    suspend fun getUserById(userId: Long): Response<UsersDto>
    suspend fun toggleActive(userId: Long, request: ToggleActiveRequest): Response<ApiResponse>
    suspend fun changePassword(userId: Long, request: ChangePasswordRequest): Response<ApiResponse>
}

class UserRepositoryImpl @Inject constructor(
    private val restUser: RestUser
) : UserRepository {

    override suspend fun getUsers(role: String?, isActive: Boolean?): Response<List<UsersDto>> {
        return restUser.getUsers(role, isActive)
    }

    override suspend fun getUserById(userId: Long): Response<UsersDto> {
        return restUser.getUserById(userId)
    }

    override suspend fun toggleActive(userId: Long, request: ToggleActiveRequest): Response<ApiResponse> {
        return restUser.toggleActive(userId, request)
    }

    override suspend fun changePassword(userId: Long, request: ChangePasswordRequest): Response<ApiResponse> {
        return restUser.changePassword(userId, request)
    }
}

