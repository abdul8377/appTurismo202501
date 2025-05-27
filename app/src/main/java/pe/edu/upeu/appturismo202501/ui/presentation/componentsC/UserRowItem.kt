package pe.edu.upeu.appturismo202501.ui.presentation.componentsC

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upeu.appturismo202501.modelo.UsersDto

@Composable
fun UserRowItem(
    user: UsersDto,
    onToggleActive: () -> Unit,
    onChangePassword: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        tonalElevation = 2.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(2f)) {
                Text(user.name, style = MaterialTheme.typography.bodyLarge)
                Text(user.roles.joinToString { it.name }, style = MaterialTheme.typography.labelMedium)
            }

            Text(
                text = if (user.is_active == 1) "🟢 Activo" else "🔴 Inactivo",
                modifier = Modifier.weight(1f),
                color = if (user.is_active == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelLarge
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onToggleActive) {
                    Icon(
                        imageVector = if (user.is_active == 1) Icons.Default.ToggleOn else Icons.Default.ToggleOff,
                        contentDescription = "Cambiar estado"
                    )
                }
                IconButton(onClick = onChangePassword) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = "Cambiar contraseña"
                    )
                }
            }
        }
    }
}
