package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

data class CarritoDto(
    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("productos_id")
    val productosId: Long? = null,

    @SerializedName("servicios_id")
    val serviciosId: Long? = null,

    @SerializedName("cantidad")
    val cantidad: Int = 1,

    @SerializedName("precio_unitario")
    val precioUnitario: Double,

    @SerializedName("subtotal")
    val subtotal: Double,

    @SerializedName("total_carrito")
    val totalCarrito: Double? = null,

    @SerializedName("estado")
    val estado: String = "en proceso"
)

data class CarritoResp(
    @SerializedName("carrito_id")
    val carritoId: Long,

    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("productos_id")
    val productosId: Long?,

    @SerializedName("servicios_id")
    val serviciosId: Long?,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio_unitario")
    val precioUnitario: Double,

    @SerializedName("subtotal")
    val subtotal: Double,

    @SerializedName("total_carrito")
    val totalCarrito: Double?,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?
)