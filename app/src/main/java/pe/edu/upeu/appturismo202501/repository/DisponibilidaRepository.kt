package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestDisponibilidad
import pe.edu.upeu.appturismo202501.modelo.DisponibilidadDto
import javax.inject.Inject
import javax.inject.Singleton



interface DisponibilidadRepository{
    suspend fun fetchAll(servicioId: Long): Result<List<DisponibilidadDto>>
    suspend fun add(servicioId: Long, nueva: DisponibilidadDto): Result<DisponibilidadDto>
    suspend fun remove(id: Long): Result<Unit>
}

@Singleton
class DisponibilidadRepositoryImpl @Inject constructor(
    private val rest: RestDisponibilidad
) : DisponibilidadRepository {

    override suspend fun fetchAll(servicioId: Long): Result<List<DisponibilidadDto>> =
        runCatching {
            val resp = rest.getDisponibilidades(servicioId)
            if (resp.isSuccessful) {
                resp.body().orEmpty()
            } else {
                throw Exception("Error ${resp.code()}: ${resp.message()}")
            }
        }

    override suspend fun add(servicioId: Long, nueva: DisponibilidadDto): Result<DisponibilidadDto> =
        runCatching {
            val resp = rest.createDisponibilidad(servicioId, nueva)
            if (resp.isSuccessful) {
                resp.body()!!
            } else {
                throw Exception("Error ${resp.code()}: ${resp.message()}")
            }
        }

    override suspend fun remove(id: Long): Result<Unit> =
        runCatching {
            val resp = rest.deleteDisponibilidad(id)
            if (resp.isSuccessful) {
                // Retrofit usa Response<Void> para bodies vacíos
                Unit
            } else {
                throw Exception("Error ${resp.code()}: ${resp.message()}")
            }
        }
}