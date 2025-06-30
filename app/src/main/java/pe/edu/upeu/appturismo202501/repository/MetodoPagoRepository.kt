package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestMetodoPago
import pe.edu.upeu.appturismo202501.modelo.MetodoPagoResp
import pe.edu.upeu.appturismo202501.modelo.MetodoPagoDto
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Define las operaciones disponibles para los métodos de pago.
 */
interface MetodoPagoRepository {
    /**
     * Obtiene la lista de métodos de pago.
     *
     * @return Lista de [MetodoPagoResp] envuelta en [Response].
     */
    suspend fun metodoPagoFetch(): Response<List<MetodoPagoResp>>

    /**
     * Obtiene el detalle de un método de pago por su ID.
     *
     * @param id ID del método de pago.
     * @return Detalle del método de pago envuelto en [Response].
     */
    suspend fun metodoPagoDetalle(id: Long): Response<MetodoPagoResp>

    /**
     * Crea un nuevo método de pago.
     *
     * @param metodoPagoDto Datos del nuevo método de pago.
     * @return El nuevo [MetodoPagoResp] envuelto en [Response].
     */
    suspend fun crearMetodoPago(metodoPagoDto: MetodoPagoDto): Response<MetodoPagoResp>

    /**
     * Actualiza un método de pago existente.
     *
     * @param id ID del método de pago a actualizar.
     * @param metodoPagoDto Datos a actualizar del método de pago.
     * @return El método de pago actualizado envuelto en [Response].
     */
    suspend fun actualizarMetodoPago(id: Long, metodoPagoDto: MetodoPagoDto): Response<MetodoPagoResp>

    /**
     * Elimina un método de pago.
     *
     * @param id ID del método de pago a eliminar.
     * @return [Response] vacío.
     */
    suspend fun eliminarMetodoPago(id: Long): Response<Void>

    /**
     * Suspende un método de pago.
     *
     * @param id ID del método de pago a suspender.
     * @return El método de pago suspendido envuelto en [Response].
     */
    suspend fun suspenderMetodoPago(id: Long): Response<MetodoPagoResp>

    /**
     * Activa un método de pago.
     *
     * @param id ID del método de pago a activar.
     * @return El método de pago activado envuelto en [Response].
     */
    suspend fun activarMetodoPago(id: Long): Response<MetodoPagoResp>
}

/**
 * Implementación de [MetodoPagoRepository] que usa Retrofit para comunicarse con la API.
 */
@Singleton
class MetodoPagoRepositoryImp @Inject constructor(
    private val restMetodoPago: RestMetodoPago
) : MetodoPagoRepository {

    override suspend fun metodoPagoFetch(): Response<List<MetodoPagoResp>> =
        restMetodoPago.getMetodosPago()

    override suspend fun metodoPagoDetalle(id: Long): Response<MetodoPagoResp> =
        restMetodoPago.getMetodoPago(id)

    override suspend fun crearMetodoPago(metodoPagoDto: MetodoPagoDto): Response<MetodoPagoResp> =
        restMetodoPago.createMetodoPago(metodoPagoDto)

    override suspend fun actualizarMetodoPago(id: Long, metodoPagoDto: MetodoPagoDto): Response<MetodoPagoResp> =
        restMetodoPago.updateMetodoPago(id, metodoPagoDto)

    override suspend fun eliminarMetodoPago(id: Long): Response<Void> =
        restMetodoPago.deleteMetodoPago(id)

    override suspend fun suspenderMetodoPago(id: Long): Response<MetodoPagoResp> =
        restMetodoPago.suspendMetodoPago(id)

    override suspend fun activarMetodoPago(id: Long): Response<MetodoPagoResp> =
        restMetodoPago.activateMetodoPago(id)
}
