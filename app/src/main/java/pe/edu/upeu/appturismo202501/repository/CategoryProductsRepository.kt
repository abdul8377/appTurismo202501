package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestCategory
import pe.edu.upeu.appturismo202501.data.remote.RestCategoryProducts
import pe.edu.upeu.appturismo202501.modelo.CategoryProductcsResp
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import retrofit2.Response
import javax.inject.Inject

interface CategoryProductsRespository {
    suspend fun categoryProductsServices(): Response<List<CategoryProductcsResp>>
}

class CategoryProductsRepositoryImp @Inject constructor(private val restCategoryProducts: RestCategoryProducts) :
    CategoryProductsRespository {
    override suspend fun categoryProductsServices(): Response<List<CategoryProductcsResp>> {
        return restCategoryProducts.categoryProducts()
    }
}