package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.Disponibilidad

import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*

import androidx.compose.ui.layout.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import java.time.*
import java.time.format.*
import kotlin.math.*

import android.os.Bundle
import androidx.activity.ComponentActivity

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.* // duplicate removed
// import androidx.compose.material3.* // duplicate removed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// import androidx.compose.material.icons.filled.Add
// import androidx.compose.material3.FloatingActionButton
// import androidx.compose.material3.Icon
// import androidx.compose.material3.Text
// import androidx.compose.runtime.Composable // duplicate with star import
// import androidx.compose.ui.text.TextStyle // needed? used plugin; keep maybe separate
// import androidx.compose.material3.Typography
// import androidx.compose.material3.lightColorScheme
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.servicios.ServiciosViewModel
import pe.edu.upeu.appturismo202501.modelo.ServicioEmprendedorUi
import pe.edu.upeu.appturismo202501.modelo.DisponibilidadDto
import androidx.compose.material3.Typography           // <<– material3, no material
import androidx.compose.ui.text.TextStyle

// Modelos de datos
data class Service(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val duration: Int, // en minutos
    val type: ServiceType
)

enum class ServiceType {
    ACCOMMODATION, GASTRONOMY
}

data class Availability(
    val id: Int,
    val serviceId: Int,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endTime: LocalTime? = null
)

// Estados de la UI
sealed class AvailabilityScreenState {
    object ServiceList : AvailabilityScreenState()
    data class ViewAvailability(val service: Service) : AvailabilityScreenState()
    data class AddAvailability(val service: Service? = null) : AvailabilityScreenState()
    data class DeleteAvailability(val service: Service) : AvailabilityScreenState()
}

@Composable
fun ServiceAvailabilityApp(
    serviciosViewModel: ServiciosViewModel = hiltViewModel(),
    disponibilidadViewModel: DisponibilidadViewModel = hiltViewModel()
) {
    var screenState by remember { mutableStateOf<AvailabilityScreenState>(AvailabilityScreenState.ServiceList) }

    // Cargar servicios del emprendedor al iniciar
    LaunchedEffect(Unit) { serviciosViewModel.loadPropios() }

    val serviciosUi by serviciosViewModel.serviciosEmprendedor.collectAsState()
    val services = serviciosUi.map { it.toService() }

    val dispDtos by disponibilidadViewModel.lista.collectAsState()
    val allAvailabilities = dispDtos.map { it.toAvailability() }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        when (val state = screenState) {
            is AvailabilityScreenState.ServiceList -> {
                ServiceListScreen(
                    services = services,
                    onViewAvailability = { service ->
                        disponibilidadViewModel.fetchAll(service.id.toLong())
                        screenState = AvailabilityScreenState.ViewAvailability(service)
                    },
                    onAddAvailability = { service ->
                        disponibilidadViewModel.fetchAll(service.id.toLong())
                        screenState = AvailabilityScreenState.AddAvailability(service)
                    },
                    onDeleteAvailability = { service ->
                        disponibilidadViewModel.fetchAll(service.id.toLong())
                        screenState = AvailabilityScreenState.DeleteAvailability(service)
                    },
                    onAddNewAvailability = {
                        screenState = AvailabilityScreenState.AddAvailability()
                    }
                )
            }
            is AvailabilityScreenState.ViewAvailability -> {
                val availForService = allAvailabilities.filter { it.serviceId == state.service.id }
                ViewAvailabilityScreen(
                    service = state.service,
                    availabilities = availForService,
                    onBack = { screenState = AvailabilityScreenState.ServiceList }
                )
            }
            is AvailabilityScreenState.AddAvailability -> {
                AddAvailabilityScreen(
                    selectedService = state.service,
                    services = services,
                    onBack = { screenState = AvailabilityScreenState.ServiceList },
                    onServiceSelected = { svc ->
                        disponibilidadViewModel.fetchAll(svc.id.toLong())
                        screenState = AvailabilityScreenState.AddAvailability(svc)
                    },
                    onAvailabilityAdded = { svcId ->
                        disponibilidadViewModel.fetchAll(svcId.toLong())
                        screenState = AvailabilityScreenState.ServiceList
                    }
                )
            }
            is AvailabilityScreenState.DeleteAvailability -> {
                val availForService = allAvailabilities.filter { it.serviceId == state.service.id }
                DeleteAvailabilityScreen(
                    service = state.service,
                    availabilities = availForService,
                    onDelete = { id ->
                        disponibilidadViewModel.delete(id.toLong())
                    },
                    onBack = { screenState = AvailabilityScreenState.ServiceList }
                )
            }
        }
    }
}

