package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaResp
import pe.edu.upeu.appturismo202501.repository.ZonaTuristicaRepository
import javax.inject.Inject

@HiltViewModel
class ZonaTuristicaViewModel @Inject constructor(
    private val repo: ZonaTuristicaRepository
): ViewModel() {

    private val _zonas = MutableStateFlow<List<ZonaTuristicaResp>>(emptyList())
    val zonas: StateFlow<List<ZonaTuristicaResp>> = _zonas

    init {
        viewModelScope.launch {
            val resp = repo.fetchZonas()
            if (resp.isSuccessful) {
                _zonas.value = resp.body().orEmpty()
            } else {
                // log o manejo de error
            }
        }
    }
}