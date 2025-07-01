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
    val metodoPagoId: Long?, // El id del método de pago, en este caso es fijo (1)

    @SerializedName("total_pagado")
    val totalPagado: BigDecimal?, // Pago realizado

    @SerializedName("fecha_pago")
    val fechaPago: String?, // Fecha del pago si se ha realizado

    @SerializedName("metodo_pago")
    val metodoPago: MetodoPagoDto?, // Información detallada del método de pago, si es necesario

    @SerializedName("detalles")
    val detalles: List<DetalleVentaDto> // Detalles de los productos o servicios de la venta
)

// DTO para detalle de la venta
data class DetalleVentaDto(
    @SerializedName("detalle_venta_id")
    val detalleVentaId: Long,

    @SerializedName("venta_id")
    val ventaId: Long,

    @SerializedName("emprendimientos_id")
    val emprendimientosId: Long?, // El id del emprendimiento al que pertenece el producto o servicio

    @SerializedName("productos_id")
    val productosId: Long?, // El id del producto, si es un producto

    @SerializedName("servicios_id")
    val serviciosId: Long?, // El id del servicio, si es un servicio

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precio_unitario")
    val precioUnitario: BigDecimal,

    @SerializedName("subtotal")
    val subtotal: BigDecimal, // Subtotal por producto/servicio

    @SerializedName("producto")
    val producto: ProductoDto?, // Información del producto

    @SerializedName("servicio")
    val servicio: ServicioDto? // Información del servicio
)

// DTO para Producto
data class ProductoDto(
    @SerializedName("productos_id")
    val productosId: Long,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String?, // Descripción del producto

    @SerializedName("precio")
    val precio: BigDecimal, // Precio unitario

    @SerializedName("stock")
    val stock: Int // Cantidad en inventario
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
    val referencia: String, // Referencia de pago (Stripe o algún otro sistema)

    @SerializedName("estado")
    val estado: String // Estado del pago
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
    val tipo: String, // Tipo de movimiento (venta o comisión)

    @SerializedName("monto")
    val monto: BigDecimal, // Monto del movimiento

    @SerializedName("estado")
    val estado: String, // Estado del movimiento (pendiente, liberado, etc.)

    @SerializedName("stripe_id")
    val stripeId: String? // ID de Stripe, si es aplicable
)

// DTO específico para el checkout inicial
data class CheckoutRequest(
    val token: String
)



// DTO específico para procesar el pago
data class PagoRequest(
    @SerializedName("payment_intent_id")
    val paymentIntentId: String // ID del intento de pago (para procesar el pago)
)