@Composable
fun ServiceListScreen(
    services: List<Service>,
    onViewAvailability: (Service) -> Unit,
    onAddAvailability: (Service) -> Unit,
    onDeleteAvailability: (Service) -> Unit,
    onAddNewAvailability: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis Servicios",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            ) // ← cierre de Text

            Spacer(modifier = Modifier.width(8.dp))

            FloatingActionButton(
                onClick = onAddNewAvailability,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Añadir disponibilidad"
                )
            }
        }

        // Lista de servicios
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(services) { service ->
                ServiceCard(
                    service = service,
                    onViewAvailability = { onViewAvailability(service) },
                    onAddAvailability = { onAddAvailability(service) },
                    onDeleteAvailability = { onDeleteAvailability(service) },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ServiceCard(
    service: Service,
    onViewAvailability: () -> Unit,
    onAddAvailability: () -> Unit,
    onDeleteAvailability: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del servicio
            AsyncImage(
                model = service.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentScale = ContentScale.Crop
            )

            // Información del servicio
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Chip(
                        text = if (service.type == ServiceType.ACCOMMODATION) "Alojamiento" else "Gastronomía",
                        color = MaterialTheme.colorScheme.primaryContainer,
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Chip(
                        text = "${service.duration} min",
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        // Botones de acción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton(
                icon = Icons.Filled.Visibility,
                text = "Ver",
                onClick = onViewAvailability
            )

            ActionButton(
                icon = Icons.Filled.Add,
                text = "Añadir",
                onClick = onAddAvailability
            )

            ActionButton(
                icon = Icons.Filled.Delete,
                text = "Eliminar",
                onClick = onDeleteAvailability
            )
        }
    }
}

@Composable
fun ActionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        // 1) Icon con paréntesis cerrados
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(18.dp)
        )
        // 2) Spacer requiere import desde foundation.layout
        Spacer(modifier = Modifier.width(4.dp))
        // 3) Texto al lado del icono
        Text(text = text, fontSize = 14.sp)
    }
}

