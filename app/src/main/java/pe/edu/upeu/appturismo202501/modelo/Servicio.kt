package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.AlojamientoUi


data class ServicioDto(
    @SerializedName("servicios_id")       val serviciosId: Long,
    @SerializedName("emprendimientos_id") val emprendimientosId: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @SerializedName("capacidad_maxima")   val capacidadMaxima: Int,
    @SerializedName("duracion_servicio")  val duracionServicio: String?,
    @SerializedName("estado")             val estado: String?,
    @SerializedName("images")             val images: List<ImagenDto>? = null,
    @SerializedName("imagenes_url")       val imagenesUrl: List<String>? = null,
    @SerializedName("imagen_url")         val imagenUrl: String? = null,
    val emprendimiento: EmprendimientoDto? = null
)


data class ServicioResp(
    @SerializedName("servicios_id")       val serviciosId: Long,
    @SerializedName("emprendimientos_id") val emprendimientosId: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @SerializedName("capacidad_maxima")   val capacidadMaxima: Int,
    @SerializedName("duracion_servicio")  val duracionServicio: String?,
    @SerializedName("estado")             val estado: String?,
    @SerializedName("images") val images: List<ImagenDto>? = null,
    @SerializedName("imagenes_url")       val imagenesUrl: List<String>? = null,
    @SerializedName("imagen_url")         val imagenUrl: String? = null,
    val emprendimiento: EmprendimientoDto? = null
)

data class CreateServicioRequest(
    @SerializedName("nombre")               val nombre: String,
    @SerializedName("descripcion")          val descripcion: String?,
    @SerializedName("precio")               val precio: Double,
    @SerializedName("capacidad_maxima")     val capacidadMaxima: Int,
    @SerializedName("duracion_servicio")    val duracionServicio: String?,
    @SerializedName("estado")             val estado: String?,

)

data class UpdateServicioRequest(
    @SerializedName("servicios_id")       val serviciosId: Long,
    @SerializedName("nombre")               val nombre: String,
    @SerializedName("descripcion")          val descripcion: String?,
    @SerializedName("precio")               val precio: Double,
    @SerializedName("capacidad_maxima")     val capacidadMaxima: Int,
    @SerializedName("duracion_servicio")    val duracionServicio: String?,
    @SerializedName("estado")             val estado: String?,
)



fun String.toReqBody() = this.toRequestBody("text/plain".toMediaType())
fun Double.toReqBody() = this.toString().toRequestBody("text/plain".toMediaType())
fun Int.toReqBody() = this.toString().toRequestBody("text/plain".toMediaType())

data class UpdateServicioParts(
    val method: RequestBody,
    val nombre: RequestBody,
    val descripcion: RequestBody?,
    val precio: RequestBody,
    val capacidad: RequestBody,
    val duracion: RequestBody?,
    val estado: RequestBody?
)

fun UpdateServicioRequest.toParts(): UpdateServicioParts = UpdateServicioParts(
    method      = "PUT".toReqBody(),
    nombre      = nombre.toReqBody(),
    descripcion = descripcion?.toReqBody(),
    precio      = precio.toReqBody(),
    capacidad   = capacidadMaxima.toReqBody(),
    duracion    = duracionServicio?.toReqBody(),
    estado      = estado?.toReqBody()
)


data class ImagenDto(
    val id: Long,
    val url: String
)





data class AlojamientoDetalleUi(
    val id: Long,
    val title: String,
    val description: String,
    val priceFormatted: String,
    val rating: Double = 4.5,
    val opiniones: Int = 0,
    val duracionServicio: String,
    val capacidadMaxima: Int,
    val images: List<ImagenDto>?,
    val isFavorite: Boolean,
    val emprendimientoName: String,
    val emprendimientoImageUrl: String,
    val emprendimientoLocation: String
)

fun ServicioResp.toAlojamientoUi(): AlojamientoUi {
    return AlojamientoUi(
        id = serviciosId,
        title = nombre,
        subtitle = descripcion ?: "",
        priceFormatted = "Desde S/ ${"%.2f".format(precio)} / ${duracionServicio ?: "1 noche"}",
        rating = 4.5, // Por ahora fijo o usa rating del backend si lo tienes
        imageUrl = imagenUrl ?: imagenesUrl?.firstOrNull() ?: "https://via.placeholder.com/150",
        isFavorite = false
    )
}

fun ServicioResp.toAlojamientoDetalleUi(): AlojamientoDetalleUi {
    val urlsFromRelation = images?.map { it.url }
    val urlsFromAppend   = imagenesUrl
    val fallbackSingle   = listOfNotNull(imagenUrl)
    val allUrls = urlsFromRelation
        ?: urlsFromAppend
        ?: fallbackSingle
    return AlojamientoDetalleUi(
        id                     = serviciosId,
        title                  = nombre,
        description            = descripcion,
        priceFormatted         = "Desde S/ ${"%.2f".format(precio)} / ${duracionServicio ?: "—"}",
        duracionServicio       = duracionServicio.orEmpty(),
        capacidadMaxima        = capacidadMaxima,
        images = this.images,                            // ← aquí la lista
        isFavorite             = false,
        emprendimientoName     = emprendimiento?.nombre.orEmpty(),
        emprendimientoImageUrl = emprendimiento?.imagenesUrl?.firstOrNull().orEmpty(),
        emprendimientoLocation = emprendimiento?.direccion.orEmpty()   // ← sitio real
    )
}
data class ServicioUi(
    val id: Long,
    val title: String,
    val description: String,
    val priceFormatted: String,
    val rating: Double = 4.5,
    val opiniones: Int = 0,
    val duracionServicio: String,
    val capacidadMaxima: Int,
    val images: List<ImagenDto>?,
    val isFavorite: Boolean,
    val emprendimientoName: String,
    val emprendimientoImageUrl: String,
    val emprendimientoLocation: String
)

fun ServicioResp.toServicioUi(): ServicioUi {
    val urlsFromRelation = images?.map { it.url }
    val urlsFromAppend   = imagenesUrl
    val fallbackSingle   = listOfNotNull(imagenUrl)
    val allUrls = urlsFromRelation
        ?: urlsFromAppend
        ?: fallbackSingle
    return ServicioUi(
        id                     = serviciosId,
        title                  = nombre,
        description            = descripcion,
        priceFormatted         = "Desde S/ ${"%.2f".format(precio)}",
        duracionServicio       = duracionServicio.orEmpty(),
        capacidadMaxima        = capacidadMaxima,
        images = this.images,                            // ← aquí la lista
        isFavorite             = false,
        emprendimientoName     = emprendimiento?.nombre.orEmpty(),
        emprendimientoImageUrl = emprendimiento?.imagenesUrl?.firstOrNull().orEmpty(),
        emprendimientoLocation = emprendimiento?.direccion.orEmpty()
    )
}

data class ServicioEmprendedorUi(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val capacity: Int,
    val duration: String,
    val images: List<ImagenDto> = emptyList(),
    val estado: String?,
    val isActive: Boolean,

)



fun ServicioDto.toServicioEmprendedorUi(): ServicioEmprendedorUi {
    return ServicioEmprendedorUi(
        id          = serviciosId,
        name        = nombre,
        description = descripcion,
        price       = precio,
        capacity    = capacidadMaxima,
        duration    = duracionServicio.orEmpty(),
        images      = images ?: emptyList(),
        estado      = estado,
        isActive    = estado == "activo" // Mapear correctamente el estado
    )
}
