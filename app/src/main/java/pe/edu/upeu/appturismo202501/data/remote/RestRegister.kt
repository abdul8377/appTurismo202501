package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.RegisterDto
import pe.edu.upeu.appturismo202501.modelo.RegisterResp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RestRegister {
    @POST("auth/register")
    suspend fun register(@Body registerDto: RegisterDto): Response<RegisterResp>
}
