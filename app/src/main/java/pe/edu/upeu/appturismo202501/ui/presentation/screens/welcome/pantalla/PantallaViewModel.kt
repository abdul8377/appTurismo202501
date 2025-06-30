package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.pantalla

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import pe.edu.upeu.appturismo202501.repository.FavoritoRepository
import pe.edu.upeu.appturismo202501.utils.SessionManager
import javax.inject.Inject

data class PantallaUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false,
    val uiEvent: String? = null // Evento para redirigir a otra pantalla
)

@HiltViewModel
class PantallaViewModel @Inject constructor(
    private val favoritoRepo: FavoritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PantallaUiState())
    val uiState: StateFlow<PantallaUiState> = _uiState

    init {
        _uiState.update { it.copy(isLoggedIn = !SessionManager.getToken().isNullOrEmpty()) }
        if (_uiState.value.isLoggedIn) {
            // Realizamos la solicitud al servidor
            hacerSolicitud()
        }
    }

    fun hacerSolicitud() = viewModelScope.launch {
        // Muestra la barra de progreso
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        try {
            // Verificamos si el token está disponible
            val token = SessionManager.getToken()
            if (token.isNullOrEmpty()) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "No hay token en la sesión") }
                return@launch
            }

            // Realizamos la solicitud
            val response = favoritoRepo.obtenerFavoritos()

            // Verificamos si la respuesta es válida (si es un HTML o error)
            if (!response.isSuccessful || response.body() == null || response.body()?.toString()?.contains("<html>") == true) {
                // Si hay un error, borrar el token
                SessionManager.clearSession()
                _uiState.update { it.copy(isLoading = false, errorMessage = "Tu sesión ha expirado o es inválida.", uiEvent = "cerrando sesión") }
                return@launch
            }

            // Si la respuesta es exitosa, actualizamos el estado
            _uiState.update { it.copy(isLoading = false) }

        } catch (e: Exception) {
            // Manejo de excepción
            SessionManager.clearSession()
            _uiState.update { it.copy(isLoading = false, errorMessage = "Error inesperado: ${e.message}", uiEvent = "cerrando sesión") }
        }
    }
}
