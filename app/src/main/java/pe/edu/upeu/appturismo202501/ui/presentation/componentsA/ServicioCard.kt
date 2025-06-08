package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.modelo.ServicioUi


@Composable
fun ServicioCard(
    id: Long,
    title: String,
    description: String,
    priceFormatted: String,
    rating: Double,
    imageUrl: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onItemClick)
            .padding(bottom = 16.dp)
    ) {
        // Imagen con favorito
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Box {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(R.drawable.ic_launcher_background),
                    error = painterResource(R.drawable.ic_launcher_background)
                )

                // Corazón que cambia de estado
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        // Fondo del ícono (más grande y detrás)
                        if (!isFavorite) {
                            Image(
                                painter = painterResource(id = R.drawable.corazonx), // Cambia esto con tu icono de fondo
                                contentDescription = "Corazón fondo",
                                modifier = Modifier
                                    .size(31.dp)
                                    .align(Alignment.Center)
                            )
                        }

                        // Corazón principal (con el tamaño adecuado)
                        val iconRes = if (isFavorite) R.drawable.corazont else R.drawable.corazontrasnp1
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = "Favorito",
                            modifier = Modifier.size(26.dp), // Ajusta el tamaño del ícono
                            tint = if (isFavorite) Color.Red else Color.Black.copy(alpha = 0.8f) // Fondo oscuro cuando no está marcado
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Título (máximo 2 líneas)
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(4.dp))

        // Descripción corta
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(4.dp))

        // Precio en formato "Desde $40 por viajero"
        Text(
            text = priceFormatted,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
        )

        Spacer(Modifier.height(4.dp))

        // Calificación con estrella
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = "Rating",
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = String.format("%.1f", rating),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable

fun ServicioGrid(
    items: List<ServicioUi>,
    favorites: Map<Long, Boolean>,
    onFavoriteClick: (Long) -> Unit,
    onItemClick: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    ServicioCard(
                        id = item.id,
                        title = item.title,
                        description = item.subtitle,  // breve descripción
                        priceFormatted = item.priceFormatted,
                        rating = item.rating,
                        imageUrl = item.imageUrl,
                        isFavorite = favorites[item.id] ?: false,
                        onFavoriteClick = { onFavoriteClick(item.id) },
                        onItemClick = { onItemClick(item.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Para que quede centrado si es impar
                if (rowItems.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}


