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

    // Obtener el ID del usuario y el rol desde SessionManager
    val userId = SessionManager.getUserId()  // Obtener el ID del usuario
    val userRole = SessionManager.getUserRole() ?: "No asignado"  // Obtener el rol del usuario

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Header del Drawer con el nombre del usuario y rol
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Bienvenido, Usuario", // Puedes cambiar esto a un nombre dinámico si lo tienes en SessionManager
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "ID: $userId", // Mostrar el ID del usuario
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Rol: $userRole", // Mostrar el rol del usuario
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
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
