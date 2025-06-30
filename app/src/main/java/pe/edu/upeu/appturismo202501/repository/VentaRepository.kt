package pe.edu.upeu.appturismo202501.repository

import pe.edu.upeu.appturismo202501.data.remote.RestVenta
import pe.edu.upeu.appturismo202501.modelo.*
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Define las operaciones que podemos hacer sobre las ventas.
 */
interface VentaRepository {
    /** Realiza el checkout, crea la venta y los movimientos contables */
    suspend fun realizarCheckout(ventaRequest: VentaDto): Response<VentaDto>

    /** Procesa el pago de una venta */
    suspend fun procesarPago(ventaId: Long, pagoRequest: PagoDto): Response<PagoDto>

    /** Obtiene el historial de compras del usuario */
    suspend fun listarCompras(): Response<List<VentaDto>>
}

/**
 * Implementación concreta que usa Retrofit (RestVenta) para
 * realizar las llamadas a la API de ventas.
 */
@Singleton
class VentaRepositoryImp @Inject constructor(
    private val restVenta: RestVenta
) : VentaRepository {

    override suspend fun realizarCheckout(ventaRequest: VentaDto): Response<VentaDto> =
        restVenta.realizarCheckout(ventaRequest)

    override suspend fun procesarPago(ventaId: Long, pagoRequest: PagoDto): Response<PagoDto> =
        restVenta.procesarPago(ventaId, pagoRequest)

    override suspend fun listarCompras(): Response<List<VentaDto>> =
        restVenta.listarCompras()
}
