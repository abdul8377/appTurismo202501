package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val imagen: String?,
    val icono: String?
)

data class CategoryResp(
    @SerializedName("categorias_servicios_id")
    val id: Long,

    val nombre: String,
    val descripcion: String?,


    @SerializedName("imagen_url")
    val imagenUrl: String?,

    @SerializedName("icono_url")
    val iconoUrl: String?
)