package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

data class CategoryProductcsDto(
    val id: Long,
    val nombre: String,
    val descripcion: String?
)

data class CategoryProductcsResp(
    @SerializedName("categorias_productos_id")
    val id: Long,
    val nombre: String,
    val descripcion: String?
)