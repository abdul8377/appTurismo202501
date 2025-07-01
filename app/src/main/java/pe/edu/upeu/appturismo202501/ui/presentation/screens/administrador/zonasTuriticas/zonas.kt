package pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador.zonasTuriticas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlin.random.Random


// Modelo de datos para Zona Turística
data class TourismZone(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val status: ZoneStatus,
    val rating: Float = Random.nextFloat() * 5
)

enum class ZoneStatus {
    ACTIVE, INACTIVE, PENDING
}

@Composable
fun TourismZonesScreen() {
    val tourismZones = remember { generateSampleZones() }
    var showDeleteDialog by remember { mutableStateOf<TourismZone?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(FilterOption.ALL) }

    val filteredZones = remember(tourismZones, searchQuery, selectedFilter) {
        tourismZones.filter { zone ->
            (zone.name.contains(searchQuery, ignoreCase = true) ||
                    zone.description.contains(searchQuery, ignoreCase = true)) &&
                    (selectedFilter == FilterOption.ALL ||
                            (selectedFilter == FilterOption.ACTIVE && zone.status == ZoneStatus.ACTIVE) ||
                            (selectedFilter == FilterOption.INACTIVE && zone.status == ZoneStatus.INACTIVE) ||
                            (selectedFilter == FilterOption.PENDING && zone.status == ZoneStatus.PENDING))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo degradado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Encabezado
            Text(
                text = "Zonas Turísticas",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Barra de búsqueda
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Filtros
            FilterBar(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Contador de resultados
            Text(
                text = "${filteredZones.size} zonas encontradas",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Lista de zonas
            if (filteredZones.isEmpty()) {
                EmptyState()
            } else {
                TourismZonesList(
                    zones = filteredZones,
                    onEdit = { /* Lógica para editar */ },
                    onDelete = { zone -> showDeleteDialog = zone }
                )
            }
        }

        // Diálogo de confirmación para eliminar
        showDeleteDialog?.let { zone ->
            DeleteConfirmationDialog(
                zone = zone,
                onConfirm = {
                    showDeleteDialog = null
                    // Aquí iría la lógica para eliminar
                },
                onDismiss = { showDeleteDialog = null }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TourismZonesList(
    zones: List<TourismZone>,
    onEdit: (TourismZone) -> Unit,
    onDelete: (TourismZone) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(zones) { zone ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                TourismZoneCard(
                    zone = zone,
                    onEdit = { onEdit(zone) },
                    onDelete = { onDelete(zone) }
                )
            }
        }
    }
}

@Composable
fun TourismZoneCard(
    zone: TourismZone,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            // Imagen con overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = zone.imageUrl,
                    contentDescription = zone.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay degradado
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 100f
                            )
                        )
                )

                // Estado
                StatusBadge(
                    status = zone.status,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )

                // Nombre
                Text(
                    text = zone.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, bottom = 16.dp)
                )
            }

            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Descripción
                Text(
                    text = zone.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Rating
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.1f".format(zone.rating),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ActionButton(
                        text = "Editar",
                        icon = Icons.Filled.Edit,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = onEdit
                    )

                    ActionButton(
                        text = "Eliminar",
                        icon = Icons.Filled.Delete,
                        color = MaterialTheme.colorScheme.error,
                        onClick = onDelete
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: ZoneStatus, modifier: Modifier = Modifier) {
    val (text, color) = when (status) {
        ZoneStatus.ACTIVE -> Pair("Activo", Color(0xFF4CAF50))
        ZoneStatus.INACTIVE -> Pair("Inactivo", Color(0xFFF44336))
        ZoneStatus.PENDING -> Pair("Pendiente", Color(0xFFFFC107))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.9f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.copy(alpha = 0.1f),
            contentColor = color
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f)),
        modifier = Modifier.width(120.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 14.sp)
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        },
        placeholder = {
            Text("Buscar zonas turísticas...")
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
    )
}

enum class FilterOption {
    ALL, ACTIVE, INACTIVE, PENDING
}

