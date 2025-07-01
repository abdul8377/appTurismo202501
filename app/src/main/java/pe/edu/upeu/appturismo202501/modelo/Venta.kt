package pe.edu.upeu.appturismo202501.modelo

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

// DTO para la venta actualizada
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
    val totalPagado: BigDecimal?,

    @SerializedName("fecha_pago")
    val fechaPago: String?,

    @SerializedName("metodo_pago")
    val metodoPago: MetodoPagoDto?,

    @SerializedName("detalles")
    val detalles: List<DetalleVentaDto>
)

// DTO para detalle de la venta
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
    val precioUnitario: BigDecimal,

    @SerializedName("subtotal")
    val subtotal: BigDecimal,

    @SerializedName("producto")
    val producto: ProductoDto?,

    @SerializedName("servicio")
    val servicio: ServicioDto?
)

// DTO para Producto
data class ProductoDto(
    @SerializedName("productos_id")
    val productosId: Long,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String?,

    @SerializedName("precio")
    val precio: BigDecimal,

    @SerializedName("stock")
    val stock: Int
)


// DTO para el Pago
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

// DTO para Movimiento Cuenta
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
    val estado: String,

    @SerializedName("stripe_id")
    val stripeId: String?
)

// ↓↓↓↓↓ **Estas dos clases van separadas claramente (no anidadas)** ↓↓↓↓↓

// DTO específico para el checkout inicial
data class CheckoutRequest(
    @SerializedName("metodo_pago_id")
    val metodoPagoId: Long
)

// DTO específico para procesar el pago
data class PagoRequest(
    @SerializedName("payment_intent_id")
    val paymentIntentId: String
)
