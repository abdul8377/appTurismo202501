package pe.edu.upeu.appturismo202501.data.remote

import retrofit2.Response
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaResp
import retrofit2.http.GET

interface RestZonaTuristica {
    @GET("zonas-turisticas")
    suspend fun getZonas(): Response<List<ZonaTuristicaResp>>
}