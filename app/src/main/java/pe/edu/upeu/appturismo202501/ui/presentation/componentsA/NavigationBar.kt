package pe.edu.upeu.appturismo202501.ui.presentation.componentsA

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val selectedColor: Color = Color.Unspecified  // constante, NO composable
)

@Composable
fun TurismoNavigationBar(
    items: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp),
        containerColor = Color.White,
        tonalElevation = 4.dp
    ) {
        items.forEachIndexed { index, item ->
            val isSel = index == selectedIndex
            val tint = if (item.selectedColor != Color.Unspecified)
                item.selectedColor
            else
                MaterialTheme.colorScheme.primary
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSel) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint = if (isSel) tint else Color.Gray
                    )
                },
                label = {
                    Text(
                        item.label,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSel) tint else Color.Gray
                    )
                },
                selected = isSel,
                onClick = { onItemSelected(index) },
                alwaysShowLabel = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TurismoNavigationBarPreview() {
    val demoItems = listOf(
        NavItem("Explorar", Icons.Filled.Place, Icons.Outlined.Place),
        NavItem("Reservas", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        NavItem("Perfil",   Icons.Filled.Face, Icons.Outlined.Face)
    )
    var sel by remember { mutableStateOf(0) }

    TurismoNavigationBar(
        items = demoItems,
        selectedIndex = sel,
        onItemSelected = { sel = it }
    )
}