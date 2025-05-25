package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.utils.TokenUtils

class PerfilViewModel : ViewModel() {

    // Variable para almacenar el estado del token
    var isLoggedIn: Boolean = false
        private set

    init {
        checkSession() // Verificar si el usuario está logueado al inicio
    }

    // Función para verificar si hay token y actualizar el estado
    private fun checkSession() {
        isLoggedIn = !SessionManager.getToken().isNullOrEmpty()
    }

    // Función para cerrar sesión
    fun logout() {
        viewModelScope.launch {
            // Limpiar la sesión y token
            TokenUtils.clearToken()
            SessionManager.clearSession()

            // Actualizar el estado de sesión
            isLoggedIn = false
        }
    }
}
