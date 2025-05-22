package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

// ZonaTuristicaResp.kt
data class ZonaTuristicaResp(
    @SerializedName("zonas_turisticas_id")
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val ubicacion: String?,
    val estado: String?,
    @SerializedName("images")
    val images: List<ImageResp>
)

// ImageResp.kt
data class ImageResp(
    val id: Long,
    @SerializedName("url")
    val url: String,
    @SerializedName("titulo")
    val titulo: String
) {
    /** Construye la URL completa a partir de tu API base, apuntando a /storage/... */
    fun fullUrl(baseApiUrl: String): String =
        "$baseApiUrl/storage/$url"
}