package pe.edu.upeu.appturismo202501.data.remote

import pe.edu.upeu.appturismo202501.modelo.CategoryProductcsResp
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import retrofit2.Response
import retrofit2.http.GET

interface RestCategoryProducts {

    @GET("categorias-productos")
    suspend fun categoryProducts(): Response<List<CategoryProductcsResp>>

}