package pe.edu.upeu.appturismo202501.ui.presentation.componentsC

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.modelo.ProductoUi

@Composable
fun ProductItem(
    producto: ProductoUi,
    cantidad: Int, // Cantidad actual del producto en el carrito
    isFavorite: Boolean,
    onItemClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onIncreaseQuantity: () -> Unit,  // Aumentar la cantidad
    onDecreaseQuantity: () -> Unit,  // Disminuir la cantidad
    onRemoveFromCart: () -> Unit,    // Eliminar el producto del carrito
    modifier: Modifier = Modifier,
    cardWidth: Dp = 180.dp,
    imageHeight: Dp = 120.dp
) {
    Column(
        modifier = modifier
            .width(cardWidth)
            .clickable(onClick = onItemClick)
            .padding(bottom = 16.dp)  // Espacio entre filas
    ) {
        // 1) Card para la imagen + favorito
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            Box {
                AsyncImage(
                    model = producto.imageUrl,
                    contentDescription = producto.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    error = painterResource(R.drawable.ic_launcher_background)
                )
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) Color.Red else Color.Gray
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // 2) Título: máximo 2 líneas
        Text(
            text = producto.title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(4.dp))

        // 3) Descripción
        Text(
            text = producto.subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(4.dp))

        // 4) Precio
        Text(
            text = producto.priceFormatted,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
        )

        Spacer(Modifier.height(4.dp))

        // 5) Rating con estrella
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating",
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = String.format("%.2f", producto.rating),
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(8.dp))

        // 6) Cantidad y acciones de carrito
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón para disminuir cantidad
            IconButton(onClick = onDecreaseQuantity) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = "Disminuir cantidad"
                )
            }

            // Mostrar la cantidad
            Text(
                text = "$cantidad",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            // Botón para aumentar cantidad
            IconButton(onClick = onIncreaseQuantity) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Aumentar cantidad"
                )
            }

            // Botón para eliminar producto del carrito
            IconButton(onClick = onRemoveFromCart) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar del carrito",
                    tint = Color.Red
                )
            }
        }
    }
}
