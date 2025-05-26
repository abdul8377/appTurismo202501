package pe.edu.upeu.appturismo202501.ui.presentation.componentsC

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pe.edu.upeu.appturismo202501.modelo.UsersDto

@Composable
fun UserTableWithHeaderPaginated(
    users: List<UsersDto>,
    onToggleActive: (UsersDto) -> Unit,
    onChangePassword: (UsersDto) -> Unit
) {
    val scrollState = rememberScrollState()
    val usersPerPage = 10
    var currentPage by remember { mutableStateOf(0) }

    val pageCount = (users.size + usersPerPage - 1) / usersPerPage
    val paginatedUsers = users.drop(currentPage * usersPerPage).take(usersPerPage)

    Column(Modifier.fillMaxWidth()) {

        // 🔠 CABECERA
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HeaderCell("Nombre", 200.dp)
            HeaderCell("Correo", 200.dp)
            HeaderCell("Estado", 100.dp)
            HeaderCell("Opciones", 100.dp)
        }

        Divider()

        // 📋 FILAS
        Column(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .fillMaxWidth()
        ) {
            paginatedUsers.forEach { user ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(user.name, Modifier.width(200.dp), style = MaterialTheme.typography.bodyLarge)
                    Text(user.email, Modifier.width(200.dp), style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = if (user.is_active == 1) "🟢 Activo" else "🔴 Inactivo",
                        modifier = Modifier.width(100.dp),
                        color = if (user.is_active == 1)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Row(
                        modifier = Modifier.width(100.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(onClick = { onToggleActive(user) }) {
                            Icon(
                                imageVector = if (user.is_active == 1) Icons.Default.ToggleOn else Icons.Default.ToggleOff,
                                contentDescription = "Cambiar estado"
                            )
                        }
                        IconButton(onClick = { onChangePassword(user) }) {
                            Icon(Icons.Default.Key, contentDescription = "Cambiar contraseña")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔢 PAGINACIÓN
        if (pageCount > 1) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { if (currentPage > 0) currentPage-- },
                    enabled = currentPage > 0
                ) {
                    Text("Anterior")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text("Página ${currentPage + 1} de $pageCount")

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = { if (currentPage < pageCount - 1) currentPage++ },
                    enabled = currentPage < pageCount - 1
                ) {
                    Text("Siguiente")
                }
            }
        }
    }
}

@Composable
private fun HeaderCell(title: String, width: Dp) {
    Text(
        text = title,
        modifier = Modifier.width(width),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary
    )
}
