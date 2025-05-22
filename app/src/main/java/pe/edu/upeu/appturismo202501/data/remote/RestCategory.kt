package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.CategoryDto
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.modelo.LoginDto
import pe.edu.upeu.appturismo202501.modelo.LoginResp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RestCategory {
    @GET("categorias-servicios")
    suspend fun category(): Response<List<CategoryResp>>
}