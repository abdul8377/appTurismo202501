package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

// DTO para la venta
data class VentaDto(
    @SerializedName("venta_id")
    val ventaId: Long,

    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("codigo_venta")
    val codigoVenta: String,

    @SerializedName("total")
    val total: BigDecimal,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("metodo_pago_id")
    val metodoPagoId: Long?,

    @SerializedName("total_pagado")
    val totalPagado: BigDecimal? = null,

    @SerializedName("fecha_pago")
    val fechaPago: String? = null,

    @SerializedName("detalles")
    val detalles: List<DetalleVentaDto>
)

// DTO para el detalle de la venta
data class DetalleVentaDto(
    @SerializedName("detalle_venta_id")
    val detalleVentaId: Long,

    @SerializedName("venta_id")
    val ventaId: Long,

    @SerializedName("emprendimientos_id")
    val emprendimientosId: Long?,

    @SerializedName("productos_id")
    val productosId: Long?,

    @SerializedName("servicios_id")
    val serviciosId: Long?,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio_unitario")
    val precioUnitario: Double,

    @SerializedName("subtotal")
    val subtotal: Double
)

// DTO para el pago
data class PagoDto(
    @SerializedName("pago_id")
    val pagoId: Long,

    @SerializedName("metodo_pago_id")
    val metodoPagoId: Long,

    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("monto")
    val monto: BigDecimal,

    @SerializedName("referencia")
    val referencia: String,

    @SerializedName("estado")
    val estado: String
)


// DTO para el movimiento de cuenta
data class MovimientoCuentaDto(
    @SerializedName("movimiento_cuenta_id")
    val movimientoCuentaId: Long,

    @SerializedName("emprendimientos_id")
    val emprendimientosId: Long,

    @SerializedName("venta_id")
    val ventaId: Long,

    @SerializedName("detalle_venta_id")
    val detalleVentaId: Long,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("monto")
    val monto: BigDecimal,

    @SerializedName("estado")
    val estado: String
)

// DTO para parámetros financieros
data class ParametroFinancieroDto(
    @SerializedName("parametro_financiero_id")
    val parametroFinancieroId: Long,

    @SerializedName("clave")
    val clave: String,

    @SerializedName("valor")
    val valor: String
)
