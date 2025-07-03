package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items

data class PaqueteTuristico(
    val id: Int,
    val nombre: String,
    val imagenes: List<String>,
    val precio: Double,
    val descripcion: String,
    val servicios: List<ServicioPaquete>
)

data class ServicioPaquete(
    val id: Int,
    val nombre: String,
    val imagenes: List<String>,
    val descripcion: String,
    val capacidad: Int
)

// Componente principal
@Composable
fun PaquetesTuristicosSection(
    paquetes: List<PaqueteTuristico>
) {
    var selectedPackage by rememberSaveable { mutableStateOf<PaqueteTuristico?>(null) }

    Column (modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Paquetes Exclusivos",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(
                items = paquetes,              // <- aquí especificas la lista
                key = { it.id }                // opcional, para mejorar performance
            ) { paquete ->
                PaqueteCard(paquete) {
                    selectedPackage = paquete
                }
            }
        }
    }

    selectedPackage?.let { pkg ->
        DetallePaqueteModal(
            paquete = pkg,
            onDismiss = { selectedPackage = null }
        )
    }
}
// Tarjeta de paquete
@Composable
fun PaqueteCard(paquete: PaqueteTuristico, onClick: () -> Unit) {
    Card (
        modifier = Modifier
            .width(280.dp)
            .clickable(onClick = onClick)
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column {
            // Carrusel de imágenes
            Box(modifier = Modifier.height(180.dp)) {
                val pagerState = rememberPagerState (pageCount = { paquete.imagenes.size })
                HorizontalPager (state = pagerState) { page ->
                    AsyncImage(
                        model = paquete.imagenes[page],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Indicadores
                PagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )
            }

            // Contenido
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = paquete.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = paquete.descripcion,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Desde $${paquete.precio}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Modal de detalle
@Composable
fun DetallePaqueteModal(paquete: PaqueteTuristico, onDismiss: () -> Unit) {
    Dialog (onDismissRequest = onDismiss) {
        Surface (
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 24.dp
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                // Header con imagen
                Box(modifier = Modifier.height(240.dp)) {
                    val pagerState = rememberPagerState(pageCount = { paquete.imagenes.size })
                    HorizontalPager(state = pagerState) { page ->
                        AsyncImage(
                            model = paquete.imagenes[page],
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Botón de cerrar
                    IconButton (
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(Color.White.copy(alpha = 0.6f), CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }

                    // Indicadores
                    PagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    )
                }

                // Contenido
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = paquete.nombre,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$${paquete.precio}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = paquete.descripcion,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sección de servicios
                    Text(
                        text = "Servicios Incluidos",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.height(200.dp), // <-- ¡AQUÍ la clave!
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(paquete.servicios) { servicio ->
                            ServicioCard(servicio)
                        }
                    }
                }
            }
        }
    }
}

// Tarjeta de servicio
@Composable
fun ServicioCard(servicio: ServicioPaquete) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column {
            // Carrusel de imágenes
            Box(modifier = Modifier.weight(1f)) {
                val pagerState = rememberPagerState(pageCount = { servicio.imagenes.size })
                HorizontalPager(state = pagerState) { page ->
                    AsyncImage(
                        model = servicio.imagenes[page],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Indicadores
                PagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    dotSize = 6.dp
                )
            }

            // Detalles
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = servicio.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = servicio.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Capacidad: ${servicio.capacidad} personas",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// Componente de indicadores
@Composable
fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    dotSize: Dp = 8.dp,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
) {
    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pagerState.pageCount) { iteration ->
            val color = if (pagerState.currentPage == iteration) activeColor else inactiveColor
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}