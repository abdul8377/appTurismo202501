package pe.edu.upeu.appturismo202501.repository


import pe.edu.upeu.appturismo202501.data.remote.RestProductos
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import retrofit2.Response

import javax.inject.Inject

interface ProductoRespository {
    suspend fun productoServices(): Response<List<ProductResp>>
}

class ProductoRepositoryImp @Inject constructor(private val restProductos: RestProductos) :
    ProductoRespository {
    override suspend fun productoServices(): Response<List<ProductResp>> {
        return restProductos.getProductos()
    }
}