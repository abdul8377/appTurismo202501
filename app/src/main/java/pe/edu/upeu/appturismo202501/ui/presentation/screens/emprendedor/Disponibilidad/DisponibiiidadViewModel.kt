package pe.edu.upeu.appturismo202501.ui.presentation.screens.emprendedor.Disponibilidad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.appturismo202501.modelo.DisponibilidadDto
import pe.edu.upeu.appturismo202501.repository.DisponibilidadRepository
import javax.inject.Inject

@HiltViewModel
class DisponibilidadViewModel @Inject constructor(
    private val repo: DisponibilidadRepository
): ViewModel() {

    private val _lista = MutableStateFlow<List<DisponibilidadDto>>(emptyList())
    val lista: StateFlow<List<DisponibilidadDto>> = _lista

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error



    fun create(servicioId: Long, dto: DisponibilidadDto) = viewModelScope.launch {
        repo.add(servicioId, dto)
            .onSuccess {
                _lista.update { list -> list + it }
            }
            .onFailure { _error.value = it.message }
    }

    fun delete(id: Long) = viewModelScope.launch {
        repo.remove(id)
            .onSuccess {
                _lista.update { list -> list.filterNot { it.disponibilidadId == id } }
            }
            .onFailure { _error.value = it.message }
    }

    fun fetchAll(servicioId: Long) = viewModelScope.launch {
        repo.fetchAll(servicioId)
            .onSuccess { _lista.value = it }
            .onFailure { _error.value = it.message }
    }
}