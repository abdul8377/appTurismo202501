package pe.edu.upeu.appturismo202501.repository

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pe.edu.upeu.appturismo202501.data.remote.RestCategoryProducts
import javax.inject.Inject
import javax.inject.Singleton
import pe.edu.upeu.appturismo202501.data.remote.RestProductos
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.modelo.CrearProductoResponse
import pe.edu.upeu.appturismo202501.modelo.ProductDto
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.modelo.ProductoDetalleResponse
import retrofit2.Response

/**
 * Define las operaciones disponibles para productos.
 */
interface ProductoRespository {
    /** Obtiene la lista de productos desde la API */
    suspend fun productoServices(): Response<List<ProductResp>>

    /** Obtiene los detalles específicos de un producto por ID desde la API */
    suspend fun productoDetalle(productoId: Long): Response<ProductoDetalleResponse>
    suspend fun productoServicesMios(): Response<List<ProductResp>>
    suspend fun getProductoById(id: Long): Response<ProductResp>
    suspend fun crearProducto(
        categoriasProductosId: RequestBody,
        nombre: RequestBody,
        descripcion: RequestBody,
        precio: RequestBody,
        stock: RequestBody,
        estado: RequestBody,
        imagenes: List<MultipartBody.Part>
    ): Response<CrearProductoResponse>

    suspend fun actualizarProducto(
        id: Long,
        categoriasProductosId: RequestBody,
        nombre: RequestBody,
        descripcion: RequestBody,
        precio: RequestBody,
        stock: RequestBody,
        estado: RequestBody,
        imagenes: List<MultipartBody.Part>
    ): Response<CrearProductoResponse>

    suspend fun eliminarProducto(id: Long): Response<Void>
}

/**
 * Implementación concreta de [ProductoRespository] que usa Retrofit.
 */
@Singleton
class ProductoRepositoryImp @Inject constructor(
    private val restProductos: RestProductos,
    private val rest: RestProductos,
    private val restCat: RestCategoryProducts,
) : ProductoRespository {


    override suspend fun productoServices(): Response<List<ProductResp>> =
        restProductos.getProductos()

    override suspend fun productoDetalle(productoId: Long): Response<ProductoDetalleResponse> =
        restProductos.getProductoDetalle(productoId)

    override suspend fun productoServicesMios(): Response<List<ProductResp>> =
        rest.listarMios()

    override suspend fun getProductoById(id: Long): Response<ProductResp> =
        rest.getById(id)

    override suspend fun crearProducto(
        categoriasProductosId: RequestBody,
        nombre: RequestBody,
        descripcion: RequestBody,
        precio: RequestBody,
        stock: RequestBody,
        estado: RequestBody,
        imagenes: List<MultipartBody.Part>
    ): Response<CrearProductoResponse> =
        rest.crear(
            categoriasProductosId    = categoriasProductosId,
            nombre                   = nombre,
            descripcion              = descripcion,
            precio                   = precio,
            stock                    = stock,
            estado                   = estado,
            imagenes                 = imagenes
        )

    override suspend fun actualizarProducto(
        id: Long,
        categoriasProductosId: RequestBody,
        nombre: RequestBody,
        descripcion: RequestBody,
        precio: RequestBody,
        stock: RequestBody,
        estado: RequestBody,
        imagenes: List<MultipartBody.Part>
    ): Response<CrearProductoResponse> =
        rest.actualizar(
            id                  = id,
            "PUT".toRequestBody("text/plain".toMediaType()),
            categoriasProductosId = categoriasProductosId,
            nombre              = nombre,
            descripcion         = descripcion,
            precio              = precio,
            stock               = stock,
            estado              = estado,
            imagenes            = imagenes
        )

    override suspend fun eliminarProducto(id: Long): Response<Void> =
        rest.eliminar(id)
}
