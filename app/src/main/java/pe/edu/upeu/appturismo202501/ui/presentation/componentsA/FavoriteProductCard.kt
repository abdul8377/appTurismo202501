package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pe.edu.upeu.appturismo202501.modelo.ServicioUi
import pe.edu.upeu.appturismo202501.ui.presentation.alertas.AlertDialogComponent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder

@Composable
fun FavoriteProductCard(
    producto: ProductoUi,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Controla apertura del diálogo de confirmación al eliminar
    val showDialog = remember { mutableStateOf(false) }

    Box(modifier = modifier.width(160.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                AsyncImage(
                    model = producto.imageUrl,
                    contentDescription = producto.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = producto.title,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = producto.priceFormatted,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Corazón en la esquina superior derecha
        IconButton(
            onClick = {
                if (isFavorite) showDialog.value = true
                else onFavoriteToggle()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Eliminar favorito" else "Agregar a favoritos",
                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        }

        // Diálogo de confirmación
        AlertDialogComponent(
            isOpen = showDialog,
            title = "Eliminar favorito?",
            message = "¿Deseas quitar este producto de tus favoritos?",
            onDismiss = { /* No hacer nada adicional */ },
            onConfirm = onFavoriteToggle,
            confirmText = "Eliminar",
            dismissText = "Cancelar",
            isSuccess = false
        )
    }
}

@Composable
fun FavoriteServiceCard(
    servicio: ServicioUi,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val showDialog = remember { mutableStateOf(false) }

    Box(modifier = modifier.width(160.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                AsyncImage(
                    model = servicio.imageUrl,
                    contentDescription = servicio.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = servicio.title,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = servicio.priceFormatted,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        IconButton(
            onClick = {
                if (isFavorite) showDialog.value = true
                else onFavoriteToggle()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) "Eliminar favorito" else "Agregar a favoritos",
                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        }

        AlertDialogComponent(
            isOpen = showDialog,
            title = "Eliminar favorito?",
            message = "¿Deseas quitar este servicio de tus favoritos?",
            onDismiss = { },
            onConfirm = onFavoriteToggle,
            confirmText = "Eliminar",
            dismissText = "Cancelar",
            isSuccess = false
        )
    }
}
