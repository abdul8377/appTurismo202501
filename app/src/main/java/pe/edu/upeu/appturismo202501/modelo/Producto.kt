package pe.edu.upeu.appturismo202501.modelo

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.annotations.SerializedName

data class ProductDto(
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
data class CrearProductoResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("producto")
    val producto: ProductDto
)


data class CategoryUi(val id: Long, val name: String, val icon: ImageVector? = Icons.Outlined.Category)

data class ProductUi(
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val estado: Boolean,
    val categoryId: Long,
    val images: List<Uri>
)

data class ExistingImage(val id: Long, val url: String)
