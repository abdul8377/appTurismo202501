package pe.edu.upeu.appturismo202501.modelo

data class TipoDeNegocio(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val created_at: String,
    val updated_at: String,
    val emprendimientos_count: Int
)

data class TipoDeNegocioResponse(
    val success: Boolean,
    val message: String,
    val data: List<TipoDeNegocio>? = null,
    val tipoDeNegocio: TipoDeNegocio? = null
)
