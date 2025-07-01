package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

data class PaqueteDto(
    @SerializedName("paquetes_id")        val paquetesId: Long,
    @SerializedName("emprendimientos_id") val emprendimientosId: Long,
    val nombre: String,
    val descripcion: String?,
    @SerializedName("precio_total")       val precioTotal: Double,
    val estado: String,
    val servicios: List<ServicioDto>      // Reutiliza tu ServicioDto que ya tiene imágenes
)

data class CreatePaqueteRequest(
    val nombre: String,
    val descripcion: String?,
    @SerializedName("precio_total")   val precioTotal: Double,
    val estado: String,
    val servicios: List<Long>        // IDs de servicios
)

data class UpdatePaqueteRequest(
    val nombre: String?,
    val descripcion: String?,
    @SerializedName("precio_total")   val precioTotal: Double?,
    val estado: String?,
    val servicios: List<Long>?
)

data class TourPackage(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val isActive: Boolean,
    val includedServices: List<String>
)

fun PaqueteDto.toTourPackage(): TourPackage {
    return TourPackage(
        id                = paquetesId,
        name              = nombre,
        description       = descripcion.orEmpty(),
        imageUrl          = servicios.firstOrNull()?.imagenUrl
            ?: "https://via.placeholder.com/300",
        price             = precioTotal,
        isActive          = estado == "activo",
        includedServices  = servicios.map { it.nombre }
    )
}