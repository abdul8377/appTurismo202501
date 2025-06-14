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

    /** Lista de imágenes asociadas */
    @SerializedName("imagen_url")
    val imagenUrl: String?,          // URL completa: "http://localhost:8000/storage/…"

    @SerializedName("images")
    val images: List<ImageResp>
)

data class ImageResp(
    @SerializedName("id")
    val id: Long,

    @SerializedName("url")
    val url: String,                 // En tu JSON es ya la URL completa

    @SerializedName("titulo")
    val titulo: String
)