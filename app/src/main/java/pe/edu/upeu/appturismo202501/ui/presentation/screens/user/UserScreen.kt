package pe.edu.upeu.appturismo202501.ui.presentation.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ChangePasswordCustomDialog
import pe.edu.upeu.appturismo202501.ui.presentation.componentsC.SummaryCard
import pe.edu.upeu.appturismo202501.ui.presentation.componentsC.RoleFilterDropdown
import pe.edu.upeu.appturismo202501.ui.presentation.componentsC.UserTableWithHeaderPaginated
import pe.edu.upeu.appturismo202501.ui.presentation.componentsC.SearchToggleField

@Composable
fun UserScreen(viewModel: UserViewModel = hiltViewModel()) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var selectedRole by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableStateOf<Long?>(null) }

    // Cargar usuarios al cambiar rol
    LaunchedEffect(selectedRole) {
        viewModel.loadUsers(role = selectedRole)
    }

    // Filtrado por búsqueda local
    val filteredUsers = users.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.email.contains(searchQuery, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 🔷 Tarjetas de resumen
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                SummaryCard("Usuarios", users.size.toString(), Icons.Default.Group)
            }
            item {
                SummaryCard(
                    "Emprendedores",
                    users.count { it.roles.any { r -> r.name == "Emprendedor" } }.toString(),
                    Icons.Default.People
                )
            }
            item {
                SummaryCard(
                    "Administradores",
                    users.count { it.roles.any { r -> r.name == "Administrador" } }.toString(),
                    Icons.Default.Person
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔽 Filtro por rol (100%)
        RoleFilterDropdown(
            selectedRole = selectedRole,
            onRoleSelected = { selectedRole = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        // 🔍 Buscador (100%)
        SearchToggleField(
            onQueryChanged = { searchQuery = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔄 Estado o tabla paginada
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
                }
            }

            else -> {
                UserTableWithHeaderPaginated(
                    users = filteredUsers,
                    onToggleActive = { user ->
                        viewModel.toggleUserActive(user.id, !user.is_active)
                    },
                    onChangePassword = { user ->
                        selectedUserId = user.id
                        showChangePasswordDialog = true
                    }
                )

            }
        }

        // 🔐 Diálogo para cambio de contraseña
        if (showChangePasswordDialog && selectedUserId != null) {
            ChangePasswordCustomDialog(
                userId = selectedUserId!!,
                showDialog = showChangePasswordDialog,
                onDismiss = { showChangePasswordDialog = false },
                onChangePassword = { userId, password, confirmPassword ->
                    viewModel.changeUserPassword(userId, password, confirmPassword)
                },
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        }
    }
}
