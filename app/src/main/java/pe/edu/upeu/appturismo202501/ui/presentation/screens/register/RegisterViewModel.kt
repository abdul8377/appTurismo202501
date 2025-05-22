package pe.edu.upeu.appturismo202501.ui.presentation.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.LoginDto
import pe.edu.upeu.appturismo202501.modelo.RegisterDto
import pe.edu.upeu.appturismo202501.repository.RegisterRepository
import pe.edu.upeu.appturismo202501.repository.LoginUserRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepo: RegisterRepository,
    private val loginRepo: LoginUserRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> get() = _lastName

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    private val _passwordConfirm = MutableStateFlow("")
    val passwordConfirm: StateFlow<String> get() = _passwordConfirm

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> get() = _registerSuccess

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> get() = _loginSuccess

    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> get() = _userRole

    fun onNameChange(value: String) { _name.value = value }
    fun onLastNameChange(value: String) { _lastName.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onPasswordConfirmChange(value: String) { _passwordConfirm.value = value }

    fun clearErrorMessage() { _errorMessage.value = null }
    fun clearLoginSuccess() { _loginSuccess.value = false }

    fun registerUser() {
        val currentName = name.value.trim()
        val currentLastName = lastName.value.trim()
        val currentEmail = email.value.trim()
        val currentPassword = password.value
        val currentPasswordConfirm = passwordConfirm.value

        if (currentName.isEmpty()) {
            _errorMessage.value = "Ingrese su nombre"
            return
        }
        if (currentLastName.isEmpty()) {
            _errorMessage.value = "Ingrese su apellido"
            return
        }
        if (currentEmail.isEmpty()) {
            _errorMessage.value = "Ingrese su correo electrónico"
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()) {
            _errorMessage.value = "Ingrese un correo válido"
            return
        }
        if (currentPassword.length < 6) {
            _errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }
        if (currentPassword != currentPasswordConfirm) {
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val registerDto = RegisterDto(
                    name = currentName,
                    last_name = currentLastName,
                    email = currentEmail,
                    password = currentPassword,
                    password_confirmation = currentPasswordConfirm,
                    country = null,
                    zip_code = null
                )
                val response = registerRepo.register(registerDto)
                if (response.isSuccessful) {
                    _registerSuccess.value = true

                    // Si registro exitoso, hacer login automático
                    val loginResponse = loginRepo.loginUsuario(LoginDto(email = currentEmail, password = currentPassword))
                    if (loginResponse.isSuccessful) {
                        // Extraer rol del usuario (ajusta según tu respuesta)
                        val roles = loginResponse.body()?.roles
                        _userRole.value = roles?.firstOrNull()
                        _loginSuccess.value = true
                    } else {
                        _errorMessage.value = "Error al iniciar sesión automáticamente: ${loginResponse.message()}"
                    }
                } else {
                    _errorMessage.value = "Error al registrar usuario: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage ?: e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
