package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pe.edu.upeu.appturismo202501.modelo.ProductResp

@Composable
fun DetalleProducto(
    producto: ProductResp,
    isFavorite: Boolean,
    isLoggedIn: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onAddToCartClick: () -> Unit,
    onCommentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {

        /* ---------- 1. Imagen principal + acciones (retroceder / favorito) ---------- */
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = producto.imagenUrl,
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.55f), CircleShape)
                        .size(36.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.55f), CircleShape)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* ---------- 2. Información del producto ---------- */
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            /* Nombre */
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            /* ---------- 2‑A. Valoración + botón Carrito en la misma fila ---------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                /* 5 estrellas estáticas (puedes cambiarlas por un AverageRating si lo tienes) */
                repeat(5) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Estrella",
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                /* Texto de opiniones */
                Text(
                    "(4.8 opiniones)",
                    style = MaterialTheme.typography.bodySmall
                )

                /* Separa la valoración del botón “Añadir” */
                Spacer(modifier = Modifier.weight(1f))

                /* Botón Carrito: discreto, alineado a la derecha */
                IconButton(
                    onClick = onAddToCartClick,
                    enabled = isLoggedIn, // Desactiva si el usuario no ha iniciado sesión
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (isLoggedIn) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        )
                ) {
                    Icon(
                        Icons.Default.AddShoppingCart,
                        contentDescription = "Añadir al carrito",
                        tint = if (isLoggedIn) MaterialTheme.colorScheme.onPrimary else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            /* Descripción */
            Text(
                text = producto.descripcion ?: "Sin descripción",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            /* Galería horizontal */
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(producto.images) { img ->
                    AsyncImage(
                        model = img.url,
                        contentDescription = img.titulo,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            /* Botón comentarios */
            Button(
                onClick = onCommentClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver Comentarios")
            }
        }
    }
}