@Composable
fun Chip(
    text: String,
    color: Color,
    textColor: Color
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ViewAvailabilityScreen(
    service: Service,
    availabilities: List<Availability>,
    onBack: () -> Unit
) {
    val occupiedDates = remember(availabilities) {
        availabilities.flatMap {
            if (it.startDate != null && it.endDate != null) {
                generateSequence(it.startDate) { date -> date.plusDays(1) }
                    .takeWhile { date -> date <= it.endDate }
                    .toList()
            } else {
                emptyList()
            }
        }.toSet()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
            }
            Text(
                text = "Disponibilidad: ${service.name}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
        }

        // Calendario visual
        Text(
            text = "Calendario de Disponibilidad",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        CalendarView(
            occupiedDates = occupiedDates,
            selectedDates = emptySet(),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                .padding(16.dp)
        )

        // Leyenda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Green, CircleShape)
                )
                Text(
                    text = "Disponible",
                    modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.width(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(Color.Red, CircleShape)
                )
                Text(
                    text = "Ocupado",
                    modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun CalendarView(
    occupiedDates: Set<LocalDate>,
    selectedDates: Set<LocalDate> = emptySet(),
    modifier: Modifier = Modifier,
    onDateClick: (LocalDate) -> Unit = {}
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val firstOfMonth = currentMonth.atDay(1)
    // Para alinear domingo=0, lunes=1...
    val offset = firstOfMonth.dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()

    Column(modifier = modifier) {
        // — Header con navegación de meses —
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Mes anterior")
            }
            Text(
                text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Mes siguiente")
            }
        }

        // — Cabecera de días de la semana —
        val weekDays = listOf("Dom","Lun","Mar","Mié","Jue","Vie","Sáb")
        Row(modifier = Modifier.fillMaxWidth()) {
            weekDays.forEach { wd ->
                Text(
                    text = wd,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // — Celdas de fechas —
        // Creamos una lista de YearMonth.atDay(day) o null si es hueco
        val totalCells = ((offset + daysInMonth + 6) / 7) * 7
        val cells = List(totalCells) { idx ->
            val day = idx - offset + 1
            if (idx < offset || day > daysInMonth) null
            else currentMonth.atDay(day)
        }

        cells.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when {
                                    date == null -> MaterialTheme.colorScheme.surfaceVariant
                                    occupiedDates.contains(date) ->
                                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                                    selectedDates.contains(date) ->
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                    else ->
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                }
                            )
                            .clickable(enabled = date != null) {
                                date?.let(onDateClick)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = when {
                                    occupiedDates.contains(date) -> MaterialTheme.colorScheme.onErrorContainer
                                    selectedDates.contains(date) -> MaterialTheme.colorScheme.onPrimaryContainer
                                    else -> MaterialTheme.colorScheme.onPrimaryContainer
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddAvailabilityScreen(
    selectedService: Service?,
    services: List<Service>,
    onBack: () -> Unit,
    onServiceSelected: (Service) -> Unit,
    onAvailabilityAdded: (Int) -> Unit = {}  // devuelve serviceId para refrescar, default vacio
) {
    var currentService by remember { mutableStateOf(selectedService) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
            }
            Text(
                text = if (currentService == null) "Seleccionar Servicio"
                else "Añadir Disponibilidad: ${currentService!!.name}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
        }

        if (currentService == null) {
            // Selección de servicio
            Text(
                text = "Selecciona un servicio para añadir disponibilidad:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(services) { service ->
                    ServiceSelectionItem(
                        service = service,
                        onSelected = {
                            currentService = service
                            onServiceSelected(service)
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        } else {
            // Formulario para añadir disponibilidad
            val service = currentService!!

            val disponibilidadVM: DisponibilidadViewModel = hiltViewModel()

            if (service.type == ServiceType.ACCOMMODATION) {
                DateRangeSelector(
                    onAdd = { start, end ->
                        val dto = DisponibilidadDto(
                            disponibilidadId = 0,
                            serviciosId       = service.id.toLong(),
                            fechaInicio       = start.toString(),
                            fechaFin          = end.toString(),
                            horaInicio        = null,
                            horaFin           = null
                        )
                        disponibilidadVM.create(service.id.toLong(), dto)
                        onAvailabilityAdded(service.id)
                        onBack()
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                TimeSlotSelector(
                    onAdd = { date, startT, endT ->
                        val dto = DisponibilidadDto(
                            disponibilidadId = 0,
                            serviciosId       = service.id.toLong(),
                            fechaInicio       = date.toString(),
                            fechaFin          = date.toString(),
                            horaInicio        = startT.toString(),
                            horaFin           = endT.toString()
                        )
                        disponibilidadVM.create(service.id.toLong(), dto)
                        onAvailabilityAdded(service.id)
                        onBack()
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun ServiceSelectionItem(
    service: Service,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onSelected,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = service.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(Icons.Filled.ArrowForward, contentDescription = "Seleccionar")
        }
    }
}

@Composable
fun DateRangeSelector(
    onAdd: (LocalDate, LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    // Calculamos fechas seleccionadas para resaltarlas
    val selectedDates: Set<LocalDate> = remember(startDate, endDate) {
        if (startDate != null && endDate != null) {
            generateSequence(startDate) { it!!.plusDays(1) }
                .takeWhile { it!! <= endDate }
                .map { it!! }
                .toSet()
        } else emptySet()
    }

    Column(modifier = modifier) {
        Text(
            text = "Selecciona el rango de fechas:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CalendarView(
            occupiedDates = emptySet(),
            selectedDates = selectedDates,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) { clickedDate ->
            if (startDate == null || (startDate != null && endDate != null)) {
                // Empezar nueva selección
                startDate = clickedDate
                endDate = null
            } else {
                // Definir fin si la fecha es posterior o igual
                if (clickedDate >= startDate) {
                    endDate = clickedDate
                } else {
                    // Si es anterior, reiniciamos
                    startDate = clickedDate
                    endDate = null
                }
            }
        }

        // Chips de fechas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            DateChip(
                label = "Inicio",
                date = startDate,
                onClick = { /* Podrías abrir un date picker nativo */ }
            )

            DateChip(
                label = "Fin",
                date = endDate,
                onClick = { /* Podrías abrir un date picker nativo */ }
            )
        }

        Button(
            onClick = { startDate?.let { s -> endDate?.let { e -> onAdd(s, e) } } },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = startDate != null && endDate != null
        ) {
            Text(text = "Añadir Disponibilidad", fontSize = 16.sp)
        }
    }
}

@Composable
fun TimeSlotSelector(
    onAdd: (LocalDate, LocalTime, LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var startTime by remember { mutableStateOf<LocalTime?>(null) }
    var endTime by remember { mutableStateOf<LocalTime?>(null) }

    Column(modifier = modifier) {
        Text(
            text = "Selecciona fecha y horario:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Selector de fecha
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fecha:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(end = 16.dp)
            )
            DateChip(
                label = "Seleccionar fecha",
                date = selectedDate,
                onClick = { /* Abrir selector de fecha */ }
            )
        }

        // Selectores de hora
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TimeChip(
                label = "Hora Inicio",
                time = startTime,
                onClick = { /* Abrir selector de hora */ }
            )

            TimeChip(
                label = "Hora Fin",
                time = endTime,
                onClick = { /* Abrir selector de hora */ }
            )
        }

        // Botón para añadir
        Button(
            onClick = {
                if (selectedDate != null && startTime != null && endTime != null) {
                    onAdd(selectedDate!!, startTime!!, endTime!!)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = selectedDate != null && startTime != null && endTime != null
        ) {
            Text(text = "Añadir Disponibilidad", fontSize = 16.sp)
        }
    }
}

@Composable
fun DateChip(
    label: String,
    date: LocalDate?,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, fontSize = 14.sp)
            if (date != null) {
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TimeChip(
    label: String,
    time: LocalTime?,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, fontSize = 14.sp)
            if (time != null) {
                Text(
                    text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DeleteAvailabilityScreen(
    service: Service,
    availabilities: List<Availability>,
    onDelete: (Int) -> Unit,
    onBack: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf<Availability?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
            }
            Text(
                text = "Eliminar Disponibilidad: ${service.name}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
        }

        if (availabilities.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay disponibilidades registradas para este servicio",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            Text(
                text = "Selecciona una disponibilidad para eliminar:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(availabilities) { availability ->
                    AvailabilityItem(
                        availability = availability,
                        serviceType = service.type,
                        onClick = { showDeleteDialog = availability },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }

    // Diálogo de confirmación de eliminación
    showDeleteDialog?.let { availability ->
        DeleteConfirmationDialog(
            availability = availability,
            serviceType = service.type,
            onConfirm = {
                onDelete(availability.id)
                showDeleteDialog = null
            },
            onDismiss = { showDeleteDialog = null }
        )
    }
}

@Composable
fun AvailabilityItem(
    availability: Availability,
    serviceType: ServiceType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (serviceType == ServiceType.ACCOMMODATION) {
                Text(
                    text = "Alojamiento",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Desde: ${availability.startDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) ?: "N/A"}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Hasta: ${availability.endDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) ?: "N/A"}",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = "Gastronomía",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Fecha: ${availability.startDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) ?: "N/A"}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Horario: ${availability.startTime?.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${availability.endTime?.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    availability: Availability,
    serviceType: ServiceType,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "¿Eliminar disponibilidad?",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (serviceType == ServiceType.ACCOMMODATION) {
                    Text(
                        text = "Desde: ${availability.startDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Hasta: ${availability.endDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = "Fecha: ${availability.startDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Horario: ${availability.startTime?.format(DateTimeFormatter.ofPattern("HH:mm"))} - ${availability.endTime?.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

// Generadores de datos de prueba
fun generateSampleServices(): List<Service> {
    return listOf(
        Service(
            id = 1,
            name = "Hotel Boutique",
            description = "Encantador hotel en el centro histórico con vistas panorámicas",
            imageUrl = "https://images.unsplash.com/photo-1566073771259-6a8506099945",
            duration = 0,
            type = ServiceType.ACCOMMODATION
        ),
        Service(
            id = 2,
            name = "Restaurante Gourmet",
            description = "Experiencia culinaria con productos locales de temporada",
            imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4",
            duration = 120,
            type = ServiceType.GASTRONOMY
        ),
        Service(
            id = 3,
            name = "Cabaña en el Bosque",
            description = "Escape natural con todas las comodidades modernas",
            imageUrl = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914",
            duration = 0,
            type = ServiceType.ACCOMMODATION
        ),
        Service(
            id = 4,
            name = "Taller de Cocina",
            description = "Aprende a preparar platos tradicionales con chefs expertos",
            imageUrl = "https://images.unsplash.com/photo-1600565193348-f74bd3c7ccdf",
            duration = 180,
            type = ServiceType.GASTRONOMY
        )
    )
}

fun generateSampleAvailabilities(): List<Availability> {
    val today = LocalDate.now()
    return listOf(
        Availability(
            id = 1,
            serviceId = 1,
            startDate = today.plusDays(5),
            endDate = today.plusDays(10)
        ),
        Availability(
            id = 2,
            serviceId = 1,
            startDate = today.plusDays(15),
            endDate = today.plusDays(20)
        ),
        Availability(
            id = 3,
            serviceId = 2,
            startDate = today.plusDays(3),
            startTime = LocalTime.of(19, 0),
            endTime = LocalTime.of(22, 0)
        ),
        Availability(
            id = 4,
            serviceId = 2,
            startDate = today.plusDays(7),
            startTime = LocalTime.of(20, 0),
            endTime = LocalTime.of(23, 0)
        ),
        Availability(
            id = 5,
            serviceId = 3,
            startDate = today.plusDays(8),
            endDate = today.plusDays(12)
        ),
        Availability(
            id = 6,
            serviceId = 4,
            startDate = today.plusDays(10),
            startTime = LocalTime.of(10, 0),
            endTime = LocalTime.of(13, 0)
        )
    )
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF0066CC),
            secondary = Color(0xFF66BB6A),
            tertiary = Color(0xFFFFA726),
            surface = Color(0xFFF5F5F5),
            surfaceVariant = Color(0xFFE0E0E0),
            onSurface = Color(0xFF212121)
        ),
        typography = Typography(
            headlineLarge = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            headlineMedium = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            titleMedium = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            ),
            bodyLarge = TextStyle(
                fontSize = 16.sp
            )
        ),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewServiceListScreen() {
    AppTheme {
        ServiceListScreen(
            services = generateSampleServices(),
            onViewAvailability = {},
            onAddAvailability = {},
            onDeleteAvailability = {},
            onAddNewAvailability = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewViewAvailabilityScreen() {
    AppTheme {
        ViewAvailabilityScreen(
            service = generateSampleServices().first(),
            availabilities = generateSampleAvailabilities().filter { it.serviceId == 1 },
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddAvailabilityScreen() {
    AppTheme {
        AddAvailabilityScreen(
            selectedService = generateSampleServices().first(),
            services = generateSampleServices(),
            onBack = {},
            onServiceSelected = {},
            onAvailabilityAdded = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDeleteAvailabilityScreen() {
    AppTheme {
        DeleteAvailabilityScreen(
            service = generateSampleServices().first(),
            availabilities = generateSampleAvailabilities().filter { it.serviceId == 1 },
            onDelete = {},
            onBack = {}
        )
    }
}

// ——————— EXTENSION MAPPERS ————————————
private fun ServicioEmprendedorUi.toService(): Service = Service(
    id          = this.id.toInt(),
    name        = this.name,
    description = this.description,
    imageUrl    = this.images.firstOrNull()?.url ?: "https://via.placeholder.com/150",
    duration    = this.duration.toIntOrNull() ?: 0,
    type        = ServiceType.ACCOMMODATION // TODO map real type if se provee
)

private fun DisponibilidadDto.toAvailability(): Availability {
    val startDate = runCatching { LocalDate.parse(fechaInicio) }.getOrNull()
    val endDate   = runCatching { LocalDate.parse(fechaFin)   }.getOrNull()
    val startTime = horaInicio?.let { runCatching { LocalTime.parse(it) }.getOrNull() }
    val endTime   = horaFin?.let   { runCatching { LocalTime.parse(it) }.getOrNull() }

    return Availability(
        id        = disponibilidadId.toInt(),
        serviceId = serviciosId.toInt(),
        startDate = startDate,
        endDate   = endDate,
        startTime = startTime,
        endTime   = endTime
    )
}