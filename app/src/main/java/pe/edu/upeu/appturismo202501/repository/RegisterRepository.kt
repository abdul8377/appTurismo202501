package pe.edu.upeu.appturismo202501.repository


import pe.edu.upeu.appturismo202501.data.remote.RestRegister
import pe.edu.upeu.appturismo202501.modelo.RegisterDto
import pe.edu.upeu.appturismo202501.modelo.RegisterResp
import retrofit2.Response
import javax.inject.Inject

interface RegisterRepository{
    suspend fun register(registerUsuario:RegisterDto):
            Response<RegisterResp>
}

class RegisterRepositoryImpl @Inject constructor(private val restRegister: RestRegister):RegisterRepository{
    override suspend fun register(registerUsuario: RegisterDto):
            Response<RegisterResp>{
        return restRegister.register(registerUsuario)
    }
}