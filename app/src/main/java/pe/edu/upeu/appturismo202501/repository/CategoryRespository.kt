package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestCategory
import pe.edu.upeu.appturismo202501.data.remote.RestLoginUsuario
import pe.edu.upeu.appturismo202501.modelo.CategoryDto
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.modelo.LoginDto
import pe.edu.upeu.appturismo202501.modelo.LoginResp
import retrofit2.Response
import javax.inject.Inject

interface CategoryRespository {
    suspend fun categoryServices(): Response<List<CategoryResp>>
}

class CategoryRepositoryImp @Inject constructor(private val restCategory: RestCategory) :
    CategoryRespository {
    override suspend fun categoryServices(): Response<List<CategoryResp>> {
        return restCategory.category()
    }
}