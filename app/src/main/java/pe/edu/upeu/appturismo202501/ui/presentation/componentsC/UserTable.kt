package pe.edu.upeu.appturismo202501.ui.presentation.componentsC

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pe.edu.upeu.appturismo202501.modelo.UsersDto

@Composable
fun UserTable(
    users: List<UsersDto>,
    onToggleActive: (UsersDto) -> Unit,
    onChangePassword: (UsersDto) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        users.forEach { user ->
            UserRowItem(
                user = user,
                onToggleActive = { onToggleActive(user) },
                onChangePassword = { onChangePassword(user) }
            )
        }
    }
}

