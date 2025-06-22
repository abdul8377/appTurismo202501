package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

import pe.edu.upeu.appturismo202501.R
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.ui.presentation.alertas.AlertDialogComponent
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import androidx.navigation.NavController
import android.widget.Toast

data class AlojamientoDetalleUi(
    val id: Long,
    val title: String,
    val description: String,
    val priceFormatted: String,
    val rating: Double = 4.5,
    val opiniones: Int = 0,
    val duracionServicio: String,
    val capacidadMaxima: Int,
    val imageUrl: String,
    val isFavorite: Boolean,
    val emprendimientoName: String,
    val emprendimientoImageUrl: String

)

@Composable
fun ServicioDetailScreen(
    servicio: AlojamientoDetalleUi,
    isFavorite: Boolean,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onCheckAvailabilityClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean,
    navController: NavController
) {
    val showLoginDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize()) {

        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = servicio.imageUrl,
                contentDescription = servicio.title,
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
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .padding(6.dp)
                    )
                }

                IconButton(onClick = {
                    if (isLoggedIn) {
                        onFavoriteClick()
                        val msg = if (!isFavorite) "Agregado a favoritos" else "Eliminado de favoritos"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    } else {
                        showLoginDialog.value = true
                    }
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) Color.Red else Color.White,
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .padding(6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = servicio.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(servicio.rating.toInt()) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Estrella",
                        tint = Color(0xFFFFA000),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "${servicio.rating} (${servicio.opiniones} opiniones)",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = servicio.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                InfoChip(label = "Duración", value = servicio.duracionServicio)
                InfoChip(label = "Capacidad", value = "${servicio.capacidadMaxima} personas")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = servicio.emprendimientoImageUrl,
                    contentDescription = "Emprendimiento",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    servicio.emprendimientoName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onCheckAvailabilityClick,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Consultar disponibilidad")
            }
        }
    }

    AlertDialogComponent(
        isOpen = showLoginDialog,
        title = "Inicia sesión",
        message = "Debes iniciar sesión para gestionar favoritos.",
        onConfirm = { navController.navigate(Destinations.Login.route) },
        onDismiss = { showLoginDialog.value = false },
        confirmText = "Iniciar sesión",
        dismissText = "Cancelar",
        isSuccess = true
    )
}

@Composable
fun InfoChip(label: String, value: String) {
    Column(
        modifier = Modifier
            .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall)
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
    }
}
