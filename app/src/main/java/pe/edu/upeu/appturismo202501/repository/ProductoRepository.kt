package pe.edu.upeu.appturismo202501.repository

import javax.inject.Inject
import javax.inject.Singleton
import pe.edu.upeu.appturismo202501.data.remote.RestProductos
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import retrofit2.Response

/**
 * Define las operaciones disponibles para productos.
 */
interface ProductoRespository {
    /** Obtiene la lista de productos desde la API */
    suspend fun productoServices(): Response<List<ProductResp>>
}

/**
 * Implementación concreta de [ProductoRespository] que usa Retrofit.
 */
@Singleton
class ProductoRepositoryImp @Inject constructor(
    private val restProductos: RestProductos
) : ProductoRespository {
    override suspend fun productoServices(): Response<List<ProductResp>> =
        restProductos.getProductos()
}
