package pe.edu.upeu.appturismo202501.ui.presentation.screens.ajustes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pe.edu.upeu.appturismo202501.ui.presentation.componentsAjustes.ThemeToggleSetting
import pe.edu.upeu.appturismo202501.ui.presentation.componentsAjustes.ColorSchemeSelector
import pe.edu.upeu.appturismo202501.ui.presentation.componentsAjustes.SecuritySettings
import pe.edu.upeu.appturismo202501.ui.presentation.componentsAjustes.PreferenceSwitches
import pe.edu.upeu.appturismo202501.ui.presentation.componentsAjustes.StorageActions
import pe.edu.upeu.appturismo202501.ui.presentation.componentsAjustes.AboutAppSection

@Composable
fun AjustesScreen(viewModel: AjustesViewModel = hiltViewModel()) {
    val darkModeEnabled = viewModel.darkMode.collectAsState()
    val colorScheme = viewModel.colorScheme.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Text(
                text = "Ajustes",
                style = MaterialTheme.typography.headlineSmall
            )

            ThemeToggleSetting(
                isDarkMode = darkModeEnabled.value,
                onToggle = { viewModel.toggleDarkMode(it) }
            )

            ColorSchemeSelector(
                selectedColor = colorScheme.value,
                onColorSelected = { viewModel.changeColorScheme(it) }
            )

            SecuritySettings(
                onChangePassword = { /* Navegar a cambio de contraseña */ },
                onLogout = { viewModel.logout() /* Agrega navegación si se desea */ }
            )

            PreferenceSwitches()
            StorageActions()
            AboutAppSection()
        }
    }
}