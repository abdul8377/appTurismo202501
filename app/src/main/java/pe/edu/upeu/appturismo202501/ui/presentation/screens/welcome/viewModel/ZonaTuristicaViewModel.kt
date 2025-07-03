


package pe.edu.upeu.appturismo202501.ui.presentation.screens.welcome.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.ZonaTuristicaResp
import pe.edu.upeu.appturismo202501.ui.presentation.componentsA.ActivityBanner
import pe.edu.upeu.appturismo202501.repository.ZonaTuristicaRepository
import javax.inject.Inject

@HiltViewModel
class ZonaTuristicaViewModel @Inject constructor(
    private val repo: ZonaTuristicaRepository
): ViewModel() {

    // 1) Flujo privado de DTO desde el repo
    private val _zonas = MutableStateFlow<List<ZonaTuristicaResp>>(emptyList())
    val zonas: StateFlow<List<ZonaTuristicaResp>> = _zonas.asStateFlow()

    // 2) Flujo de banners listos para la UI
    val banners: StateFlow<List<ActivityBanner>> = zonas
        .map { list ->
            list.map { zona ->
                ActivityBanner(
                    imageUrl = zona.imagenUrl.orEmpty(),
                    name     = zona.nombre
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        loadZonas()
    }

    fun loadZonas() = viewModelScope.launch {
        val resp = repo.fetchZonas()
        if (resp.isSuccessful) {
            _zonas.value = resp.body().orEmpty()
        } else {
            // opcional: logging o manejo de error
        }
    }
}