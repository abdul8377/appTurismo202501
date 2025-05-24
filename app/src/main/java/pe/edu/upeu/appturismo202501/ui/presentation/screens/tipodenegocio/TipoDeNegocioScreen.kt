package pe.edu.upeu.appturismo202501.ui.presentation.screens.tipodenegocio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upeu.appturismo202501.modelo.TipoDeNegocio
import pe.edu.upeu.appturismo202501.ui.presentation.components.TipoDeNegocioItem
import pe.edu.upeu.appturismo202501.ui.presentation.search.SearchBarWithHistory
import pe.edu.upeu.appturismo202501.ui.presentation.alertas.AlertDialogComponent
import pe.edu.upeu.appturismo202501.ui.presentation.alertas.SnackbarNotification
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.navigation.NavHostController
import pe.edu.upeu.appturismo202501.ui.navigation.Destinations
import pe.edu.upeu.appturismo202501.ui.presentation.components.TipoDeNegocioForm

@Composable
fun TipoDeNegocioScreen(
    navController: NavHostController, // Asegúrate de pasar el navController aquí
    viewModel: TipoDeNegocioViewModel = hiltViewModel()
) {
    val tiposDeNegocio by viewModel.tiposDeNegocio.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Estados para controlar la visibilidad del formulario y los diálogos
    val isFormVisible = remember { mutableStateOf(false) }
    val tipoDeNegocioToEdit = remember { mutableStateOf<TipoDeNegocio?>(null) }

    // Estados para los diálogos y snackbar
    val isDeleteDialogOpen = remember { mutableStateOf(false) }
    val snackbarMessage = remember { mutableStateOf("") }
    val isSnackbarVisible = remember { mutableStateOf(false) }

    // Mensajes de alerta
    val dialogTitle = remember { mutableStateOf("") }
    val dialogMessage = remember { mutableStateOf("") }

    // Seguimiento de cambios en la consulta de búsqueda
    var searchQuery by remember { mutableStateOf("") }

    // Historial de búsqueda
    val searchHistory = remember { mutableStateOf(listOf<String>()) }

    LaunchedEffect(Unit) {
        viewModel.loadTiposDeNegocio()  // Cargar los tipos de negocio inicialmente
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Barra de búsqueda
            SearchBarWithHistory(
                textFieldState = remember { mutableStateOf(TextFieldState()) }.value,
                onSearch = { query ->
                    viewModel.searchTiposDeNegocio(query)
                    // Actualizar el historial de búsqueda
                    if (query.isNotEmpty() && query !in searchHistory.value) {
                        searchHistory.value = searchHistory.value + query
                    }
                },
                searchResults = tiposDeNegocio.map { it.nombre },
                searchHistory = searchHistory.value,
                onHistoryItemClick = { historyItem ->
                    searchQuery = historyItem
                    viewModel.searchTiposDeNegocio(historyItem)
                }
            )

            // Mostrar los resultados filtrados en un LazyColumn
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                tiposDeNegocio.isEmpty() -> {
                    Text(
                        text = "No hay tipos de negocio disponibles",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentPadding = PaddingValues(2.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(tiposDeNegocio) { tipoDeNegocio ->
                            TipoDeNegocioItem(
                                tipoDeNegocio = tipoDeNegocio,
                                onViewClick = {
                                    val destino = Destinations.VerTipoDeNegocio
                                        .route.replace("{id}", tipoDeNegocio.id.toString())
                                    navController.navigate(destino)
                                },
                                onEditClick = {
                                    tipoDeNegocioToEdit.value = tipoDeNegocio
                                    isFormVisible.value = true // Mostrar formulario de edición
                                },
                                onDeleteClick = {
                                    if (tipoDeNegocio.emprendimientos_count > 0) {
                                        dialogTitle.value = "No se puede eliminar"
                                        dialogMessage.value = "Este tipo de negocio está vinculado a un emprendimiento y no se puede eliminar."
                                        isDeleteDialogOpen.value = true // Mostrar alerta de error
                                    } else {
                                        dialogTitle.value = "Confirmar eliminación"
                                        dialogMessage.value = "¿Estás seguro de que deseas eliminar este tipo de negocio?"
                                        tipoDeNegocioToEdit.value = tipoDeNegocio
                                        isDeleteDialogOpen.value = true // Mostrar alerta de confirmación
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        // Botón flotante para crear un nuevo tipo de negocio
        FloatingActionButton(
            onClick = {
                tipoDeNegocioToEdit.value = null // Crear un nuevo tipo de negocio
                isFormVisible.value = true // Mostrar el formulario de creación
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar Tipo de Negocio")
        }
    }

    // Mostrar el formulario de agregar o editar tipo de negocio
    if (isFormVisible.value) {
        TipoDeNegocioForm(
            tipoDeNegocio = tipoDeNegocioToEdit.value,
            onSubmit = { tipoDeNegocio ->
                if (tipoDeNegocio.id == 0L) {
                    viewModel.createOrUpdateTipoDeNegocio(tipoDeNegocio) // Crear nuevo tipo de negocio
                    snackbarMessage.value = "Tipo de negocio creado con éxito"
                    isSnackbarVisible.value = true
                } else {
                    viewModel.createOrUpdateTipoDeNegocio(tipoDeNegocio) // Editar tipo de negocio
                    snackbarMessage.value = "Tipo de negocio actualizado con éxito"
                    isSnackbarVisible.value = true
                }
                isFormVisible.value = false // Cerrar el formulario
                viewModel.loadTiposDeNegocio() // Recargar los tipos de negocio
            },
            onDismiss = { isFormVisible.value = false } // Cerrar el formulario
        )
    }

    // Mostrar Snackbar para éxito de operación
    if (isSnackbarVisible.value) {
        SnackbarNotification(
            message = snackbarMessage.value,
            isSuccess = true, // Cambiar a `false` si se trata de un error
            onDismiss = { isSnackbarVisible.value = false }
        )
    }

    // Mostrar el cuadro de diálogo de confirmación de eliminación
    AlertDialogComponent(
        isOpen = isDeleteDialogOpen,
        title = dialogTitle.value,
        message = dialogMessage.value,
        onDismiss = { isDeleteDialogOpen.value = false },
        onConfirm = {
            tipoDeNegocioToEdit.value?.let { tipoDeNegocio ->
                viewModel.deleteTipoDeNegocio(tipoDeNegocio.id) // Eliminar el tipo de negocio
            }
            isDeleteDialogOpen.value = false
            snackbarMessage.value = "Tipo de negocio eliminado con éxito"
            isSnackbarVisible.value = true // Mostrar Snackbar de eliminación exitosa
        },
        isSuccess = false
    )
}

