package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.UserResp
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
    val lastName: String? = "", // ✅ Cambia a nullable
    val email: String = "",
    val role: String = "",
    val error: String? = null
)


@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginRepo: LoginUserRepository // ✅ añade esta inyección
) : ViewModel() {

    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState

    init {
        val token = SessionManager.getToken()
        if (!token.isNullOrEmpty()) {
            loadUserData(SessionManager.getUserId().toLong())
        } else {
            // No hay token, actualiza claramente a estado no logueado
            _userState.value = UserState(isLoggedIn = false, isLoading = false)
        }
    }

    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            _userState.value = _userState.value.copy(isLoading = true, error = null)
            try {
                val response = userRepository.getUserById(userId)
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        _userState.value = UserState(
                            isLoggedIn = true,
                            name = user.name ?: "",
                            lastName = user.lastName ?: "",
                            email = user.email ?: "",
                            role = user.roles.firstOrNull()?.name ?: "",
                            isLoading = false
                        )
                    } ?: run {
                        _userState.value = UserState(error = "Usuario vacío", isLoading = false)
                    }
                } else {
                    _userState.value = UserState(
                        error = "Error: ${response.message()}",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _userState.value = UserState(
                    error = e.localizedMessage ?: "Error inesperado",
                    isLoading = false
                )
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            try {
                val response = loginRepo.logout()

                if (response.isSuccessful) {
                    // Elimina token local y limpia datos del usuario
                    SessionManager.clearSession()
                    TokenUtils.TOKEN_CONTENT = ""

                    // Actualiza estado a deslogueado
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


