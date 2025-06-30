package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.paquetes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import pe.edu.upeu.appturismo202501.R
import pe.edu.upeu.appturismo202501.modelo.ServicioEmprendedorUi
import pe.edu.upeu.appturismo202501.modelo.UpdatePaqueteRequest
import pe.edu.upeu.appturismo202501.modelo.toServicioEmprendedorUi
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.servicios.ServiciosViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTourPackageScreen(
    paqueteId: Long,
    navController: NavController,
    paqueteViewModel: PaquetesViewModel = hiltViewModel(),
    serviciosViewModel: ServiciosViewModel = hiltViewModel()
) {
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Flujos desde ViewModels
    val paquete by paqueteViewModel.currentPackage.collectAsState()
    val serviciosDisponibles by serviciosViewModel.serviciosEmprendedor.collectAsState()
    LaunchedEffect(Unit) { serviciosViewModel.loadPropios() }
    // Estados editables
    val packageName = rememberSaveable() { mutableStateOf("") }
    val packageDescription = rememberSaveable { mutableStateOf("") }
    val packagePrice = rememberSaveable { mutableStateOf("") }
    val isPackageActive = rememberSaveable { mutableStateOf(true) }
    val includedServices = remember { mutableStateListOf<ServicioEmprendedorUi>() }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // 1. Cargar datos al iniciar
    LaunchedEffect (paqueteId) {
        paqueteViewModel.loadPackageById(paqueteId)
        serviciosViewModel.loadPropios()
    }

    // 2. Inicializar campos cuando paquete esté disponible
    LaunchedEffect(paquete) {
        paquete?.let { p ->
            packageName.value = p.nombre
            packageDescription.value = p.descripcion.orEmpty()
            packagePrice.value = p.precioTotal.toString()
            isPackageActive.value = p.estado == "activo"
            includedServices.clear()
            includedServices.addAll(p.servicios.map { it.toServicioEmprendedorUi() })
            isLoading = false
        } ?: kotlin.run {
            // aún no llegó o falla
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }
    errorMsg?.let { msg ->
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(msg, color = MaterialTheme.colorScheme.error) }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header con título y estado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Editar Paquete Turístico",
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            
            Box(
                modifier = Modifier
                    .background(
                        color = if (isPackageActive.value) Color(0xFF2E7D32) else Color(0xFFD32F2F),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (isPackageActive.value) "ACTIVO" else "INACTIVO",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(Color(0xFFF5F7FA))

        ) {
            item {
                // Sección de información básica
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp),

                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        SectionHeader("Información Básica", Icons.Default.Info)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nombre
                        OutlinedTextField(
                            value = packageName.value,
                            onValueChange = { packageName.value = it },
                            label = { Text("Nombre del paquete") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(Icons.Default.Edit, contentDescription = "Nombre")
                            },

                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Descripción
                        OutlinedTextField(
                            value = packageDescription.value,
                            onValueChange = { packageDescription.value = it },
                            label = { Text("Descripción") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            maxLines = 5,
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Precio y estado
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Precio
                            OutlinedTextField(
                                value = packagePrice.value,
                                onValueChange = { packagePrice.value = it },
                                label = { Text("Precio") },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                leadingIcon = {
                                    Text("$", modifier = Modifier.padding(start = 8.dp), color = MaterialTheme.colorScheme.onSurface)
                                },
                                trailingIcon = {
                                    Text("USD", modifier = Modifier.padding(end = 8.dp), color = MaterialTheme.colorScheme.onSurface)
                                },
                                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                )
                            )

                            // Estado
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Activo", modifier = Modifier.padding(end = 8.dp), color = MaterialTheme.colorScheme.onSurface)
                                Switch(
                                    checked = isPackageActive.value,
                                    onCheckedChange = { isPackageActive.value = it }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))


                        //seccion imagenes del paquete
                        Text(
                            "Imágenes del paquete",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(8.dp))


                        val serviceImages = includedServices
                            .flatMap { svc -> svc.images.map { img -> img.url } }

                        if (serviceImages.isEmpty()) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "No hay imágenes disponibles",
                                    color = Color.Gray
                                )
                            }
                        } else {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(190.dp)
                            ) {
                                items(
                                    items = serviceImages,
                                    key = { it } // la URL sirve de clave única
                                ) { imageUrl ->
                                    Card(
                                        shape = RoundedCornerShape(8.dp),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        modifier = Modifier.size(190.dp)
                                    ) {
                                        AsyncImage(
                                            model = imageUrl,
                                            contentDescription = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Servicios incluidos
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SectionHeader("Servicios Incluidos", Icons.Default.Info)

                            Text(
                                text = "${includedServices.size} servicios",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (includedServices.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay servicios incluidos",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                            ) {
                                items(includedServices) { service ->
                                    IncludedServiceItem(
                                        service = service,
                                        onRemove = { includedServices.remove(service) }
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                // Añadir servicios
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        SectionHeader("Añadir Servicios", Icons.Default.Add)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Buscador
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Buscar servicios") },
                            modifier = Modifier.fillMaxWidth(),
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Buscar")
                            },
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Servicios disponibles
                        Text(
                            text = "Servicios Disponibles",
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        val filteredServices = if (searchQuery.isBlank()) {
                            serviciosDisponibles
                        } else {
                            serviciosDisponibles.filter {
                                it.name.contains(searchQuery, ignoreCase = true)
                            }
                        }


                        if (filteredServices.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No se encontraron servicios",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            ) {
                                items(filteredServices) { service ->
                                    AvailableServiceItem(
                                        service = service,
                                        onAdd = {
                                            if (!includedServices.contains(service)) {
                                                includedServices.add(service)
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { navController.popBackStack()  },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = {
                    val req = UpdatePaqueteRequest(
                        nombre = packageName.value,
                        descripcion = packageDescription.value,
                        precioTotal = packagePrice.value.toDoubleOrNull(),
                        estado = if (isPackageActive.value) "activo" else "inactivo",
                        servicios = includedServices.map { it.id }
                    )
                    paqueteViewModel.updatePaquete(paqueteId, req)
                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Guardar Cambios")
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@Composable
fun IncludedServiceItem(
    service: ServicioEmprendedorUi,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = service.images.firstOrNull()?.url
                    ?: "https://via.placeholder.com/150", // placeholder si no hay URL
                contentDescription = service.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = service.name,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = service.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AvailableServiceItem(
    service: ServicioEmprendedorUi,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(140.dp)
            .clickable { onAdd() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = service.images.firstOrNull()?.url
                    ?: "https://via.placeholder.com/150", // placeholder si no hay URL
                contentDescription = service.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = service.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}