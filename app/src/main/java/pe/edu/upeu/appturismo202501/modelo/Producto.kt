package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("emprendimientos_id")
    val emprendimientosId: Long,

    @SerializedName("categorias_productos_id")
    val categoriasProductosId: Long,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("stock")
    val stock: Int,

    /** opcional, por defecto “activo” */
    @SerializedName("estado")
    val estado: String = "activo"
)

data class ProductResp(
    @SerializedName("productos_id")
    val id: Long,

    @SerializedName("emprendimientos_id")
    val emprendimientoId: Long,

    @SerializedName("categorias_productos_id")
    val categoriaProductoId: Long,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("precio")
    val precio: Double,

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("estado")
    val estado: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?,

    /** URL principal del producto */
    @SerializedName("imagen_url")
    val imagenUrl: String?,

    /** Lista de imágenes asociadas */
    @SerializedName("images")
    val images: List<ImageResp>
)

data class ImageResp(
    @SerializedName("id")
    val id: Long,

    @SerializedName("url")
    val url: String,

    @SerializedName("titulo")
    val titulo: String
)

/**
 * Representa la respuesta completa para el detalle específico de un producto desde la API.
 */

data class ProductoDetalleResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("producto")
    val producto: ProductResp
)
