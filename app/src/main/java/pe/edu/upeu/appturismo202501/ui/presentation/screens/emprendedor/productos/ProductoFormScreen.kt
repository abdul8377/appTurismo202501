package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.productos

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import pe.edu.upeu.appturismo202501.modelo.CategoryProductcsResp
import pe.edu.upeu.appturismo202501.modelo.CategoryResp
import pe.edu.upeu.appturismo202501.modelo.CategoryUi
import pe.edu.upeu.appturismo202501.modelo.ProductDto
import pe.edu.upeu.appturismo202501.modelo.ProductUi

import androidx.compose.foundation.lazy.grid.items
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmprendedorProductoFormScreen(
    navController: NavController,
    viewModel: EmprendedorProductoViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    // 1) Categorías desde el repositorio
    val categoriesResp by viewModel.categories.collectAsState()
    val categoriesUi = categoriesResp.map {
        CategoryUi(id = it.id, name = it.nombre)
    }

    // 2) Estados de guardado
    val isSaving by remember { derivedStateOf { viewModel.isSaving } }
    val saveError by remember { derivedStateOf { viewModel.saveError } }

    ProductFormScreen(
        categories = categoriesUi,
        onSave = { ui: ProductUi ->
            viewModel.saveProduct(
                name        = ui.name,
                description = ui.description,
                priceText   = ui.price.toString(),
                stockText   = ui.stock.toString(),
                status      = ui.estado,
                categoryId  = ui.categoryId,
                images      = ui.images
            ) { success ->
                if (success) {
                    navController.popBackStack()
                } else {
                    // Mostrar toast con el error
                    Toast.makeText(ctx, saveError ?: "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    onSave: (ProductUi) -> Unit,
    categories: List<CategoryUi>
) {
    // Estados
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableIntStateOf(0) }
    var status by remember { mutableStateOf(true) }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()) }
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp)
        ) {
            // Header con título y acciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Nuevo Producto",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de imágenes con cards
            Text(
                text = "Galería del Producto",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ImagePickerSection(images = images, onImagesSelected = { images = it })

            Spacer(modifier = Modifier.height(32.dp))

            // Formulario en tarjeta elevada
            Card (
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Campo nombre con icono
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre del producto") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo descripción
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción detallada") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sección precio y stock
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Solo el campo numérico, sin iconos ni sufijos
                        OutlinedTextField(
                            value = price,
                            onValueChange = { input ->
                                // opcional: filtrar para permitir solo dígitos y punto decimal
                                price = input.filter { it.isDigit() || it == '.' }
                            },
                            label = { Text("Precio") },
                            placeholder = { Text("0.00") },
                            modifier = Modifier
                                .weight(1f)
                                .height(IntrinsicSize.Min),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )

                        // Campo stock con stepper
                        var stockExpanded by remember { mutableStateOf(false) }
                        var stockText     by remember { mutableStateOf(stock.toString()) }


                        LaunchedEffect(stockText) {
                            stock = stockText.toIntOrNull() ?: 0
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = stockText,
                                onValueChange = { input ->
                                    // Opcional: filtrar solo dígitos
                                    if (input.all { it.isDigit() }) {
                                        stockText = input
                                    }
                                },
                                label = { Text("Stock disponible") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                // 3) Permitir edición manual
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                trailingIcon = {
                                    IconButton(onClick = { stockExpanded = true }) {
                                        Icon(Icons.Outlined.ArrowDropDown, contentDescription = null)
                                    }
                                }
                            )

                            DropdownMenu(
                                expanded = stockExpanded,
                                onDismissRequest = { stockExpanded = false }
                            ) {
                                (0..100 step 5).forEach { value ->
                                    DropdownMenuItem(
                                        text = { Text("$value unidades") },
                                        onClick = {
                                            stock = value
                                            stockText = value.toString()  // sincroniza ambos
                                            stockExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector de categoría premium
                    var categoryExpanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = selectedCategory?.name.orEmpty(),
                            onValueChange = { /* no cambia nada, es readOnly */ },
                            label = { Text("Categoría") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { categoryExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            leadingIcon = { Icon(Icons.Outlined.Category, contentDescription = null) },
                            trailingIcon = { Icon(Icons.Outlined.ArrowDropDown, contentDescription = null) },
                            readOnly = true,          // impide abrir teclado
                            enabled = false           // opcional, para que no reciba foco
                        )

                        DropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = category.icon ?: Icons.Outlined.Category,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text(category.name)
                                        }
                                    },
                                    onClick = {
                                        selectedCategory = category
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Estado con toggle personalizado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (status) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = null,
                                tint = if (status) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Producto visible",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Switch(
                            checked = status,
                            onCheckedChange = { status = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            val canSave = name.isNotBlank() && selectedCategory != null

            Box(modifier = Modifier.fillMaxWidth()) {
                FloatingActionButton(
                    onClick = {
                        if (canSave) {
                            val product = ProductUi(
                                name        = name,
                                description = description,
                                price       = price.toDoubleOrNull() ?: 0.0,
                                stock       = stock,
                                estado      = status,
                                categoryId  = selectedCategory!!.id,
                                images      = images
                            )
                            onSave(product)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(width = 200.dp, height = 50.dp),
                    containerColor = if (canSave)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(12.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    ),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Save, contentDescription = "Guardar")
                        Spacer(Modifier.width(8.dp))
                        Text("Guardar Producto", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ImagePickerSection(images: List<Uri>, onImagesSelected: (List<Uri>) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris -> onImagesSelected(uris) }
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Miniaturas en grid
            if (images.isNotEmpty()) {
                LazyVerticalGrid (
                    columns = GridCells.Adaptive(minSize = 80.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(images) { uri ->
                        Box(modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Product image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Botón para eliminar
                            IconButton(
                                onClick = {
                                    onImagesSelected(images.toMutableList().apply { remove(uri) })
                                },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.errorContainer,
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Eliminar",
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón para agregar imágenes
            OutlinedButton(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.AddPhotoAlternate,
                        contentDescription = "Agregar imágenes",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (images.isEmpty()) "Agregar imágenes" else "Agregar más imágenes",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            if (images.isEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Máximo 10 imágenes. Primera imagen será la principal.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}