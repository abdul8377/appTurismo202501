package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.explorar.contentTabs.notificaciones

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    val notifications = listOf(
        "Tu reserva ha sido confirmada",
        "¡Nueva experiencia disponible cerca de ti!",
        "Recibiste un mensaje nuevo",
        "Recordatorio: Visita mañana a las 10:00 am",
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Notificaciones") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Icono de Notificaciones",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            items(notifications) { notification ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(text = notification)
                    }
                }
            }
        }
    }
}