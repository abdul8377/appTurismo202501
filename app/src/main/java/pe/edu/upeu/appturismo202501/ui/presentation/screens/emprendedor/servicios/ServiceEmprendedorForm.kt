package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.servicios

import android.content.Context
import android.net.Uri
import android.os.FileUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import pe.edu.upeu.appturismo202501.modelo.CreateServicioRequest
import pe.edu.upeu.appturismo202501.modelo.ServicioEmprendedorUi
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearServicioScreen(
    navController: NavHostController,
    viewModel: ServiciosViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMsg    by remember { mutableStateOf<String?>(null) }

    // Recogemos el resultado del SharedFlow
    LaunchedEffect(Unit) {
        viewModel.operacion.collect { result ->
            isSubmitting = false
            result.fold(
                onSuccess = { navController.popBackStack() },
                onFailure = { errorMsg = it.localizedMessage ?: "Error desconocido" }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Servicio") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }
            )
        }
    ) { padding ->
        AddServiceForm(
            modifier     = Modifier
                .fillMaxSize()
                .padding(padding),
            isLoading    = isSubmitting,
            errorMessage = errorMsg,
            onCancel     = { navController.popBackStack() },

            onSave       = { request: CreateServicioRequest, uris: List<Uri> ->
                Log.d("CrearServicioScreen", "onSave recibido: ${request.nombre}, uris=${uris.size}")
                // Convertimos URIs → Files
                val files = uris.mapNotNull { uri ->
                    runCatching { FileUtil.from(context, uri.toString()) }
                        .getOrNull()
                }
                // Disparamos la creación
                isSubmitting = true
                errorMsg = null
                viewModel.createService(
                    nombre      = request.nombre,
                    descripcion = request.descripcion,
                    precio      = request.precio,
                    capacidad   = request.capacidadMaxima,
                    duracion    = request.duracionServicio,
                    estado      = request.estado,
                    imagenFiles = files
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceForm(
    onSave: (CreateServicioRequest, List<Uri>) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    val context = LocalContext.current

    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val capacity = remember { mutableStateOf("") }
    val duration = remember { mutableStateOf("") }
    val isActive = remember { mutableStateOf(true) }
    val images = remember { mutableStateOf<List<Uri>>(emptyList()) }

    // Estado para controlar el diálogo de selección de imágenes
    val showImagePickerDialog = remember { mutableStateOf(false) }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        images.value = images.value + uris
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Nuevo Servicio") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    Log.d("AddServiceForm", "Guardar pulsado; images=${images.value.size}")
                    Toast.makeText(context, "Guardar pulsado", Toast.LENGTH_SHORT).show()
                    val req = CreateServicioRequest(
                        nombre            = name.value,
                        descripcion       = description.value.takeIf(String::isNotBlank),
                        precio            = price.value.toDoubleOrNull() ?: 0.0,
                        capacidadMaxima   = capacity.value.toIntOrNull() ?: 0,
                        duracionServicio  = duration.value.takeIf(String::isNotBlank),
                        estado            = if (isActive.value) "activo" else "inactivo"
                    )
                    onSave(req, images.value)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = name.value.isNotBlank() && description.value.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                }
                Text("Guardar Servicio")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                // ① Permite scroll cuando el contenido es más alto que la pantalla
                .verticalScroll(rememberScrollState())
                // ② Respeta el espacio del topBar y bottomBar
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección de imágenes
            ImageSelectionSection(
                images        = images.value,
                onAddImage    = { galleryLauncher.launch("image/*") },
                onRemoveImage = { uri -> images.value = images.value - uri }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campos del formulario
            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre del servicio*") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                label = { Text("Descripción*") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campos numéricos en fila
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = price.value,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*\$"))) price.value = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    prefix = { Text("$") }
                )

                OutlinedTextField(
                    value = capacity.value,
                    onValueChange = { if (it.isEmpty() || it.matches(Regex("^\\d*\$"))) capacity.value = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("Capacidad") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    suffix = { Text("pers.") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = duration.value,
                onValueChange = { duration.value = it },
                label = { Text("Duración del servicio") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(Modifier.height(16.dp))

            // — Estado activo/inactivo —
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Estado del servicio", style = MaterialTheme.typography.bodyLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isActive.value) "Activo" else "Inactivo",
                        color = if (isActive.value)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                    Switch(
                        checked = isActive.value,
                        onCheckedChange = { isActive.value = it }
                    )
                }
            }

            // — Mensaje de error (si existe) —
            errorMessage?.let { msg ->
                Spacer(Modifier.height(16.dp))
                Text(text = msg, color = MaterialTheme.colorScheme.error)
            }
        }

    }
}


@Composable
fun ImageSelectionSection(
    images: List<Uri>,
    onAddImage: () -> Unit,
    onRemoveImage: (Uri) -> Unit
) {
    Column {
        Text(
            text = "Imágenes del servicio",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1) Usar 'uriString' (no 'imageUri')
            items(images) { uri  ->
                ImageThumbnail(
                    imageUri = uri,
                    onRemove = { onRemoveImage(uri) }
                )
            }

            // 2) Reemplazar la referencia directa a 'galleryLauncher'
            //    por la llamada a onAddImage()
            item {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .clickable { onAddImage() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.PhotoCamera,
                            contentDescription = "Añadir imagen"
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Galería", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun ImageThumbnail(imageUri: Uri, onRemove: () -> Unit) {
    Box(modifier = Modifier.size(80.dp)) {
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "Imagen del servicio",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Eliminar imagen",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun AddImageButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Default.PhotoCamera,
                contentDescription = "Añadir imagen",
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Añadir",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}


object FileUtil {
    /** Nueva sobrecarga que recibe Uri directamente */
    fun from(context: Context, uri: Uri): File? {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File(context.cacheDir, "upload_${System.nanoTime()}")
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
        return tempFile
    }

    /** (Opcional) para seguir soportando Strings si lo necesitas */
    fun from(context: Context, uriString: String): File? =
        from(context, Uri.parse(uriString))
}
