package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.UsersDto
import pe.edu.upeu.appturismo202501.repository.UserRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import javax.inject.Inject

data class UserState(
    val name: String = "",
    val lastName: String = "",  // <-- valor por defecto para evitar null
    val email: String = "",
    val role: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)


@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState

    init {
        // Solo intenta cargar si hay token e id válido
        val token = SessionManager.getToken()
        val userId = SessionManager.getUserId().takeIf { it != -1 }?.toLong()
        if (token != null && userId != null) {
            fetchUserData(userId)
        } else {
            // No hay sesión guardada, estado inicial no logueado
            _userState.value = UserState(isLoggedIn = false, isLoading = false)
        }
    }

    private fun fetchUserData(userId: Long) {
        viewModelScope.launch {
            _userState.value = _userState.value.copy(isLoading = true)
            try {
                val response = userRepository.getUserById(userId)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _userState.value = UserState(
                            name = user.name,
                            lastName = user.lastName ?: "",  // <-- Aquí usas ?: ""
                            email = user.email,
                            role = user.roles.firstOrNull()?.name ?: "Invitado",
                            isLoading = false,
                            isLoggedIn = true
                        )
                    } ?: run {
                        _userState.value = UserState(
                            error = "Usuario no encontrado",
                            isLoading = false,
                            isLoggedIn = false
                        )
                    }
                } else {
                    _userState.value = UserState(
                        error = "Error al obtener datos: ${response.code()}",
                        isLoading = false,
                        isLoggedIn = false
                    )
                }
            } catch (e: Exception) {
                _userState.value = UserState(
                    error = e.localizedMessage ?: "Error desconocido",
                    isLoading = false,
                    isLoggedIn = false
                )
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            TokenUtils.clearToken()
            SessionManager.clearSession()
            _userState.value = UserState(isLoggedIn = false) // Resetear estado
        }
    }
}
