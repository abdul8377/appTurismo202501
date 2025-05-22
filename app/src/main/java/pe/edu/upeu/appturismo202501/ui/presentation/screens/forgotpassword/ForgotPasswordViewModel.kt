package pe.edu.upeu.appturismo202501.ui.presentation.screens.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.ResetPasswordRequest
import pe.edu.upeu.appturismo202501.repository.LoginUserRepository

import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: LoginUserRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> get() = _successMessage

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun sendResetPasswordEmail(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.sendResetPasswordEmail(ResetPasswordRequest(email))
                if (response.isSuccessful) {
                    _successMessage.value = response.body()?.message ?: "Correo enviado correctamente"
                } else {
                    // Aquí podrías leer un mensaje de error más detallado si el backend lo envía
                    _errorMessage.value = "Error al enviar correo de recuperación"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de red o servidor: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccess() {
        _successMessage.value = null
    }
}
