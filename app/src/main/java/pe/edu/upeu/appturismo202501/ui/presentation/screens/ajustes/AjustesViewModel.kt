package pe.edu.upeu.appturismo202501.ui.presentation.screens.ajustes

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject

@HiltViewModel
class AjustesViewModel @Inject constructor() : ViewModel() {

    // Estado para el modo oscuro
    private val _darkMode = MutableStateFlow(SessionManager.isDarkMode())
    val darkMode: StateFlow<Boolean> = _darkMode

    // Estado para el esquema de color (ej: "Rojo", "Verde", "Púrpura")
    private val _colorScheme = MutableStateFlow(SessionManager.getColorScheme())
    val colorScheme: StateFlow<String> = _colorScheme

    // Cambiar modo oscuro
    fun toggleDarkMode(enabled: Boolean) {
        _darkMode.value = enabled
        SessionManager.setDarkMode(enabled)
    }

    // Cambiar esquema de color
    fun changeColorScheme(newColor: String) {
        _colorScheme.value = newColor
        SessionManager.setColorScheme(newColor)
    }

    // Cerrar sesión
    fun logout() {
        SessionManager.clearSession()
    }
}
