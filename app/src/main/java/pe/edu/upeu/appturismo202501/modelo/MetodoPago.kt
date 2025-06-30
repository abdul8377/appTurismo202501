package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

data class MetodoPagoDto(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("activo")
    val activo: Boolean = true
)

data class MetodoPagoResp(
    @SerializedName("metodo_pago_id")
    val metodoPagoId: Long,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("activo")
    val activo: Boolean,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?
)