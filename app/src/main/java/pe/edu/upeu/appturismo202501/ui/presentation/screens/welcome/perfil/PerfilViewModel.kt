package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.repository.LoginUserRepository
import pe.edu.upeu.appturismo202501.repository.UserRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import javax.inject.Inject

// Estado del usuario para la pantalla PerfilScreen
data class UserState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val name: String = "",
    val lastName: String? = "",
    val email: String = "",
    val role: String = "",
    val error: String? = null
)

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginRepo: LoginUserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState

    init {
        val token = SessionManager.getToken()
        if (!token.isNullOrEmpty()) {
            loadUserData(SessionManager.getUserId().toLong())
        } else {
            _userState.value = UserState(isLoggedIn = false, isLoading = false)
        }
    }

    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            _userState.value = _userState.value.copy(isLoading = true, error = null)
            try {
                val response = userRepository.getUserById(userId)
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let { user ->
                        _userState.value = UserState(
                            isLoggedIn = true,
                            name = user.name ?: "",
                            lastName = user.lastName ?: "",
                            email = user.email ?: "",
                            role = user.roles.firstOrNull()?.name ?: "",
                            isLoading = false
                        )
                    }
                } else {
                    clearInvalidSession()
                }
            } catch (e: Exception) {
                clearInvalidSession()
            }
        }
    }

    private fun clearInvalidSession() {
        SessionManager.clearSession()
        TokenUtils.TOKEN_CONTENT = ""
        _userState.value = UserState(
            isLoggedIn = false,
            error = "Tu sesión expiró o es inválida. Por favor vuelve a iniciar sesión.",
            isLoading = false
        )
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val response = loginRepo.logout()
                if (response.isSuccessful) {
                    SessionManager.clearSession()
                    TokenUtils.TOKEN_CONTENT = ""
                    _userState.value = UserState(isLoggedIn = false)
                } else {
                    _userState.value = _userState.value.copy(
                        error = "Error al cerrar sesión: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _userState.value = _userState.value.copy(
                    error = e.localizedMessage ?: "Error inesperado"
                )
            }
        }
    }
}