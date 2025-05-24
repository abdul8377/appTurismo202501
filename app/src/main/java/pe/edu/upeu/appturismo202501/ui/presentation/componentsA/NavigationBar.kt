package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class NavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val selectedColor: Color = Color.Unspecified
)

@Composable
fun TurismoNavigationBar(
    items: List<NavItem>,
    selectedRoute: String,  // Aquí se acepta selectedRoute
    onItemSelected: (NavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        items.forEach { item ->
            val isSelected = item.route == selectedRoute
            val tintColor = if (item.selectedColor != Color.Unspecified)
                item.selectedColor
            else
                MaterialTheme.colorScheme.primary

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = if (isSelected) tintColor else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) tintColor else Color.Gray
                    )
                },
                alwaysShowLabel = true
            )
        }
    }
}
