package pe.edu.upeu.appturismo202501.ui.presentation.screens

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import pe.edu.upeu.appturismo202501.modelo.CheckEmailDto
import pe.edu.upeu.appturismo202501.modelo.LoginDto
import pe.edu.upeu.appturismo202501.modelo.LoginResp
import pe.edu.upeu.appturismo202501.modelo.RegisterDto
import pe.edu.upeu.appturismo202501.repository.LoginUserRepository
import pe.edu.upeu.appturismo202501.repository.RegisterRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginUserRepository,
    private val registerRepo: RegisterRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _emailExists = MutableLiveData<Boolean?>(null)
    val emailExists: LiveData<Boolean?> get() = _emailExists

    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _userRoles = MutableLiveData<List<String>>(emptyList())
    val userRoles: LiveData<List<String>> = _userRoles

    private val _userRole = MutableLiveData<String?>(null)
    val userRole: LiveData<String?> = _userRole

    private val _isAuthSuccess = MutableLiveData(false)
    val isAuthSuccess: LiveData<Boolean> get() = _isAuthSuccess

    // Verifica si el correo ya está registrado
    fun checkEmail(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = loginRepo.checkEmail(CheckEmailDto(email))
                if (response.isSuccessful) {
                    val body = response.body()
                    _emailExists.value = body?.exists
                    _userName.value = body?.name ?: ""
                    _userRoles.value = body?.roles ?: emptyList()
                } else {
                    _errorMessage.value = "Error al consultar el email"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage ?: e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Restablece la verificación del email
    fun resetEmailCheck() {
        _emailExists.value = null
        _userName.value = ""
        _userRoles.value = emptyList()
    }

    // Función común para manejar la sesión, tanto para login como para registro
    private fun handleLoginResponse(response: Response<LoginResp>) {
        if (response.isSuccessful) {
            val body = response.body()
            val token = body?.token
            val tokenType = "Bearer"
            if (!token.isNullOrEmpty() && body?.id != null) {
                // Guardar sesión
                SessionManager.saveSession(token, body.id, body.roles?.firstOrNull() ?: "")
                TokenUtils.TOKEN_CONTENT = "$tokenType $token"
                TokenUtils.USER_LOGIN = body.email ?: ""
                _userRole.value = body.roles?.firstOrNull()
                _isAuthSuccess.value = true
            } else {
                _errorMessage.value = "Credenciales incorrectas"
                _isAuthSuccess.value = false
            }
        } else {
            _errorMessage.value = response.message()
            _isAuthSuccess.value = false
        }
    }

    // Función para el login
    fun loginUser(loginDto: LoginDto) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = loginRepo.loginUsuario(loginDto)
                handleLoginResponse(response)
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage ?: e.message}"
                _isAuthSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Función para el registro
    fun registerUser(registerDto: RegisterDto) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = registerRepo.register(registerDto)
                if (response.isSuccessful) {
                    // Si el registro es exitoso, hacer login automáticamente
                    val loginDto = LoginDto(email = registerDto.email, password = registerDto.password)
                    loginUser(loginDto)
                } else {
                    _errorMessage.value = "Error al registrar usuario: ${response.message()}"
                    _isAuthSuccess.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage ?: e.message}"
                _isAuthSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Limpiar mensaje de error
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
