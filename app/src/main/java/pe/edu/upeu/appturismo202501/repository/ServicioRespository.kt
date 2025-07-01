package pe.edu.upeu.appturismo202501.repository

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import pe.edu.upeu.appturismo202501.data.remote.RestServicio
import pe.edu.upeu.appturismo202501.modelo.ServicioDto
import pe.edu.upeu.appturismo202501.modelo.ServicioResp
import pe.edu.upeu.appturismo202501.modelo.UpdateServicioRequest
import pe.edu.upeu.appturismo202501.modelo.toParts
import pe.edu.upeu.appturismo202501.modelo.toReqBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Define las operaciones disponibles para los servicios.
 */
interface ServicioRepository {

    suspend fun servicioFetch(tipoNegocioId: Long): Response<List<ServicioResp>>
    suspend fun servicioDetalle(id: Long): Response<ServicioResp>
    suspend fun fetchServicioEmprendedor(): Result<List<ServicioDto>>
    suspend fun createServicio(
        nombre: String,
        descripcion: String?,
        precio: Double,
        capacidad: Int,
        duracion: String?,
        estado: String?,
        imagenFiles: List<File>?
    ): Result<ServicioDto>

    suspend fun updateServicio(
        req: UpdateServicioRequest,
        newImages: List<File>?,
        eliminarImagenIds: List<Long>?
    ): Result<ServicioDto>

    suspend fun deleteServicio(id: Long): Result<Unit>
}

@Singleton
class ServicioRepositoryImp @Inject constructor(
    private val restServicio: RestServicio
) : ServicioRepository {

    override suspend fun servicioFetch(tipoNegocioId: Long): Response<List<ServicioResp>> =
        restServicio.getServicios(tipoNegocioId)

    override suspend fun servicioDetalle(id: Long): Response<ServicioResp> =
        restServicio.getServicioDetalle(id)

    override suspend fun fetchServicioEmprendedor() = runCatching {
        val resp = restServicio.getServiciosEmprendedor()
        if (resp.isSuccessful) resp.body().orEmpty()
        else throw HttpException(resp)
    }
    override suspend fun createServicio(
        nombre: String,
        descripcion: String?,
        precio: Double,
        capacidad: Int,
        duracion: String?,
        estado: String?,
        imagenFiles: List<File>?
    ) = runCatching {
        // Helper para crear RequestBody
        fun String.toReq() = toRequestBody("text/plain".toMediaType())

        // Prepara partes de imagen
        val parts = imagenFiles?.map { file ->
            val reqFile = file.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("imagenes[]", file.name, reqFile)
        }

        val resp = restServicio.createServicio(
            nombre.toReq(),
            descripcion?.toReq(),
            precio.toString().toReq(),
            capacidad.toString().toReq(),
            duracion?.toReq(),
            estado?.toReq(),
            parts                                   // ahora coincide con `List<Part>?`
        )
        if (resp.isSuccessful) resp.body()!! else throw HttpException(resp)
    }

    override suspend fun updateServicio(
        req: UpdateServicioRequest,
        newImages: List<File>?,
        eliminarImagenIds: List<Long>?
    ) = runCatching {
        val parts = req.toParts()
        val newParts = newImages?.map { file ->
            val reqFile = file.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("imagenes[]", file.name, reqFile)
        }
        val deleteParts = eliminarImagenIds?.map {
            it.toString().toRequestBody("text/plain".toMediaType())
        }
        val resp = restServicio.updateServicio(
            id = req.serviciosId,          // <-- El ID para la URL
            method = parts.method,
            nombre = parts.nombre,
            descripcion = parts.descripcion,
            precio = parts.precio,
            capacidad = parts.capacidad,
            duracion_servicio = parts.duracion,
            estado = parts.estado,
            eliminarImagenes = deleteParts,
            images = newParts
        )

        if (!resp.isSuccessful) {
            Log.e("API", "Error: code=${resp.code()} body=${resp.errorBody()?.string()}")
            throw HttpException(resp)
        }
        resp.body()!!
    }


    override suspend fun deleteServicio(id: Long) = runCatching {
        val resp = restServicio.deleteServicio(id)
        if (!resp.isSuccessful) throw HttpException(resp)
    }


}
