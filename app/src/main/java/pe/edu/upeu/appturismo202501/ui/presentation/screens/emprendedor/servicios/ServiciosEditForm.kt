package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.servicios

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import pe.edu.upeu.appturismo202501.modelo.ServicioEmprendedorUi
import pe.edu.upeu.appturismo202501.modelo.UpdateServicioRequest
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceEditScreen(
    serviceId: Long,
    navController: NavController,
    viewModel: ServiciosViewModel = hiltViewModel()
) {
    // Estado para feedback y control
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var serviceData by remember { mutableStateOf<ServicioEmprendedorUi?>(null) }
    var isLoading by remember { mutableStateOf(true) }


    // 1. Observa la lista de servicios del ViewModel
    val servicios by viewModel.serviciosEmprendedor.collectAsState()

    // 2. Carga los servicios una vez
    LaunchedEffect(serviceId) {
        viewModel.loadPropios()
    }

    // 3. Cuando cambie la lista, encuentra el servicio específico
    LaunchedEffect(servicios) {
        if (servicios.isNotEmpty()) {
            val encontrado = servicios.find { it.id == serviceId }
            if (encontrado != null) {
                serviceData = encontrado
            } else {
                errorMsg = "Servicio no encontrado"
            }
            isLoading = false
        }
    }
    // 2. UI loading/error
    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    if (errorMsg != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(errorMsg ?: "", color = Color.Red) }
        return
    }

    // 3. Estados locales con los datos cargados
    serviceData?.let { servicio ->
        // States para editar
        val serviceName = remember { mutableStateOf(servicio.name) }
        val description = remember { mutableStateOf(servicio.description) }
        val price = remember { mutableStateOf(servicio.price.toString()) }
        val duration = remember { mutableStateOf(servicio.duration) }
        val capacity = remember { mutableStateOf(servicio.capacity.toString()) }
        val isActive = remember { mutableStateOf(servicio.estado == "activo") }

        // Aquí puedes controlar imágenes actuales, nuevas y a eliminar
        val imagenesActuales = remember { mutableStateListOf<Pair<Long, String>>() } // Pair<ID, URL>
        val nuevasImagenes = remember { mutableStateListOf<File>() }
        val idsAEliminar = remember { mutableStateListOf<Long>() }


        val allImages = imagenesActuales.filter { (id, _) -> id !in idsAEliminar }.map { it.second } +
                nuevasImagenes.map { it.absolutePath } // Solo para preview, luego adaptas

        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val file = FileUtil.from(context, uri) // Tu helper para convertir Uri a File
                if (file != null) nuevasImagenes.add(file)
            }
        }

        // Inicializa solo una vez
        LaunchedEffect(Unit) {
            imagenesActuales.clear()
            imagenesActuales.addAll(servicio.images.map { it.id to it.url }.toList())
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    title = { Text("Editar Servicio", fontWeight = FontWeight.Bold) }
                )
            },
            bottomBar = {
                Button(
                    enabled = !isSubmitting,
                    onClick = {
                        isSubmitting = true
                        val req = UpdateServicioRequest(
                            serviciosId = servicio.id,
                            nombre = serviceName.value,
                            descripcion = description.value,
                            precio = price.value.toDoubleOrNull() ?: 0.0,
                            capacidadMaxima = capacity.value.toIntOrNull() ?: 1,
                            duracionServicio = duration.value,
                            estado = if (isActive.value) "activo" else "inactivo"
                        )
                        viewModel.updateService(
                            req = req,
                            newImages = nuevasImagenes,
                            eliminarImagenIds = idsAEliminar
                        ) { exito ->
                            isSubmitting = false
                            if (exito) navController.popBackStack()
                            else errorMsg = "Error al actualizar"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isSubmitting) CircularProgressIndicator(Modifier.size(18.dp))
                    else Text("Guardar Cambios", fontSize = 16.sp)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                ImageSection(
                    images = allImages,
                    onAddImage = { launcher.launch("image/*") },
                    onRemoveImage = { idx ->
                        // Determina si es actual o nueva (por posición)
                        val countActuales = imagenesActuales.count { it.first !in idsAEliminar }
                        if (idx < countActuales) {
                            // Es actual, márcala para eliminar
                            val actualId = imagenesActuales.filter { it.first !in idsAEliminar }[idx].first
                            idsAEliminar.add(actualId)
                        } else {
                            // Es nueva, quítala de la lista
                            nuevasImagenes.removeAt(idx - countActuales)
                        }
                    }
                )
                // Botón para agregar nueva imagen (puedes usar launcher para picker)
                // nuevasImagenes.add(file) cuando selecciones

                Spacer(modifier = Modifier.height(24.dp))
                // Resto de campos
                CustomTextField(
                    value = serviceName.value,
                    onValueChange = { serviceName.value = it },
                    label = "Nombre del Servicio"
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = "Descripción",
                    isMultiLine = true,
                    maxLines = 4
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomNumberField(
                        value = price.value,
                        onValueChange = { price.value = it },
                        label = "Precio",
                        modifier = Modifier.weight(1f),
                        prefix = "S/"
                    )
                    CustomNumberField(
                        value = duration.value,
                        onValueChange = { duration.value = it },
                        label = "Duración (min)",
                        modifier = Modifier.weight(1f),
                        suffix = "min"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                CustomNumberField(
                    value = capacity.value,
                    onValueChange = { capacity.value = it },
                    label = "Capacidad",
                    suffix = "personas"
                )
                Spacer(modifier = Modifier.height(24.dp))
                StatusToggle(isActive = isActive.value, onToggle = { isActive.value = it })
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSection(
    images: List<String>,
    onAddImage: () -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título con botón de añadir
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Imágenes del Servicio",
                    style = MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = onAddImage,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir imagen",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Añadir", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vista previa principal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (images.isNotEmpty()) {
                    AsyncImage(
                        model = images[0],
                        contentDescription = "Imagen principal",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Añade una imagen principal",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de miniaturas
            Text(
                text = "Todas las imágenes",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(images) { index, imageUrl ->
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .animateItemPlacement()
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Miniatura $index",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        // Botón de eliminar (X)
                        IconButton(
                            onClick = { onRemoveImage(index) },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(22.dp) // Más pequeño el círculo
                                .offset(x = (-6).dp, y = 6.dp) // Mueve la X hacia adentro (no sobresale)
                                .background(
                                    color = Color.Black.copy(alpha = 0.54f), // Gris oscuro/transparente
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Eliminar imagen",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp) // Icono igual de grande, pero el círculo es más chico
                            )
                        }
                    }
                }

                // Botón para añadir más imágenes
                item {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable(onClick = onAddImage),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Añadir más",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Añadir",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    isMultiLine: Boolean = false,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        textStyle = TextStyle(fontSize = 16.sp),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        maxLines = if (isMultiLine) maxLines else 1,
        singleLine = !isMultiLine,
        leadingIcon = icon?.let {
            { Icon(imageVector = icon, contentDescription = null) }
        }
    )
}

@Composable
fun CustomNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    prefix: String = "",
    suffix: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        label = { Text(label) },
        textStyle = TextStyle(fontSize = 16.sp),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        prefix = { if (prefix.isNotEmpty()) Text(prefix) },
        suffix = { if (suffix.isNotEmpty()) Text(suffix) }
    )
}

@Composable
fun StatusToggle(isActive: Boolean, onToggle: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Estado del Servicio",
                style = MaterialTheme.typography.bodyLarge
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isActive) "Activo" else "Inactivo",
                    color = if (isActive) Color(0xFF388E3C) else MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isActive,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF388E3C),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}