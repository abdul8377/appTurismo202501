package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.AlojamientoDetalleUi
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.AlojamientoUi


data class ServicioDto(
    @SerializedName("servicios_id")
    val serviciosId: Long,
    @SerializedName("emprendimientos_id")
    val emprendimientosId: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @SerializedName("capacidad_maxima")
    val capacidadMaxima: Int,
    @SerializedName("duracion_servicio")
    val duracionServicio: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("imagenes_url")
    val imagenesUrl: List<String>? = null,
    @SerializedName("imagen_url")
    val imagenUrl: String? = null,
    val emprendimiento: EmprendimientoDto? = null
)

data class ServicioResp(
    @SerializedName("servicios_id")
    val serviciosId: Long,
    @SerializedName("emprendimientos_id")
    val emprendimientosId: Long,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    @SerializedName("capacidad_maxima")
    val capacidadMaxima: Int,
    @SerializedName("duracion_servicio")
    val duracionServicio: String?,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("imagenes_url")
    val imagenesUrl: List<String>? = null,
    @SerializedName("imagen_url")
    val imagenUrl: String? = null,
    val emprendimiento: EmprendimientoDto? = null
    )

fun ServicioResp.toServicioUi(): ServicioUi {
    return ServicioUi(
        id = serviciosId,
        title = nombre,
        subtitle = descripcion,
        priceFormatted = "Desde S/ ${"%.2f".format(precio)}",
        rating = 5.0, // O ajusta según tu modelo de rating
        imageUrl = imagenUrl ?: imagenesUrl?.firstOrNull() ?: "https://via.placeholder.com/150"
    )
}

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
    return AlojamientoDetalleUi(
        id = serviciosId,
        title = nombre,
        description = descripcion,
        priceFormatted = "Desde S/ ${"%.2f".format(precio)} / ${duracionServicio ?: "1 noche"}",
        rating = 4.5,  // Valor temporal por defecto
        opiniones = 0,  // Valor temporal por defecto
        duracionServicio = duracionServicio ?: "No especificado",
        capacidadMaxima = capacidadMaxima,
        imageUrl = imagenUrl ?: imagenesUrl?.firstOrNull() ?: "",
        isFavorite = false,  // Local (hasta que implementes favoritos)
        emprendimientoName = emprendimiento?.nombre ?: "Sin nombre",
        emprendimientoImageUrl = emprendimiento?.imagenesUrl?.firstOrNull() ?: ""
    )
}
data class ServicioUi(
    val id: Long,
    val title: String,
    val subtitle: String,
    val priceFormatted: String,
    val rating: Double,
    val imageUrl: String,
    val isFavorite: Boolean = false
)