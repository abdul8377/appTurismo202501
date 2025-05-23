package pe.edu.upeu.appturismo202501.ui.presentation.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.data.remote.ChangePasswordRequest
import pe.edu.upeu.appturismo202501.data.remote.ToggleActiveRequest
import pe.edu.upeu.appturismo202501.modelo.ApiResponse
import pe.edu.upeu.appturismo202501.modelo.UsersDto
import pe.edu.upeu.appturismo202501.repository.UserRepository
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<UsersDto>>(emptyList())
    val users: StateFlow<List<UsersDto>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Estado para controlar el éxito del cambio de contraseña
    private val _passwordChangeSuccess = MutableStateFlow(false)
    val passwordChangeSuccess: StateFlow<Boolean> = _passwordChangeSuccess

    fun loadUsers(role: String? = null, isActive: Boolean? = null) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<List<UsersDto>> = repository.getUsers(role, isActive)
                if (response.isSuccessful) {
                    _users.value = response.body() ?: emptyList()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleUserActive(userId: Long, newState: Boolean) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val request = ToggleActiveRequest(
                    is_active = newState,
                    motivo_inactivo = if (!newState) "Inactivado manualmente" else null
                )
                val response: Response<ApiResponse> = repository.toggleActive(userId, request)
                if (response.isSuccessful) {
                    loadUsers()  // recarga usuarios para actualizar estado
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error actualizando estado: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun changeUserPassword(userId: Long, password: String, passwordConfirmation: String) {
        _isLoading.value = true
        _passwordChangeSuccess.value = false  // resetear al inicio
        viewModelScope.launch {
            try {
                val request = ChangePasswordRequest(
                    password = password,
                    password_confirmation = passwordConfirmation
                )
                val response: Response<ApiResponse> = repository.changePassword(userId, request)
                if (response.isSuccessful) {
                    _errorMessage.value = null
                    _passwordChangeSuccess.value = true  // indicar éxito
                } else {
                    _errorMessage.value = "Error al cambiar contraseña: ${response.code()}"
                    _passwordChangeSuccess.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido"
                _passwordChangeSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}
