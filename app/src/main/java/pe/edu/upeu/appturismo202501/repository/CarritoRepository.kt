package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestCarrito
import pe.edu.upeu.appturismo202501.modelo.CarritoDto
import pe.edu.upeu.appturismo202501.modelo.CarritoResp
import retrofit2.Response
import javax.inject.Inject

interface CarritoRepository {
    suspend fun obtenerCarrito(): Response<List<CarritoResp>>
    suspend fun agregarItemAlCarrito(carritoDto: CarritoDto): Response<CarritoResp>
    suspend fun actualizarItemEnCarrito(carritoId: Long, carritoDto: CarritoDto): Response<CarritoResp>
    suspend fun eliminarItemDelCarrito(carritoId: Long): Response<Void>
}

class CarritoRepositoryImpl @Inject constructor(
    private val restCarrito: RestCarrito
) : CarritoRepository {

    override suspend fun obtenerCarrito(): Response<List<CarritoResp>> {
        return restCarrito.getCarrito()
    }

    override suspend fun agregarItemAlCarrito(carritoDto: CarritoDto): Response<CarritoResp> {
        return restCarrito.agregarItemCarrito(carritoDto)
    }

    override suspend fun actualizarItemEnCarrito(carritoId: Long, carritoDto: CarritoDto): Response<CarritoResp> {
        return restCarrito.actualizarItemCarrito(carritoId, carritoDto)
    }

    override suspend fun eliminarItemDelCarrito(carritoId: Long): Response<Void> {
        return restCarrito.eliminarItemCarrito(carritoId)
    }
}
