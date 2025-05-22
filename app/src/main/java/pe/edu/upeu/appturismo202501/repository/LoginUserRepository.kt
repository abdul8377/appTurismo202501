package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestLoginUsuario
import pe.edu.upeu.appturismo202501.modelo.CheckEmailDto
import pe.edu.upeu.appturismo202501.modelo.CheckEmailResp
import pe.edu.upeu.appturismo202501.modelo.LoginDto
import pe.edu.upeu.appturismo202501.modelo.LoginResp
import pe.edu.upeu.appturismo202501.modelo.ResetPasswordRequest
import pe.edu.upeu.appturismo202501.modelo.ApiResponse
import retrofit2.Response
import javax.inject.Inject

interface LoginUserRepository {
    suspend fun loginUsuario(userLogin: LoginDto): Response<LoginResp>
    suspend fun checkEmail(checkEmailDto: CheckEmailDto): Response<CheckEmailResp>
    suspend fun sendResetPasswordEmail(request: ResetPasswordRequest): Response<ApiResponse>  // Nuevo método
}

class LoginUserRespositoryImp @Inject constructor(
    private val restLoginUsuario: RestLoginUsuario
) : LoginUserRepository {

    override suspend fun loginUsuario(userLogin: LoginDto): Response<LoginResp> {
        return restLoginUsuario.login(userLogin)
    }

    override suspend fun checkEmail(checkEmailDto: CheckEmailDto): Response<CheckEmailResp> {
        return restLoginUsuario.checkEmail(checkEmailDto)
    }

    override suspend fun sendResetPasswordEmail(request: ResetPasswordRequest): Response<ApiResponse> {
        return restLoginUsuario.sendResetPasswordEmail(request)
    }
}
