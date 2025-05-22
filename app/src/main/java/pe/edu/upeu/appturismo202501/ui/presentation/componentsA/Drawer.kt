package pe.edu.upeu.appturismo202501.ui.presentation.componentsA


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


data class DrawerNavItem(
    val label: String,
    val icon: ImageVector,
    val id: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SidebarDrawer(
    items: List<DrawerNavItem>,
    selectedItemId: String,
    onItemClicked: (DrawerNavItem) -> Unit,
    onLogoutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Header del Drawer
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Tu App",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Items del Drawer
                items.forEach { item ->
                    val isSelected = item.id == selectedItemId
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = isSelected,
                        onClick = {
                            onItemClicked(item)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                // Botón de Cerrar Sesión
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        scope.launch {
                            drawerState.close()
                            onLogoutClicked()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Cerrar Sesión")
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(items.first { it.id == selectedItemId }.label) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            },
            content = { innerPadding ->
                Box(Modifier.padding(innerPadding)) {
                    content()
                }
            }
        )
    }
}