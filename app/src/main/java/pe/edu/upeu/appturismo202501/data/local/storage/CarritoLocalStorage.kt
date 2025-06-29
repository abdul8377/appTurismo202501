package pe.edu.upeu.appturismo202501.data.local.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import pe.edu.upeu.appturismo202501.modelo.CarritoDto
import pe.edu.upeu.appturismo202501.modelo.CarritoResp

val Context.dataStore by preferencesDataStore("carrito_local")

class CarritoLocalStorage(private val context: Context) {
    private val gson = Gson()
    private val key = stringPreferencesKey("carrito_items")

    suspend fun obtenerCarritoLocal(): List<CarritoResp> {
        val data = context.dataStore.data.first()[key]
        return data?.let {
            gson.fromJson(it, object : TypeToken<List<CarritoResp>>() {}.type)
        } ?: emptyList()
    }

    suspend fun guardarItemCarritoLocal(carritoDto: CarritoDto, stockDisponible: Int? = null) {
        val carritoActual = obtenerCarritoLocal().toMutableList()

        val productoExistente = carritoActual.find {
            it.productosId == carritoDto.productosId && it.serviciosId == carritoDto.serviciosId
        }

        if (productoExistente != null) {
            val nuevaCantidad = (productoExistente.cantidad + carritoDto.cantidad).let { cantidad ->
                if (stockDisponible != null) minOf(cantidad, stockDisponible) else cantidad
            }

            val itemActualizado = productoExistente.copy(
                cantidad = nuevaCantidad,
                subtotal = nuevaCantidad * carritoDto.precioUnitario,
                stockDisponible = stockDisponible
            )

            carritoActual[carritoActual.indexOf(productoExistente)] = itemActualizado
        } else {
            val nuevoItem = CarritoResp(
                carritoId = System.currentTimeMillis(),
                userId = carritoDto.userId,
                productosId = carritoDto.productosId,
                serviciosId = carritoDto.serviciosId,
                cantidad = carritoDto.cantidad,
                precioUnitario = carritoDto.precioUnitario,
                subtotal = carritoDto.subtotal,
                totalCarrito = carritoDto.totalCarrito,
                estado = carritoDto.estado,
                createdAt = null,
                updatedAt = null,
                stockDisponible = stockDisponible
            )
            carritoActual.add(nuevoItem)
        }

        context.dataStore.edit { prefs ->
            prefs[key] = gson.toJson(carritoActual)
        }
    }


    suspend fun limpiarCarritoLocal() {
        context.dataStore.edit { prefs ->
            prefs[key] = gson.toJson(emptyList<CarritoResp>())
        }
    }

    suspend fun eliminarItemCarritoLocal(carritoId: Long) {
        val carritoActual = obtenerCarritoLocal().filterNot { it.carritoId == carritoId }
        context.dataStore.edit { prefs ->
            prefs[key] = gson.toJson(carritoActual)
        }
    }
}