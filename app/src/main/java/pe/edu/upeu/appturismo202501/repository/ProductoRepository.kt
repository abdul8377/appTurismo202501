package pe.edu.upeu.appturismo202501.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton
import pe.edu.upeu.appturismo202501.data.remote.RestProductos
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import retrofit2.Response


interface ProductoRespository {

    suspend fun productoServices(): Response<List<ProductResp>>

    suspend fun productoServicesMios(): Response<List<ProductResp>>

    suspend fun crearProducto(
        nombre: RequestBody,
        descripcion: RequestBody,
        precio: RequestBody,
        stock: RequestBody,
        imagen: MultipartBody.Part?
    ): Response<Void>

    suspend fun actualizarProducto(
        id: Long,
        nombre: RequestBody,
        descripcion: RequestBody,
        precio: RequestBody,
        stock: RequestBody,
        imagen: MultipartBody.Part?
    ): Response<Void>

    suspend fun eliminarProducto(id: Long): Response<Void>
}


@Singleton
class ProductoRepositoryImp @Inject constructor(
    private val rest: RestProductos
) : ProductoRespository {

    override suspend fun productoServices(): Response<List<ProductResp>> =
        rest.getProductos()

    override suspend fun productoServicesMios(): Response<List<ProductResp>> =
        rest.listarMios()

    override suspend fun crearProducto(
        nombre: RequestBody,
        descripcion: RequestBody,
        precio: RequestBody,
        stock: RequestBody,
        imagen: MultipartBody.Part?
    ): Response<Void> =
        rest.crear(nombre, descripcion, precio, stock, imagen)

    override suspend fun actualizarProducto(
        id: Long,
        nombre: RequestBody,
        descripcion: RequestBody,
        precio: RequestBody,
        stock: RequestBody,
        imagen: MultipartBody.Part?
    ): Response<Void> =
        rest.actualizar(id, nombre, descripcion, precio, stock, imagen)

    override suspend fun eliminarProducto(id: Long): Response<Void> =
        rest.eliminar(id)
}