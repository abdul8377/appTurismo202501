package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NotificationButton(
    unreadCount: Int = 0,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    iconColor: Color = MaterialTheme.colorScheme.onSurface
) {
    BadgedBox(
        badge = {
            if (unreadCount > 0) {
                Badge { Text(text = unreadCount.toString()) }
            }
        }
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier
                .size(40.dp)
                .clip(shape = CircleShape)
                .background(backgroundColor)
        ) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notificaciones",
                tint = iconColor
            )
        }
    }
}
