package pe.edu.upeu.appturismo202501.ui.presentation.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upeu.appturismo202501.modelo.UsersDto
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ChangePasswordCustomDialog
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.UserItem  // Importa aquí tu componente reutilizable
import androidx.compose.material.icons.filled.Key

@Composable
fun UserScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var selectedUserId by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            users.isEmpty() -> {
                Text(
                    text = "No hay usuarios disponibles",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(users) { user ->
                        UserItem(
                            user = user,
                            onToggleActive = {
                                val newActiveState = user.is_active == 0
                                viewModel.toggleUserActive(user.id, newActiveState)
                            },
                            onChangePasswordClick = {
                                selectedUserId = user.id
                                showChangePasswordDialog = true
                            }
                        )
                    }
                }
            }
        }

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
