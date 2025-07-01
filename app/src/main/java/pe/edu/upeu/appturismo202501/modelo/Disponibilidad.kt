package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName

data class DisponibilidadDto(
    @SerializedName("disponibilidad_servicio_id") val disponibilidadId: Long,
    @SerializedName("servicios_id")               val serviciosId: Long,
    @SerializedName("fecha_inicio")               val fechaInicio: String,
    @SerializedName("fecha_fin")                  val fechaFin: String,
    @SerializedName("hora_inicio")                val horaInicio: String?,
    @SerializedName("hora_fin")                   val horaFin: String?
)