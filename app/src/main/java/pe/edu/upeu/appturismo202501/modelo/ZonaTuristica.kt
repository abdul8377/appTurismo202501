package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName


data class ZonaTuristicaDto(
    val nombre: String,
    val descripcion: String? = null,
    val ubicacion: String?  = null,
    val estado: String      = "activo"
)


data class ZonaTuristicaResp(
    @SerializedName("zonas_turisticas_id")
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val ubicacion: String?,
    @SerializedName("imagen_url")
    val imagenUrl: String?,
    val estado: String,
)