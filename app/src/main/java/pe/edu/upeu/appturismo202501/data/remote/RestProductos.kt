package pe.edu.upeu.appturismo202501.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.edu.upeu.appturismo202501.modelo.CrearProductoResponse
import pe.edu.upeu.appturismo202501.modelo.ProductDto
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaResp
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface RestProductos {
    @GET("productos")
    suspend fun getProductos(): Response<List<ProductResp>>

    @GET("emprendedor/productos")
    suspend fun listarMios(): Response<List<ProductResp>>

    @GET("emprendedor/productos/{id}")
    suspend fun getById(
        @Path("id") id: Long
    ): Response<ProductResp>

    @Multipart
    @POST("emprendedor/productos")
    suspend fun crear(
        @Part("categorias_productos_id")   categoriasProductosId: RequestBody,
        @Part("nombre")                    nombre: RequestBody,
        @Part("descripcion")               descripcion: RequestBody,
        @Part("precio")                    precio: RequestBody,
        @Part("stock")                     stock: RequestBody,
        @Part("estado")                    estado: RequestBody,
        @Part              imagenes: List<MultipartBody.Part>
    ): Response<CrearProductoResponse>

    @Multipart
    @POST("emprendedor/productos/{id}")
    suspend fun actualizar(
        @Path("id") id: Long,
        @Part("_method") method: RequestBody,
        @Part("categorias_productos_id") categoriasProductosId: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("precio") precio: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("estado") estado: RequestBody,
        @Part            imagenes: List<MultipartBody.Part>
    ): Response<CrearProductoResponse>

    @DELETE("emprendedor/productos/{id}")
    suspend fun eliminar(@Path("id") id: Long): Response<Void>
}