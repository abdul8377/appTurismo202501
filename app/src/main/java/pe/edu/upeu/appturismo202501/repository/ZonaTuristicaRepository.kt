package pe.edu.upeu.appturismo202501.repository

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pe.edu.upeu.appturismo202501.data.remote.RestZonaTuristica
import pe.edu.upeu.appturismo202501.modelo.UpdateZonaRequest
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaDto
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaResp
import pe.edu.upeu.appturismo202501.modelo.toParts
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface ZonaTuristicaRepository {
    suspend fun fetchZonas(): Response<List<ZonaTuristicaResp>>
    suspend fun zonaDetalle(id: Long): Response<ZonaTuristicaResp>
    suspend fun createZonas(
        nombre: String,
        descripcion: String?,
        ubicacion: String?,
        estado: String?,
        imagenFile: List<File>?
    ): Result<ZonaTuristicaDto>
    suspend fun updateZonas(
        req: UpdateZonaRequest,
        newImages: List<File>?,
        eliminarImagenIds: List<Long>?
    ): Result<ZonaTuristicaDto>

    suspend fun deleteZona(id: Long): Result<Unit>
}

@Singleton
class ZonaTuristicaRepositoryImpl @Inject constructor(
    private val restZonaTuristica: RestZonaTuristica
): ZonaTuristicaRepository {
    override suspend fun fetchZonas(): Response<List<ZonaTuristicaResp>> =
        restZonaTuristica.getZonas()
    override suspend fun zonaDetalle(id: Long): Response<ZonaTuristicaResp> =
        restZonaTuristica.getZonasById(id)

    override suspend fun createZonas(
        nombre: String,
        descripcion: String?,
        ubicacion: String?,
        estado: String?,
        imagenFile: List<File>?
    ) = runCatching {
        fun String.toReq() =toRequestBody("text/plain".toMediaType())

        val parts =imagenFile?.map { file ->
            val reqFile = file.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("images[]", file.name, reqFile)
        }

        val resp = restZonaTuristica.createZonas(
            nombre.toReq(),
            descripcion?.toReq(),
            ubicacion?.toReq(),
            estado?.toReq(),
            parts
        )
        if (resp.isSuccessful) resp.body()!! else throw HttpException(resp)
    }

    override suspend fun updateZonas(
        req: UpdateZonaRequest,
        newImages: List<File>?,
        eliminarImagenIds: List<Long>?
    ) = runCatching {
        val parts = req.toParts()
        val newParts = newImages?.map { file ->
            val reqFile = file.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("images[]", file.name, reqFile)
        }
        val deleteParts = eliminarImagenIds?.map {
            it.toString().toRequestBody("text/plain".toMediaType())
        }
        val resp = restZonaTuristica.updateZonas(
            id = req.zonaId,
            method = parts.method,
            nombre = parts.nombre,
            descripcion = parts.descripcion,
            ubicacion = parts.ubicacion,
            estado = parts.estado,
            eliminarImagen = deleteParts,
            images = newParts
        )

        if (!resp.isSuccessful) {
            Log.e("API", "Error: code=${resp.code()} body=${resp.errorBody()?.string()}")
            throw HttpException(resp)
        }
        resp.body()!!
    }


    override suspend fun deleteZona(id: Long) = runCatching {
        val resp = restZonaTuristica.deleteZonas(id)
        if (!resp.isSuccessful) throw HttpException(resp)
    }

}

