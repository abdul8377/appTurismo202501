package pe.edu.upeu.appturismo202501.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import pe.edu.upeu.appturismo202501.modelo.ProductResp
import pe.edu.upeu.appturismo202501.modelo.ProductoDetalleResponse
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaResp
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface RestProductos {
    @GET("productos")
    suspend fun getProductos(): Response<List<ProductResp>>

    @GET("productos/{id}")
    suspend fun getProductoDetalle(@Path("id") productoId: Long): Response<ProductoDetalleResponse>


    @GET("api/productos/{id}")
    suspend fun getProduct(@Path("id") id: Long): ProductResp

    /*@Multipart
    @POST("api/productos")
    suspend fun createProduct(
        @Part("emprendimientos_id") emprendimientosId: RequestBody,
        @Part("categorias_productos_id") categoriaId: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody?,
        @Part("precio") precio: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("estado") estado: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): Response<ProductResp>
*/
    /*@Multipart
    @PUT("api/productos/{id}")
    suspend fun updateProduct(
        @Path("id") id: Long,
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part imagen: MultipartBody.Part?
    ): Response<ProductResp>
*/
    /*@DELETE("api/productos/{id}")
    suspend fun deleteProduct(@Path("id") id: Long): Response<DeleteResponse>
    */
}
