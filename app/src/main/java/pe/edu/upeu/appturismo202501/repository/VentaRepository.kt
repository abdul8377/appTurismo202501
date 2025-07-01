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
    /** Realiza el checkout inicial creando una venta pendiente */
    suspend fun realizarCheckout(checkoutRequest: CheckoutRequest): Response<VentaDto>

    /** Procesa el pago usando un PaymentIntent de Stripe */
    suspend fun procesarPago(ventaId: Long, pagoRequest: PagoRequest): Response<VentaDto>

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

    override suspend fun realizarCheckout(checkoutRequest: CheckoutRequest): Response<VentaDto> =
        restVenta.realizarCheckout(checkoutRequest)

    override suspend fun procesarPago(ventaId: Long, pagoRequest: PagoRequest): Response<VentaDto> =
        restVenta.procesarPago(ventaId, pagoRequest)

    override suspend fun listarCompras(): Response<List<VentaDto>> =
        restVenta.listarCompras()
}