package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


data class ZonaTuristicaDto(
    @SerializedName("zonas_turisticas_id")       val zonaId: Long,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    @SerializedName("estado")             val estado: String?,
    @SerializedName("images")             val images: List<ImagenDto>? = null,
    @SerializedName("imagen_url")         val imagenUrl: String? = null,
)


data class ZonaTuristicaResp(
    @SerializedName("zonas_turisticas_id")       val zonaId: Long,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    @SerializedName("estado")             val estado: String?,
    @SerializedName("images")             val images: List<ImagenDto>? = null,
    @SerializedName("imagen_url")         val imagenUrl: String? = null,
)

data class CreateZonaRequest(
    @SerializedName("nombre")               val nombre: String,
    @SerializedName("descripcion")          val descripcion: String?,
    @SerializedName("ubicacion")            val ubicacion: Double,
    @SerializedName("estado")               val estado: String?,
    )

data class UpdateZonaRequest(
    @SerializedName("zonas_turisticas_id")  val zonaId: Long,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    @SerializedName("estado")             val estado: String?,
)

data class UpdateZonaParts(
    val method: RequestBody,
    val nombre: RequestBody,
    val descripcion: RequestBody?,
    val ubicacion: RequestBody?,
    val estado: RequestBody?
)


fun UpdateZonaRequest.toParts(): UpdateZonaParts = UpdateZonaParts(
    method = "PUT".toReqBody(),
    nombre = nombre.toReqBody(),
    descripcion = descripcion?.toReqBody(),
    ubicacion = ubicacion?.toReqBody(),
    estado = estado?.toReqBody()
)
