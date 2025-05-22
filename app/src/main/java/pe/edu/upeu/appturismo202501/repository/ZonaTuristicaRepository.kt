package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestZonaTuristica
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaResp
import retrofit2.Response
import javax.inject.Inject

interface ZonaTuristicaRepository {
    suspend fun fetchZonas(): Response<List<ZonaTuristicaResp>>
}

class ZonaTuristicaRepositoryImpl @Inject constructor(
    private val api: RestZonaTuristica
): ZonaTuristicaRepository {
    override suspend fun fetchZonas() = api.getZonas()
}