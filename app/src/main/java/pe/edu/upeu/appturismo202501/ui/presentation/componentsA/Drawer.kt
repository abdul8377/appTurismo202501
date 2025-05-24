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
import pe.edu.upeu.appturismo202501.utils.SessionManager


data class DrawerNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String     // ahora ruta en vez de id genérico
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SidebarDrawer(
    items: List<DrawerNavItem>,
    selectedRoute: String,
    onItemClicked: (DrawerNavItem) -> Unit,
    onLogoutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Datos de usuario
    val userId = SessionManager.getUserId()
    val userRole = SessionManager.getUserRole() ?: "No asignado"

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // — Header del Drawer —
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Bienvenido, Usuario",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "ID: $userId",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Rol: $userRole",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // — Items del Drawer —
                items.forEach { item ->
                    val isSelected = item.route == selectedRoute
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
                            selectedIconColor      = MaterialTheme.colorScheme.primary,
                            selectedTextColor      = MaterialTheme.colorScheme.primary,
                            unselectedIconColor    = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor    = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Spacer(Modifier.weight(1f))

                // — Botón Cerrar Sesión —
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
                    Icon(Icons.Default.Logout, contentDescription = "Logout")
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                val currentLabel = items
                    .firstOrNull { it.route == selectedRoute }
                    ?.label
                    ?: items.firstOrNull()?.label
                    ?: ""
                TopAppBar(
                    title = { Text(currentLabel) },
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