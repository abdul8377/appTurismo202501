package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.ApiResponse
import pe.edu.upeu.appturismo202501.modelo.CheckEmailDto
import pe.edu.upeu.appturismo202501.modelo.CheckEmailResp
import pe.edu.upeu.appturismo202501.modelo.LoginDto
import pe.edu.upeu.appturismo202501.modelo.LoginResp
import pe.edu.upeu.appturismo202501.modelo.ResetPasswordRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RestLoginUsuario {
    @POST("auth/login")
    suspend fun login(@Body userLogin: LoginDto): Response<LoginResp>
    @POST("auth/check-email")
    suspend fun checkEmail(@Body checkEmailDto: CheckEmailDto): Response<CheckEmailResp>  // <-- Agrega este método

    @POST("password/email")
    suspend fun sendResetPasswordEmail(@Body request: ResetPasswordRequest): Response<ApiResponse>
}