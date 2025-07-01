package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestPaquetes
import pe.edu.upeu.appturismo202501.modelo.CreatePaqueteRequest
import pe.edu.upeu.appturismo202501.modelo.PaqueteDto
import pe.edu.upeu.appturismo202501.modelo.UpdatePaqueteRequest
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface PaqueteRepository {
    suspend fun listAll(): Response<List<PaqueteDto>>
    suspend fun listMine(): Result<List<PaqueteDto>>
    suspend fun getById(id: Long): Result<PaqueteDto>
    suspend fun create(req: CreatePaqueteRequest): Result<PaqueteDto>
    suspend fun update(id: Long, req: UpdatePaqueteRequest): Result<PaqueteDto>
    suspend fun delete(id: Long): Result<Unit>
}

@Singleton
class PaqueteRepositoryImpl @Inject constructor(
    private val restPaquetes: RestPaquetes
) : PaqueteRepository {

    override suspend fun listAll() = restPaquetes.getAllPaquetes()

    override suspend fun listMine() = runCatching {
        val resp = restPaquetes.getMyPaquetes()
        if (resp.isSuccessful) resp.body().orEmpty()
        else throw HttpException(resp)
    }

    override suspend fun getById(id: Long) = runCatching {
        val resp = restPaquetes.getPaquete(id)
        if (resp.isSuccessful) resp.body()!!
        else throw HttpException(resp)
    }

    override suspend fun create(req: CreatePaqueteRequest) = runCatching {
        val resp = restPaquetes.createPaquete(req)
        if (resp.isSuccessful) resp.body()!!
        else throw HttpException(resp)
    }

    override suspend fun update(id: Long, req: UpdatePaqueteRequest) = runCatching {
        val resp = restPaquetes.updatePaquete(id, req)
        if (resp.isSuccessful) resp.body()!!
        else throw HttpException(resp)
    }

    override suspend fun delete(id: Long) = runCatching {
        val resp = restPaquetes.deletePaquete(id)
        if (!resp.isSuccessful) throw HttpException(resp)
    }
}