package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.servicios

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.LayoutDirection
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import pe.edu.upeu.appturismo202501.modelo.ServicioEmprendedorUi
import pe.edu.upeu.appturismo202501.modelo.ServicioUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmprendedorServicesListScreen(
    onAddService:    () -> Unit,
    onEditService:   (Long) -> Unit,
    onDeleteService: (Long) -> Unit,
    viewModel:       ServiciosViewModel = hiltViewModel()
) {
    var query by rememberSaveable { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf<Long?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var errorMsg by rememberSaveable { mutableStateOf<String?>(null) }
    var successMsg by rememberSaveable { mutableStateOf<String?>(null) }
    var isDeleting by rememberSaveable { mutableStateOf(false) }

    val servicios by viewModel.serviciosEmprendedor.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPropios()
        // Simulamos un pequeño delay si quieres mostrar el loader
        // delay(300)
        isLoading = false

        viewModel.operacion.collect { res ->
            res.fold(
                onSuccess = {
                    successMsg = "Operación completada exitosamente"
                    viewModel.loadPropios()
                    delay(3000)
                    successMsg = null
                },
                onFailure = {
                    errorMsg = it.message ?: "Error desconocido"
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 1.dp,         // ← aquí controlas el espacio superior
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        // Barra de búsqueda y botón de agregar en la parte superior
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra de búsqueda que ocupa el espacio disponible
            SearchBar(
                query         = query,
                onQueryChange = { query = it },
                onSearch      = { /*…*/ },
                onClear       = { query = "" },
                modifier      = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Botón de agregar servicio
            IconButton(onClick = onAddService) {
                Icon(Icons.Default.Add, contentDescription = "Agregar servicio")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMsg != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
                }
            }
            else -> {
                // Filtrar por query
                val filtrados = servicios.filter {
                    it.name.contains(query, ignoreCase = true)
                }
                if (filtrados.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        EmptyState(
                            messageMain = "No tienes servicios aún",
                            messageSub  = "Pulsa + para agregar uno nuevo"
                        )
                    }
                } else {
                    ServicesList(
                        services      = filtrados,
                        onEditClick   = onEditService,
                        onDeleteClick = { showDeleteDialog = it },
                        modifier      = Modifier.weight(1f)
                    )
                }
            }
        }
        showDeleteDialog?.let { serviceId ->
            val service = servicios.find { it.id == serviceId }
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                icon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                title = {
                    Text("Eliminar Servicio", style = MaterialTheme.typography.headlineSmall)
                },
                text = {
                    Column {
                        Text("¿Seguro que deseas eliminar el servicio:")
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "\"${service?.name.orEmpty()}\"",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Esta acción es irreversible y borrará todas sus imágenes.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            isDeleting = true
                            viewModel.deleteService(serviceId)
                            showDeleteDialog = null
                            isDeleting = false
                        },
                        enabled = !isDeleting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        if (isDeleting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onError
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showDeleteDialog = null },
                        enabled = !isDeleting
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }

        // 5. Snackbar de éxito
        successMsg?.let { msg ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { successMsg = null }) {
                        Text("Cerrar")
                    }
                }
            ) {
                Text(msg)
            }
        }
    }
}
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Buscar servicios...") },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Buscar",
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Limpiar búsqueda",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        singleLine = true,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ServicesList(
    services: List<ServicioEmprendedorUi>,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(services) { service ->
            ServiceCard(
                service = service,
                onEditClick = { onEditClick(service.id) },
                onDeleteClick = { onDeleteClick(service.id) }
            )
        }
    }
}

@Composable
fun ServiceCard(
    service: ServicioEmprendedorUi,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Carrusel de imágenes
            ServiceImageCarousel(images = service.images.map { it.url })

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                StatusIndicator(isActive = service.isActive)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Detalles del servicio
            ServiceDetails(
                price = service.price,
                capacity = service.capacity,
                duration = service.duration
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botones de acción
            ServiceActions(
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ServiceImageCarousel(images: List<String>) {
    if (images.isEmpty()) {
        // fallback sin imágenes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(48.dp))
        }
    } else {
        val pagerState = rememberPagerState()
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager (
                count = images.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = "Imagen ${page + 1}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(Modifier.height(8.dp))
            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun StatusIndicator(isActive: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isActive) Color.Green.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = if (isActive) "Activo" else "Inactivo",
            color = if (isActive) Color.Green else Color.Red,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ServiceDetails(price: Double, capacity: Int, duration: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Precio",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = "$${"%.2f".format(price)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column {
            Text(
                text = "Capacidad",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = "$capacity personas",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Column {
            Text(
                text = "Duración",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = duration,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ServiceActions(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = onEditClick) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Editar",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onDeleteClick) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun EmptyState(
    messageMain: String = "No hay servicios disponibles",
    messageSub: String = "Presiona el botón + para agregar un nuevo servicio"
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = messageMain,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = messageSub,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}