@Composable
fun FilterBar(
    selectedFilter: FilterOption,
    onFilterSelected: (FilterOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val filters = listOf(
        FilterOption.ALL to "Todas",
        FilterOption.ACTIVE to "Activas",
        FilterOption.INACTIVE to "Inactivas",
        FilterOption.PENDING to "Pendientes"
    )

    ScrollableTabRow(
        selectedTabIndex = filters.indexOfFirst { it.first == selectedFilter },
        edgePadding = 0.dp,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[filters.indexOfFirst { it.first == selectedFilter }]),
                height = 3.dp,
                color = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    ) {
        filters.forEach { (option, title) ->
            Tab(
                selected = selectedFilter == option,
                onClick = { onFilterSelected(option) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selectedFilter == option) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1) Icon aislado, sin contenido dentro de sus paréntesis
        Icon(
            imageVector = Icons.Filled.Explore,
            contentDescription = "Sin resultados",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
            modifier = Modifier.size(80.dp)
        )

        // 2) Spacer y Text como elementos separados en el Column
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No se encontraron zonas",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Intenta con otro término de búsqueda o filtro",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
    zone: TourismZone,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("ELIMINAR", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR")
            }
        },
        title = {
            Text("Eliminar zona turística", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text("¿Estás seguro que deseas eliminar esta zona?")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = zone.name,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Esta acción no se puede deshacer",
                    color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

// Generador de datos de prueba
fun generateSampleZones(): List<TourismZone> {
    return listOf(
        TourismZone(
            id = 1,
            name = "Montañas Azules",
            description = "Cadena montañosa con vistas panorámicas y rutas de senderismo espectaculares. Ideal para amantes de la naturaleza y la aventura.",
            imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b",
            status = ZoneStatus.ACTIVE,
            rating = 4.7f
        ),
        TourismZone(
            id = 2,
            name = "Cascada Esmeralda",
            description = "Majestuosa cascada de 120 metros de altura rodeada de vegetación exuberante. El sonido del agua crea un ambiente sereno y relajante.",
            imageUrl = "https://images.unsplash.com/photo-1511497584788-876760111969",
            status = ZoneStatus.PENDING,
            rating = 4.5f
        ),
        TourismZone(
            id = 3,
            name = "Playa Dorada",
            description = "Extensa playa de arena fina y dorada con aguas cristalinas. Perfecta para practicar deportes acuáticos y disfrutar del sol.",
            imageUrl = "https://images.unsplash.com/photo-1505228395891-9a51e7e86bf6",
            status = ZoneStatus.ACTIVE,
            rating = 4.9f
        ),
        TourismZone(
            id = 4,
            name = "Valle de las Flores",
            description = "Valle cubierto de flores silvestres durante la primavera. Hogar de diversas especies de aves y mariposas endémicas.",
            imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4",
            status = ZoneStatus.INACTIVE,
            rating = 4.3f
        ),
        TourismZone(
            id = 5,
            name = "Ciudad Antigua",
            description = "Sitio arqueológico con ruinas de una civilización precolombina. Incluye templos, plazas y un museo de artefactos históricos.",
            imageUrl = "https://images.unsplash.com/photo-1564053489984-317bbd824340",
            status = ZoneStatus.ACTIVE,
            rating = 4.8f
        ),
        TourismZone(
            id = 6,
            name = "Bosque Encantado",
            description = "Bosque milenario con árboles gigantes y formaciones rocosas peculiares. Rodeado de leyendas locales y senderos místicos.",
            imageUrl = "https://images.unsplash.com/photo-1448375240586-882707db888b",
            status = ZoneStatus.PENDING,
            rating = 4.2f
        )
    )
}

@Composable
fun TourismZonesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF2E7D32),
            secondary = Color(0xFF0288D1),
            surface = Color(0xFFFFFFFF),
            background = Color(0xFFF5F9F6),
            onSurface = Color(0xFF333333)
        ),
        typography = Typography(
            displayMedium = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.15.sp
            ),
            headlineSmall = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            bodyMedium = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            labelLarge = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        ),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTourismZonesScreen() {
    TourismZonesTheme {
        TourismZonesScreen()
    }
}