package pe.edu.upeu.appturismo202501.ui.presentation.screens

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.CheckEmailDto
import pe.edu.upeu.appturismo202501.modelo.LoginDto
import pe.edu.upeu.appturismo202501.modelo.LoginResp
import pe.edu.upeu.appturismo202501.repository.LoginUserRepository
import pe.edu.upeu.appturismo202501.utils.TokenUtils
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepo: LoginUserRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _islogin = MutableLiveData(false)
    val islogin: LiveData<Boolean> get() = _islogin

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _emailExists = MutableLiveData<Boolean?>(null)
    val emailExists: LiveData<Boolean?> get() = _emailExists

    private val _userName = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _userRoles = MutableLiveData<List<String>>(emptyList())
    val userRoles: LiveData<List<String>> = _userRoles

    private val _userRole = MutableLiveData<String?>(null)  // rol principal para navegación
    val userRole: LiveData<String?> = _userRole

    val listUser = MutableLiveData<LoginResp?>()

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

    fun resetEmailCheck() {
        _emailExists.value = null
        _userName.value = ""
        _userRoles.value = emptyList()
    }

    fun loginSys(loginDto: LoginDto) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = loginRepo.loginUsuario(loginDto)
                if (response.isSuccessful) {
                    val body = response.body()
                    val token = body?.token
                    val tokenType = "Bearer"
                    if (!token.isNullOrEmpty()) {
                        TokenUtils.TOKEN_CONTENT = "$tokenType $token"
                        TokenUtils.USER_LOGIN = loginDto.email
                        listUser.value = body
                        _userRole.value = body?.roles?.firstOrNull()
                        _islogin.value = true
                    } else {
                        _errorMessage.value = "Credenciales incorrectas"
                        _islogin.value = false
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _errorMessage.value = errorBody ?: "Error de autenticación"
                    _islogin.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage ?: e.message}"
                _islogin.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
