package pe.edu.upeu.appturismo202501.ui.presentation.screens.administrador

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.repository.LoginUserRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import javax.inject.Inject

@HiltViewModel
class AdministradorViewModel @Inject constructor(
    private val loginRepo: LoginUserRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)

    fun logout(
        token: String,
        onLogoutSuccess: () -> Unit,
        onLogoutFailed: (String) -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = loginRepo.logout(token)
                if (response.isSuccessful) {
                    SessionManager.clearSession()
                    TokenUtils.clearToken()
                    onLogoutSuccess()
                } else {
                    onLogoutFailed("Error al cerrar sesión")
                }
            } catch (e: Exception) {
                onLogoutFailed(e.localizedMessage ?: "Error inesperado")
            } finally {
                isLoading = false
            }
        }
    }
}
