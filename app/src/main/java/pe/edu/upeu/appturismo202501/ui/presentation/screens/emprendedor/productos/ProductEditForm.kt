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
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ProductoUi
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults

import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditForm(
    navController: NavController,
    productId: Long,
    viewModel: EmprendedorProductoViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current

    // 1. Carga el producto al iniciar
    LaunchedEffect(productId) {
        viewModel.loadProductForEdit(productId)
    }
    val editing by viewModel.editingProduct.collectAsState()
    val categoriesResp by viewModel.categories.collectAsState()
    val isSaving by viewModel::isSaving
    val saveError by viewModel::saveError

    // 2. Mientras no haya producto, muestra loader
    if (editing == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // 3. Mapea ProductResp -> ProductUi
    val initialUi = remember(editing) {
        ProductUi(
            id          = editing!!.id,
            name        = editing!!.nombre,
            description = editing!!.descripcion.orEmpty(),
            price       = editing!!.precio,
            stock       = editing!!.stock,
            estado      = editing!!.estado == "activo",
            categoryId  = editing!!.categoriaProductoId,
            images      = editing!!.images.map { Uri.parse(it.url) }
        )
    }
    val categoriesUi = categoriesResp.map { CategoryUi(it.id, it.nombre) }

    // 4. Lanza tu formulario, pero cambiando onSave por actualizarProducto
    ProducteditScreen(
        initialProduct = initialUi,
        categories     = categoriesUi
    ) { updatedUi, imagesList ->
        // filtro para subir solo los Uri “locales” (scheme != http(s))
        val toUpload = imagesList.filter {
            it.scheme != "http" && it.scheme != "https"
        }
        viewModel.actualizarProducto(
            id          = updatedUi.id!!,
            nombre      = updatedUi.name,
            descripcion = updatedUi.description,
            precio      = updatedUi.price,
            stock       = updatedUi.stock,
            estado      = updatedUi.estado,
            categoryId  = updatedUi.categoryId,
            images      = toUpload
        ) { success ->
            if (success) {
                // esto te devuelve automáticamente al listado
                navController.popBackStack()
            } else {
                Toast.makeText(ctx, viewModel.saveError ?: "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProducteditScreen(
    initialProduct: ProductUi,
    categories: List<CategoryUi>,
    onSave: (updated: ProductUi, newImages: List<Uri>) -> Unit
) {
    // Estados
    var name by remember { mutableStateOf(initialProduct.name) }
    var description by remember { mutableStateOf(initialProduct.description) }
    var priceText  by remember { mutableStateOf(initialProduct.price.toString()) }
    var stockText   by remember { mutableStateOf(initialProduct.stock.toString()) }
    var status by remember { mutableStateOf(initialProduct.estado) }
    var selectedCategory by remember {
        mutableStateOf(
            categories.firstOrNull { it.id == initialProduct.categoryId }
        )
    }
    var images by remember { mutableStateOf(initialProduct.images) }

    var existingImages by remember { mutableStateOf(initialProduct.images) }
    var newImages      by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val price = priceText.toDoubleOrNull() ?: 0.0
    val stock = stockText.toInt() ?: 0

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
                    text = "Editar Producto",
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

            ImagePickerSectionEdit(
                images      = images,
                onImagesChange = { newList ->
                    images = newList.toMutableList()
                }
            )

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
                        // Precio
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { input ->
                                // solo dígitos y punto
                                priceText = input.filter { it.isDigit() || it == '.' }
                            },
                            label = { Text("Precio") },
                            placeholder = { Text("0.00") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Stock
                        var stockExpanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = stockText,
                                onValueChange = { input ->
                                    if (input.all { it.isDigit() }) stockText = input
                                },
                                label = { Text("Stock disponible") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(
                                        onClick = { stockExpanded = true },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.ArrowDropDown,
                                            contentDescription = null
                                        )
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
                                            stockText = value.toString()
                                            stockExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    var selectedCategory by remember {
                        mutableStateOf(
                            // Busca la CategoryUi cuyo id coincide con initialProduct.categoryId
                            categories.firstOrNull { it.id == initialProduct.categoryId }
                        )
                    }

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
                        modifier = Modifier
                            .fillMaxWidth()

                            .clickable { status = !status },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (status) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = null,
                                tint = if (status)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outline
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (status) "Producto visible" else "Producto oculto",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        Switch(
                            checked = status,
                            onCheckedChange = { status = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor    = MaterialTheme.colorScheme.primary,
                                checkedTrackColor    = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor  = MaterialTheme.colorScheme.outline,
                                uncheckedTrackColor  = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            val canSave = name.isNotBlank()
                    && priceText.isNotBlank()
                    && stockText.isNotBlank()
                    && selectedCategory != null

            Box(Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (canSave) {
                            // Crea el UI con todas las imágenes PARA MOSTRAR
                            val updatedUi = ProductUi(
                                id          = initialProduct.id,
                                name        = name,
                                description = description,
                                price       = priceText.toDoubleOrNull() ?: 0.0,
                                stock       = stockText.toIntOrNull() ?: 0,
                                estado      = status,
                                categoryId  = selectedCategory!!.id,
                                images      = images
                            )
                            // Llama al callback pasando EL UI y las nuevas URIs
                            onSave(updatedUi, images)
                        }
                    },
                    enabled = canSave,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(200.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor         = MaterialTheme.colorScheme.primary,
                        contentColor           = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                ) {
                    Icon(Icons.Filled.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Actualizar Producto", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ImagePickerSectionEdit(
    images: List<Uri>,
    onImagesChange: (List<Uri>) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        // fusionalas, quita duplicados, máximo 10
        onImagesChange((images + uris).distinct().take(10))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            Modifier.padding(16.dp)
        ) {
            if (images.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(80.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement   = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    items(images, key = { it.toString() }) { uri ->
                        Box(Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                onClick = { onImagesChange(images - uri) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            OutlinedButton(
                onClick = { launcher.launch("image/*") },
                enabled = images.size < 10,
                modifier = Modifier.fillMaxWidth(),
                border   = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape    = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Outlined.AddPhotoAlternate, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (images.isEmpty()) "Agregar imágenes" else "Agregar más")
            }
        }
    }
}